package com.hngf.web.controller.gent;

import com.alibaba.fastjson.JSONObject;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.dto.danger.RiskSourceDto;
import com.hngf.entity.risk.RiskCtrlLevel;
import com.hngf.entity.risk.RiskSource;
import com.hngf.entity.sys.FileBean;
import com.hngf.service.risk.RiskCtrlLevelService;
import com.hngf.service.risk.RiskSourceService;
import com.hngf.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.io.FileInputStream;
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

@Api(value = "【集团】危险源信息管理",tags = {"【集团】危险源信息管理"})
@RestController
@RequestMapping("gent/risksource")
public class GentRiskSourceController  extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(GentRiskSourceController.class);

    @Autowired
    RiskSourceService riskSourceService;

    @Autowired
    private RiskCtrlLevelService riskCtrlLevelService;

    @Value("${scyf.uploaddir}")
    private String uploadDir;
    @Value("${scyf.accessPath}")
    private String accessPath;

    @ApiOperation(value = "查询设备危险源列表", notes = "" +
            "风险库接口：gent/risk/queryRiskList "

            ,response = RiskSourceDto.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riskDangerLevel", value = "风险源等级", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "危险源名称", paramType = "query", required = false, dataType = "string"),
            @ApiImplicitParam(name = "riskDangerType", value = "危险源类型 1.设备设施 2.作业活动库 3.作业环境库，4 设施场所库", paramType = "query", required = true, dataType = "int")
    })
    @GetMapping("/queryRiskDanger")
    public R queryRiskDanger(@RequestParam Map<String, Object> params) {
        params.put("companyId",getCompanyId());
        List<RiskSourceDto> tbriskSource = riskSourceService.findList(params);
        List<RiskSourceDto> resultList = new ArrayList(); // 存贮顶层的数据
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
        return R.ok().put("data",resultList);
    }

    @ApiOperation(value = "新增危险源", notes="新增危险源"   ,response = RiskSource.class)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public R save(@RequestBody RiskSource riskSource){
        riskSource.setCompanyId(getCompanyId());
        riskSource.setRiskDangerId(getId("scyf_risk_source"));
        ValidatorUtils.validateEntity(riskSource);
        riskSourceService.save(riskSource);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    public R delete(@RequestBody Long[] riskDangerIds){
        riskSourceService.removeByIds(Arrays.asList(riskDangerIds));
        return R.ok();
    }

    @ApiOperation(value = "删除单个危险源信息", notes="删除单个危险源信息")
    @RequestMapping(value="/deleteById",method = RequestMethod.DELETE)
    public R deleteById(Long riskDangerId){
        riskSourceService.removeById(riskDangerId);
        return R.ok();
    }

    @ApiOperation(value = "危险源编辑", notes="危险源编辑")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public R update(@RequestBody RiskSource RiskSource){
        ValidatorUtils.validateEntity(RiskSource);
        RiskSource.setDelFlag(0);
        riskSourceService.update(RiskSource);
        return R.ok();
    }

    @ApiOperation(value = "上传文件", notes="上传文件")
    @RequestMapping(value = "/import",method = RequestMethod.POST)
    public R importData(MultipartFile file, HttpServletRequest req) throws IOException {
        Map map=new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
        String format  =  sdf.format(new Date());
        String savePath=uploadDir + format;
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

    @ApiOperation(value = "Excel模板下载", notes="Excel模板下载")
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            Resource resource = new ClassPathResource("excel/sysRiskNew.xlsx");
            File file = resource.getFile();
            String filename = resource.getFilename();
            InputStream inputStream = new FileInputStream(file);
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
            int b = 0;
            byte[] buffer = new byte[1000000];
            while (b != -1) {
                b = inputStream.read(buffer);
                if (b != -1) out.write(buffer, 0, b);
            }
            inputStream.close();
            out.close();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 新建文件夹 放入excel表格 并返回文件路径
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
     *  危险源Excel 导入
     *  2020/06/03
     * @param file
     * @return
     */
    @ApiOperation(value = "危险源Excel 导入", notes="危险源Excel 导入")
    @RequestMapping(value = {"/uploadAll"},method = {RequestMethod.POST})
    public R uploadAll(MultipartFile file,Integer riskDangerType,HttpServletRequest request) {
        Long id=getId("scyf_risk_source");
        Map<String, Object> map =new HashMap<>();
        map.put("riskDangerId",getId("scyf_risk_source"));
        map.put("riskDangerType",riskDangerType);
        map.put("companyId",getCompanyId());
        map.put("userId",getUserId());
        String str = "数据正在处理中。。。。。";
        String path = "";
        //  new ArrayList();
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
     * 查看危险源等级
     * 2020/06/12
     * @return
     */
    @ApiOperation(value = "查看危险源等级", notes="查看危险源等级  等级名称:  dictName" +
            "等级编码：dictCode")
    @RequestMapping(value="/getRiskDangerLevel",method = RequestMethod.GET)
    public R getRiskDangerLevel(){
        return R.ok().put("data",riskSourceService.getRiskDangerLevel());
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
     * @Author: zyj
     * @Description:根据条件返回相应的列表
     * @Param companyId 企业id  riskCtrlLevelId 风险管控层级ID keyword关键词
     * @Date 16:50 2020/5/21
     */
    @ApiOperation(value = "风险管控层级表", notes="查询所属企业下的所有数据",response = RiskCtrlLevel.class)
    @RequestMapping(value = "/getCtrlLevelList",method = RequestMethod.GET)
    public R getCtrlLevelList(){
        //获取企业id  companyId
        Long companyId=getCompanyId();
        Map<String,Object> params = new HashMap<>(4);
        params.put("companyId",companyId) ;
        Integer count=riskCtrlLevelService.count(companyId);
        if (count==0){
            riskCtrlLevelService.initRiskCtrlLevel(params);
        }
        return R.ok().put("data", this.riskCtrlLevelService.getRiskCtrlLevelList(companyId));
    }
}
