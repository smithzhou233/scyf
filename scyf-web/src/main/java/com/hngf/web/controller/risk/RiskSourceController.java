package com.hngf.web.controller.risk;

import com.alibaba.fastjson.JSONObject;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.dto.danger.RiskSourceDto;
import com.hngf.dto.risk.InspectItemContentDto;
import com.hngf.dto.risk.RiskInspectItemDto;
import com.hngf.entity.risk.RiskSource;
import com.hngf.entity.sys.FileBean;
import com.hngf.service.risk.RiskCtrlService;
import com.hngf.service.risk.RiskSourceService;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


/**
 * 危险源
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Api(value = "危险源信息管理",tags = {"危险源信息管理"})
@RestController
@RequestMapping("scyf/risksource")
public class RiskSourceController extends BaseController {
    @Autowired
    private RiskSourceService riskSourceService;
    @Autowired
    private RiskCtrlService riskCtrlService;
    @Value("${scyf.uploaddir}")
    private String uploadDir;
    @Value("${scyf.accessPath}")
    private String accessPath;

    private final static Logger logger = LoggerFactory.getLogger(RiskSourceController.class);
    /**
     * 查询设备危险源列表
     * yfh
     * 2020/05/27
     * @param params
     * @return
     */
    @ApiOperation(value = "查询设备危险源列表", notes="查询设备危险源列表",response = RiskSourceDto.class)
    @ApiImplicitParams({
      @ApiImplicitParam(name = "riskDangerLevel", value = "风险源等级", paramType = "query", required = false, dataType = "int"),
      @ApiImplicitParam(name = "keyword", value = "危险源名称", paramType = "query", required = false, dataType = "string"),
      @ApiImplicitParam(name = "riskDangerType", value = "危险源类型", paramType = "query", required = true, dataType = "int")
    })
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public R list(@RequestParam(required = false) Map<String, Object> params){
        params.put("companyId",getCompanyId());
        List<RiskSourceDto> tbriskSource = riskSourceService.findList(params);
        List<RiskSourceDto> resultList = new ArrayList<RiskSourceDto>(); // 存贮顶层的数据
        Map<Object ,Object> treeMap = new HashMap();
        Object itemTree;
        for(int i = 0;i<tbriskSource.size() && !tbriskSource.isEmpty();i++){
            itemTree = tbriskSource.get(i);
            treeMap.put(tbriskSource.get(i).getRiskDangerId(),tbriskSource.get(i));// 把所有的数据都放到map中
        }
        for(int i =0;i<tbriskSource.size();i++){
            if(!treeMap.containsKey(tbriskSource.get(i).getParentRiskDangerId())){
                resultList.add(tbriskSource.get(i));
            }
        }
        // 循环数据，将数据放到该节点的父节点的children属性中
        for(int i =0 ;i<tbriskSource.size()&& !tbriskSource.isEmpty();i++){
            RiskSourceDto riskSource = (RiskSourceDto)treeMap.get(tbriskSource.get(i).getParentRiskDangerId());
            if(riskSource!=null ){
                if(riskSource.getChildren() == null){
                    riskSource.setChildren(new ArrayList<RiskSourceDto>());
                }
                riskSource.getChildren().add(tbriskSource.get(i)); // 添加到父节点的ChildList集合下
                treeMap.put(tbriskSource.get(i).getParentRiskDangerId(),riskSource);  // 把放好的数据放回到map中
            }

        }
        return R.ok().put("data", resultList);
    }

    /**
     * 查询危险源的风险管控措施
     * @param params
     * @return
     */
    @GetMapping("/getRiskMeasureByDangerId")
    @ApiOperation(value = "查询危险源的风险管控措施", response = InspectItemContentDto.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dangerId", value = "危险源ID", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "inspectDefId", value = "检查表ID", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "scheduleId", value = "任务ID", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "riskPointId", value = "风险点ID", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "checkRecordNo", value = "检查记录编号", paramType = "query", required = true, dataType = "int")
    })
    public R getRiskMeasureByDangerId(@RequestParam() Map<String, Object> params){
        Assert.isNull(params.get("dangerId"),"危险源ID不能为空");
      //  Assert.isNull(params.get("inspectDefId"),"检查表ID不能为空");
        Assert.isNull(params.get("scheduleId"),"任务ID不能为空");
        Assert.isNull(params.get("riskPointId"),"风险点ID不能为空");
        Assert.isNull(params.get("checkRecordNo"),"检查记录编号不能为空");

        params.put("companyId", getCompanyId());
        params.put("positionId", getPositionId());
        params.put("userId", getUserId());

        Integer ctlLevel = this.riskCtrlService.getCurrentUserCtlLevel(params);
        params.put("ctlLevel", ctlLevel);
        List<RiskInspectItemDto> controlCheckItemList = this.riskSourceService.getControlInspectItemList(params);
        List<InspectItemContentDto> itemContentDtos = new ArrayList();
        if (!controlCheckItemList.isEmpty()) {
            for (RiskInspectItemDto item : controlCheckItemList) {
                if (null != item && null != item.getInspectItemes() && !item.getInspectItemes().isEmpty()) {
                    itemContentDtos.addAll(item.getInspectItemes());
                }
            }
        }
        return R.ok().put("data",itemContentDtos);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{riskDangerId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:risksource:info")
    public R info(@PathVariable("riskDangerId") Long riskDangerId){
        RiskSource RiskSource = riskSourceService.getById(riskDangerId);

        return R.ok().put("RiskSource", RiskSource);
    }
    /**
    * @Author: zyj
    * @Description:作业活动比较图统计图柱形图
    * @Param riskDangerType危险源类型（1 设备设施 2 作业活动） companyId企业id
    * @Date 16:37 2020/6/19
    */
    @GetMapping(value = "/queryRiskLevel")
    @RequiresPermissions("scyf:risksource:list")
    @ApiOperation(value = "【地矿专用】作业活动比较图统计图柱形图")
    @ApiImplicitParam(name = "riskDangerType", value = "危险源类型（1 设备设施 2 作业活动）", paramType = "query",defaultValue = "2",required = true, dataType = "int")
     public R queryRiskLevel(Integer riskDangerType) {
        Long companyId = getCompanyId();
        List<Map<String, Object>> maps = riskSourceService.queryRiskLevel(riskDangerType, companyId);
        List resultList = getBarChartsList(maps) ;
        return R.ok().put("data", maps).put("list",resultList);
     }
     private List getBarChartsList(List<Map<String, Object>> maps  ){
         List list=new ArrayList();
         if (maps.size() > 0) {
             for (int i = 0; i < maps.size(); i++) {
                 Map map1 = new HashMap();
                 Map map2=new HashMap();
                 Map map3=new HashMap();
                 map1.put("value", maps.get(i).get("numbers"));
                 String color1="#FF0000";
                 String color2="#FF6100";
                 String color3="#FFFF00";
                 String color4="#0000FF";
                 String riskDangerLevel = maps.get(i).get("riskDangerLevel").toString();
                 if (riskDangerLevel.equals("1")){
                     map3.put("color",color1);
                 }
                 if (riskDangerLevel.equals("2")){
                     map3.put("color",color2);
                 }
                 if (riskDangerLevel.equals("3")){
                     map3.put("color",color3);
                 }
                 if (riskDangerLevel.equals("4")){
                     map3.put("color",color4);
                 }
                 map2.put("normal",map3);
                 map1.put("itemStyle",map2);
                 list.add(map1);
             }
         }
         return  list ;
     }
    /**
    * @Author: zyj
    * @Description:作业活动比较图统计图柱形图
    * @Param riskDangerType危险源类型（1 设备设施 2 作业活动） companyId企业id
    * @Date 16:37 2020/6/19
    */
    @GetMapping(value = "/queryRiskLevelAnalyze")
    @RequiresPermissions("scyf:risksource:list")
    @ApiOperation(value = "【非地矿企业】作业活动比较图统计图柱形图")
    @ApiImplicitParam(name = "riskDangerType", value = "危险源类型（1 设备设施 2 作业活动）", paramType = "query",defaultValue = "2",required = true, dataType = "int")
     public R queryRiskLevelAnalyze(Integer riskDangerType) {
        Long companyId = getCompanyId();
        List<Map<String, Object>> maps = riskSourceService.queryRiskLevelAnalyze(riskDangerType, companyId);
        List resultList = getBarChartsList(maps) ;
        return R.ok().put("data", maps).put("list",resultList);
     }


    /**
     * 新增危险源
     * yfh
     * 2020/05/28
     * @param riskSource
     * @return
     */
    @ApiOperation(value = "新增危险源", notes="新增危险源")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @RequiresPermissions("scyf:risksource:save")
    public R save(@RequestBody RiskSource riskSource){
        riskSource.setCompanyId(getCompanyId());
        riskSource.setRiskDangerId(getId("scyf_risk_source"));
        ValidatorUtils.validateEntity(riskSource);
        riskSourceService.save(riskSource);

        return R.ok();
    }

    /**
     * 危险源编辑
     * yfh
     * 2020/05/29
     * @param RiskSource
     * @return
     */
    @ApiOperation(value = "危险源编辑", notes="危险源编辑")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
   // @RequiresPermissions("scyf:risksource:update")
    public R update(@RequestBody RiskSource RiskSource){
        ValidatorUtils.validateEntity(RiskSource);
        RiskSource.setDelFlag(0);
        riskSourceService.update(RiskSource);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:risksource:delete")
    public R delete(@RequestBody Long[] riskDangerIds){
        riskSourceService.removeByIds(Arrays.asList(riskDangerIds));

        return R.ok();
    }

    /**
     * 删除单个危险源信息
     * yfh
     * 2020/06/08
     * @param riskDangerId
     * @return
     */
    @ApiOperation(value = "删除单个危险源信息", notes="删除单个危险源信息")
    @RequestMapping(value="/deleteById",method = RequestMethod.DELETE)
  //  @RequiresPermissions("scyf:risksource:delete")
    public R deleteById(Long riskDangerId){
        riskSourceService.removeById(riskDangerId);
        return R.ok();
    }

    /**
     * 上传文件
     * yfh
     * 2020/05/29
     * @param file
     * @param req
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "上传文件", notes="上传文件")
    @RequestMapping(value = "/import",method = RequestMethod.POST)
    public R importData(MultipartFile file, HttpServletRequest req) throws IOException {
        Map map=new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
        String format = sdf.format(new Date());
        String savePath = uploadDir + format;
        File folder = new File(savePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        file.transferTo(new File(folder,newName));
        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source" + format + newName;
        String imgName=format + newName;
        map.put("url",url);
        map.put("imgName",imgName);
        return R.ok().put("data", map);
    }

    /**
     * 新增编辑风险
     * yfh
     * 2020/06/01
     * @param map
     * @return
     */
    @ApiOperation(value = "新增编辑风险", notes="新增编辑风险")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "riskCode", value = "风险编码", paramType = "json", required = true, dataType = "string"),
        @ApiImplicitParam(name = "riskDangerId", value = "危险源ID", paramType = "json", required = true, dataType = "int"),
        @ApiImplicitParam(name = "riskDangerType", value = "危险源类型", paramType = "json", required = true, dataType = "int"),
        @ApiImplicitParam(name = "riskHramFactor", value = "危险因素", paramType = "json", required = true, dataType = "string"),
        @ApiImplicitParam(name = "riskId", value = "风险ID", paramType = "json", required = false, dataType = "int")
    })
    @RequestMapping(value = "/saveRisk",method = RequestMethod.POST)
    public R saveRisk(@ApiIgnore @RequestParam(required = false) Map<String, Object> map) {
        map.put("companyId",getCompanyId());
        JSONObject paramsJson = new JSONObject(map);
        return riskSourceService.addEntRisk(paramsJson,getUserId(),getId("scyf_risk_source"));
    }

    /**
     * Excel模板下载
     * yfh
     * 2020/06/02
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "Excel模板下载", notes="Excel模板下载")
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    @ApiImplicitParam(name = "type", value = "模板类型(1:LC类型  2：LEC类型  3：LS类型  4：设备设施  5：作业活动  14：环境类)", paramType = "query", required = true, dataType = "string")
    public void downloadFile(HttpServletResponse response,HttpServletRequest request,String type) throws IOException {
        String path = "/excel/sysRiskNew.xlsx" ;
        if("1".equals(type)){
            path = "/excel/lcExcel.xlsx";
        }else if("2".equals(type)){
            path = "/excel/lecExcel.xlsx";
        }else if("3".equals(type)){
            path = "/excel/lsExcel.xlsx";
        }else if("4".equals(type)){
            path = "/excel/riskSourceDevice.xlsx";
        }else if("5".equals(type)){
            path = "/excel/riskSourceActivity.xlsx";
        }else if("14".equals(type)){
            path = "/excel/riskSourceActivity.xlsx";
        }

        String fileName = path.substring(path.lastIndexOf("/") + 1);
        downExcelTemplate(response, path, fileName);
    }


    /**
     * 高速Excel模板下载
     * yfh
     * 2020/09/17
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "高速Excel模板下载", notes="高速Excel模板下载 </br> " +
            "1、/downloadGsA ，/downloadGsA/0 下载高速excel模板A7版 </br>" +
            "2、/downloadGsA/1 下载高速Excel模块道路A8版 </br>" +
            "3、/downloadGsA/2 下载高速Excel模块桥梁A7版 </br>" +
            "4、/downloadGsA/3 下载高速Excel模块隧道A8版 </br>")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "高速Excel评价模板类型", paramType = "path", dataType = "int")
    })
    @RequestMapping(value = {"/downloadGsA","/downloadGsA/{type}" },method = RequestMethod.GET)
    public void downloadGsA(@ApiIgnore @PathVariable (value = "type", required = false)  String type, HttpServletResponse response) throws IOException {
        StringBuilder path = new StringBuilder();
        if(null == type || "0".equals(type) ){
            path.append("/excel/gsA.xlsx")  ;
        }else if("1".equals(type)){
            path.append("/excel/gsA8dl.xlsx")  ;
        }else if("2".equals(type)){
            path.append("/excel/gsA7ql.xlsx")  ;
        }else if("3".equals(type)){
            path.append("/excel/gsA8sd.xlsx")  ;
        }
        String fileName = path.toString().substring(path.toString().lastIndexOf("/") + 1);
        downExcelTemplate(response, path.toString(), fileName);
    }


    /**
     * 地矿Excel模板下载
     * yfh
     * 2020/09/02
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "地矿Excel模板下载", notes="地矿Excel模板下载")
    @RequestMapping(value = "/downloadExcel",method = RequestMethod.GET)
    @ApiImplicitParam(name = "type", value = "模板类型( 1：设备设施  2：作业活动  14：环境类)", paramType = "query", required = true, dataType = "string")
    public void downloadExcel(HttpServletResponse response,HttpServletRequest request,String type) throws IOException {
        String path = "" ;
        if("1".equals(type)){
            path = "/excel/riskSourceDevice.xlsx";
        }else if("2".equals(type)){
            path = "/excel/riskSourceActivity.xlsx";
        }else if("14".equals(type)){
            path = "/excel/riskSourceActivity.xlsx";
        }

        String fileName = path.substring(path.lastIndexOf("/") + 1);
        downExcelTemplate(response, path, fileName);
    }


    /**
     * 水利Excel模板下载
     * yfh
     * 2020/08/26
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "水利Excel模板下载", notes="水利Excel模板下载")
    @RequestMapping(value = "/downloadRiverFile",method = RequestMethod.GET)
    @ApiImplicitParam(name = "type", value = "模板类型(1:LS类型  2：LEC类型)", paramType = "query", required = true, dataType = "string")
    public void downloadRiverFile(HttpServletResponse response,HttpServletRequest request,String type) throws IOException {
        String path = "" ;
        if("1".equals(type)){
            path = "/excel/riverLs.xlsx";
        }else if("2".equals(type)){
            path = "/excel/riverLec.xlsx";
        }

        String fileName = path.substring(path.lastIndexOf("/") + 1);
        downExcelTemplate(response, path, fileName);
    }


    private void downExcelTemplate(HttpServletResponse response, String path, String fileName) throws IOException {
        /** 将文件名称进行编码 */
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("content-type:octet-stream");
        /** 读取服务器端模板文件*/
        InputStream inputStream = RiskSourceController.class.getResourceAsStream(path);

        /** 将流中内容写出去 .*/
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        outputStream.close();
    }

    /**
     *  危险源Excel 导入
     *  yfh
     *  2020/06/03
     * @param file
     * @return
     */
    @ApiOperation(value = "危险源Excel 导入", notes="危险源Excel 导入")
    @RequestMapping(value = {"/uploadAll"},method = {RequestMethod.POST})
    public R uploadAll(MultipartFile file,Integer riskDangerType,HttpServletRequest request) {
        Long id=getId("scyf_risk_source");
        Map<String, Object> map =new HashMap<>();
        map.put("companyId",getCompanyId());
        map.put("userId",getUserId());
        map.put("riskDangerId",getId("scyf_risk_source"));
        map.put("riskDangerType",riskDangerType);
        String str = "数据正在处理中。。。。。";
        String path = "";
        new ArrayList();
        JSONObject paramsJsonPage = new JSONObject(map);

        try {
            String originalFileName = file.getOriginalFilename();
            //判断文件后缀名 是否为xlsx格式文件
            String extName = originalFileName.substring(originalFileName.lastIndexOf(46) + 1, originalFileName.length());
            if (!"xlsx".equals(extName)) {
                return R.error("文件后缀名必须为xlsx！");
            }
            //新建文件夹 放入excel表格 并返回文件路径
            path =saveFileAndBackPath(file,request);
            //检验excel表格填写内容是否规范
            riskSourceService.importExcelCheck(path, paramsJsonPage);
        } catch (Exception var9) {
            return  R.error(var9.getMessage());
        }

            try {
                //根据文件路径 获取文件 读取文件内容 导入数据库
                riskSourceService.importExcel(path, paramsJsonPage,id);
            } catch (Exception var4) {
               return R.error(var4.getMessage());
            }
        return R.ok(str);
    }

    /**
     *  危险源lec/lc/ls Excel 导入  交通
     *  yfh
     *  2020/08/25
     * @param file
     * @return
     */
    @ApiOperation(value = "危险源lec/lc/ls Excel 导入", notes="危险源lec/lc/ls Excel 导入")
    @RequestMapping(value = {"/uploadLecAll"},method = {RequestMethod.POST})
    @ApiImplicitParam(name = "type", value = "模板类型(LC：LC模板导入  LEC：LEC导入  LS：LS导入)", paramType = "query", required = true, dataType = "string")
    public R uploadLecAll(MultipartFile file,Integer riskDangerType,HttpServletRequest request,String type) {
        Long id=getId("scyf_risk_source");
        Map<String, Object> map =new HashMap<>();
        map.put("companyId",getCompanyId());
        map.put("userId",getUserId());
        map.put("riskDangerId",getId("scyf_risk_source"));
        map.put("riskDangerType",riskDangerType);
        String str = "数据正在处理中。。。。。";
        String path = "";
        new ArrayList();
        JSONObject paramsJsonPage = new JSONObject(map);

        try {
            String originalFileName = file.getOriginalFilename();
            //判断文件后缀名 是否为xlsx格式文件
            String extName = originalFileName.substring(originalFileName.lastIndexOf(46) + 1, originalFileName.length());
            if (!"xlsx".equals(extName)) {
                return R.error("文件后缀名必须为xlsx！");
            }
            //新建文件夹 放入excel表格 并返回文件路径
            path =saveFileAndBackPath(file,request);
            if("LEC".equals(type)){
                //检验LecExcel表格填写内容是否规范
                riskSourceService.importLecExcel(path, paramsJsonPage);
            }else if("LC".equals(type)){
                //检验LcExcel表格填写内容是否规范
                riskSourceService.importLcExcel(path, paramsJsonPage);
            }else if("LS".equals(type)){
                //检验LsExcel表格填写内容是否规范
                riskSourceService.importLsExcel(path, paramsJsonPage);
            }

        } catch (Exception var9) {
            return  R.error(var9.getMessage());
        }

        try {
            //根据文件路径 获取文件 读取文件内容 导入数据库
            riskSourceService.importNewExcel(path, paramsJsonPage,id,type);
        } catch (Exception var4) {
            return R.error(var4.getMessage());
        }
        return R.ok(str);
    }


    /**
     *  危险源  高速A评价方式导入
     *  yfh
     *  2020/09/17
     * @param file
     * @return
     */
    @ApiOperation(value = "危险源 高速A评价 Excel 导入", notes="危险源 高速A评价 Excel 导入 </br> " +
            "excelType 不传值或者值是0 ：危险源 高速A评价 Excel 导入 A7版  </br>" +
            "excelType = 1 危险源 道路评价Excel导入 </br> " +
            "excelType = 2 危险源 桥梁评价Excel导入 </br> " +
            "excelType = 3 危险源 隧道评价Excel导入 </br> ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskDangerType",value = "危险源类型", paramType = "query" , required = true, dataType = "int"),
            @ApiImplicitParam(name = "excelType",value = "评价excel类型：高速A评价Excel--0或着空；评价道路--1；评价桥梁--2；评价隧道--3； ", paramType = "query" ,  dataType = "int")
    })
    @RequestMapping(value = {"/uploadAExcel"},method = {RequestMethod.POST})
    public R uploadAExcel(MultipartFile file, @ApiIgnore @RequestParam(required = false) Integer riskDangerType, @ApiIgnore @RequestParam(required = false) Integer excelType,HttpServletRequest request) {
        Long id=getId("scyf_risk_source");
        Map<String, Object> map =new HashMap<>();
        map.put("companyId",getCompanyId());
        map.put("userId",getUserId());
        map.put("riskDangerId",getId("scyf_risk_source"));
        map.put("riskDangerType",riskDangerType);
        if(null == excelType ){
            map.put("dealType","0");
        }else {
            map.put("dealType",excelType);
        }
        String str = "数据正在处理中。。。。。";
        String path = "";
        new ArrayList();
        JSONObject paramsJsonPage = new JSONObject(map);

        try {
            String originalFileName = file.getOriginalFilename();
            //判断文件后缀名 是否为xlsx格式文件
            String extName = originalFileName.substring(originalFileName.lastIndexOf(46) + 1, originalFileName.length());
            if (!"xlsx".equals(extName)) {
                return R.error("文件后缀名必须为xlsx！");
            }
            //新建文件夹 放入excel表格 并返回文件路径
            path =saveFileAndBackPath(file,request);


        } catch (Exception var9) {
            return  R.error(var9.getMessage());
        }

        try {
            //根据文件路径 获取文件 读取文件内容 导入数据库
            riskSourceService.importNewAExcel(path, paramsJsonPage,id);
        } catch (Exception var4) {
            return R.error(var4.getMessage());
        }
        return R.ok(str);
    }


    /**
     *  水利危险源lec/ls Excel 导入
     *  yfh
     *  2020/08/26
     * @param file
     * @return
     */
    @ApiOperation(value = "水利危险源lec/ls Excel 导入", notes="水利危险源lec/ls Excel 导入")
    @RequestMapping(value = {"/uploadRiverAll"},method = {RequestMethod.POST})
    @ApiImplicitParam(name = "type", value = "水利模板类型(LS：LS模板导入  LEC：LEC导入)", paramType = "query", required = true, dataType = "string")
    public R uploadRiverAll(MultipartFile file,Integer riskDangerType,HttpServletRequest request,String type) {
        Long id=getId("scyf_risk_source");
        Map<String, Object> map =new HashMap<>();
        map.put("companyId",getCompanyId());
        map.put("userId",getUserId());
        map.put("riskDangerId",getId("scyf_risk_source"));
        map.put("riskDangerType",riskDangerType);
        String str = "数据正在处理中。。。。。";
        String path = "";
        new ArrayList();
        JSONObject paramsJsonPage = new JSONObject(map);

        try {
            String originalFileName = file.getOriginalFilename();
            //判断文件后缀名 是否为xlsx格式文件
            String extName = originalFileName.substring(originalFileName.lastIndexOf(46) + 1, originalFileName.length());
            if (!"xlsx".equals(extName)) {
                return R.error("文件后缀名必须为xlsx！");
            }
            //新建文件夹 放入excel表格 并返回文件路径
            path =saveFileAndBackPath(file,request);
            if("LEC".equals(type)){
                //检验LecExcel表格填写内容是否规范
                riskSourceService.importRiverLecExcel(path, paramsJsonPage);
            }else if("LS".equals(type)){
                //检验LcExcel表格填写内容是否规范
                riskSourceService.importRiverLsExcel(path, paramsJsonPage);
            }

        } catch (Exception var9) {
            return  R.error(var9.getMessage());
        }

        try {
            //根据文件路径 获取文件 读取文件内容 导入数据库
            riskSourceService.importRiverNewExcel(path, paramsJsonPage,id,type);
        } catch (Exception var4) {
            return R.error(var4.getMessage());
        }
        return R.ok(str);
    }

    /**
     * 新建文件夹 放入excel表格 并返回文件路径
     * yfh
     * 2020/06/04
     * @param uploadfile
     * @param request
     * @return
     */
    public String saveFileAndBackPath(MultipartFile uploadfile,HttpServletRequest request) {
        String excelPath = request.getSession().getServletContext().getRealPath("/importExcel");
        FileBean fileBean = this.saveFile(uploadfile, excelPath, "/upload");
        return excelPath + "/" + fileBean.getSaveFileName();
    }

    /**
     * 新建文件夹 放入excel表格
     * @param uploadfile
     * @param uploadPath
     * @param prefix
     * @return
     */
    public FileBean saveFile(MultipartFile uploadfile, String uploadPath, String prefix) {
        FileBean fileBean = new FileBean();

        try {
            if (uploadfile.isEmpty()) {
                return fileBean;
            } else {
                File dir = new File(uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String originalFileName = uploadfile.getOriginalFilename();
                String extName = originalFileName.substring(originalFileName.lastIndexOf(46) + 1, originalFileName.length());
                //获取保存文件名称
                String saveFileName = this.generateSaveFileName(extName);
                if ("apk".equals(extName)) {
                    saveFileName = originalFileName;
                }

                Long fileSize = uploadfile.getSize();
                logger.debug("###" + originalFileName);
                logger.debug("###" + extName);
                logger.debug("###" + saveFileName);
                logger.debug("###" + fileSize);
                String filepath = Paths.get(uploadPath, saveFileName).toString();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
                stream.write(uploadfile.getBytes());
                stream.close();
                String path = uploadPath + saveFileName;
                String url = path.substring(0, path.lastIndexOf("."));
                if (!"mp4".equals(extName) && !"3gp".equals(extName) && !"mov".equals(extName) && !"m4v".equals(extName) && !"rm".equals(extName) && "rmvb".equals(extName)) {
                }

                fileBean.setOriFileName(originalFileName);
                fileBean.setSaveFileName(saveFileName);
                fileBean.setExt(extName);
                fileBean.setSize(fileSize);
                fileBean.setPath(prefix + "/" + saveFileName);
                fileBean.setMimeType(uploadfile.getContentType());
                return fileBean;
            }
        } catch (IOException var14) {
            throw new RuntimeException("upload file");
        }
    }

    /**
     * 获取保存文件名称
     * @param extName
     * @return
     */
    private String generateSaveFileName(String extName) {
        String fileName = "";
        Calendar calendar = Calendar.getInstance();
        fileName = fileName + calendar.get(1);
        fileName = fileName + (calendar.get(2) + 1);
        fileName = fileName + calendar.get(5);
        fileName = fileName + calendar.get(11);
        fileName = fileName + calendar.get(12);
        fileName = fileName + calendar.get(13);
        fileName = fileName + calendar.get(14);
        Random random = new Random();
        fileName = fileName + random.nextInt(9999);
        fileName = fileName + "." + extName;
        return fileName;
    }

    /**
     * 危险源Excel 导出
     * yfh
     * 2020/06/04
     * @param response
     * @param riskDangerType
     * @return
     */
    @ApiOperation(value = "危险源Excel 导出", notes="危险源Excel 导出")
    @RequestMapping(value = {"/exportAll"},method = {RequestMethod.GET})
    @ApiImplicitParam(name = "riskDangerType", value = "1：设备设施导出   2：作业活动导出  14：环境类导出", paramType = "query", required = true, dataType = "string")
    public void exportAll(HttpServletResponse response,String riskDangerType) {
        try {
            XSSFWorkbook wb = this.riskSourceService.exportExcel(Integer.valueOf(riskDangerType),getCompanyId());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=riskSource.xlsx");
            response.flushBuffer();
            OutputStream output = response.getOutputStream();
            wb.write(output);
            output.close();
        } catch (Exception var5) {
            R.error(var5.getMessage());
        }
    }


    /**
     * 危险源 LEC/LC/LS Excel 导出  交通
     * yfh
     * 2020/08/26
     * @param response
     * @param riskDangerType
     * @return
     */
    @ApiOperation(value = "危险源LEC/LC/LS Excel 导出  交通", notes="危险源LEC/LC/LS Excel 导出  交通")
    @RequestMapping(value = {"/exportLAll"},method = {RequestMethod.GET})
    public void exportLAll(HttpServletResponse response,String riskDangerType) {
        try {
            XSSFWorkbook wb = this.riskSourceService.exportLExcel(Integer.valueOf(riskDangerType),getCompanyId());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=riskSource.xlsx");
            response.flushBuffer();
            OutputStream output = response.getOutputStream();
            wb.write(output);
            output.close();
        } catch (Exception var5) {
            R.error(var5.getMessage());
        }
    }


    /**
     * 高速  危险源 A评价方式 导出
     * yfh
     * 2020/09/17
     * @param response
     * @param riskDangerType
     * @return
     */
    @ApiOperation(value = "高速  危险源 A评价方式 导出", notes="高速  危险源 A评价方式 导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskDangerType", value = "危险源类型", paramType = "query", dataType = "String", required = true ),
            @ApiImplicitParam(name = "evaluateType", value = "评价方式 : 0或不传值 高速A7;1 道路；2桥梁；3隧道", paramType = "query", dataType = "int" )
    })
    @RequestMapping(value = {"/exportAExcel"},method = {RequestMethod.GET})
    public void exportAExcel(HttpServletResponse response,String riskDangerType, Integer evaluateType) {
        try {
            XSSFWorkbook wb = this.riskSourceService.exportAExcel(Integer.valueOf(riskDangerType),getCompanyId(),evaluateType);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=riskSource.xlsx");
            response.flushBuffer();
            OutputStream output = response.getOutputStream();
            wb.write(output);
            output.close();
        } catch (Exception var5) {
            R.error(var5.getMessage());
        }
    }



    /**
     * 危险源 LEC/LS Excel 导出  水利
     * yfh
     * 2020/08/27
     * @param response
     * @param riskDangerType
     * @return
     */
    @ApiOperation(value = "危险源LEC/LC Excel 导出  水利", notes="危险源LEC/LC Excel 导出  水利")
    @RequestMapping(value = {"/exportRiverAll"},method = {RequestMethod.GET})
    public void exportRiverAll(HttpServletResponse response,String riskDangerType) {
        try {
            XSSFWorkbook wb = this.riskSourceService.exportRiverExcel(Integer.valueOf(riskDangerType),getCompanyId());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=riskSource.xlsx");
            response.flushBuffer();
            OutputStream output = response.getOutputStream();
            wb.write(output);
            output.close();
        } catch (Exception var5) {
            R.error(var5.getMessage());
        }
    }

    /**
     * 查看危险源等级
     * yfh
     * 2020/06/12
     * @return
     */
    @ApiOperation(value = "查看危险源等级", notes="查看危险源等级  等级名称:  dictName" +
            "等级编码：dictCode")
    @RequestMapping(value="/getRiskDangerLevel",method = RequestMethod.GET)
    public R getRiskDangerLevel(){
        return R.ok().put("data",riskSourceService.getRiskDangerLevel());
    }
}
