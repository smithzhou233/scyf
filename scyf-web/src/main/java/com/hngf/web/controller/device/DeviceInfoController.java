package com.hngf.web.controller.device;

import com.hngf.common.utils.Constant;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.device.DeviceInfo;
import com.hngf.entity.sys.Dict;
import com.hngf.entity.sys.FileBean;
import com.hngf.service.device.DeviceInfoService;
import com.hngf.service.sys.IndustryService;
import com.hngf.web.controller.BaseController;
import com.hngf.web.controller.risk.RiskSourceController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.*;


/**
 * 设备信息表
 *
 * @author hngf
 * @email 
 * @date 2020-06-16 15:10:13
 */
@Api(value="设备信息表",tags = {"设备信息表"})
@RestController
@RequestMapping("scyf/deviceinfo")
public class DeviceInfoController extends BaseController{
    @Autowired
    private DeviceInfoService DeviceInfoService;
    @Autowired
    private IndustryService industryService;

    /**
     * @Author: zyj
     * @Description:查看设备管理列表
     * @Param  companyId 企业id  groupId 群组id  deviceTypeId 设备类型id  deviceStatus 设备状态  keyword 关键词
     * @Date 15:51 2020/6/16
     */
    @ApiOperation(value = "查看设备管理列表",notes = "查看设备管理列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:deviceinfo:list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条数据", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "deviceTypeId", value = "设备类型id", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "deviceStatus", value = "设备状态0开启，1关闭", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "companyId", value = "企业id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "群组id", required = false, paramType = "query", dataType = "int")
    })
    public R list(@RequestParam(required = false) Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;
         params.put("companyId",getCompanyId());
        PageUtils page = DeviceInfoService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }
    /**
    * @Author: zyj
    * @Description:设备类型下拉框
    * @Param
    * @Date 17:56 2020/6/16
    */
    @ApiOperation(value = "设备类型下拉框",notes = "设备类型下拉框")
    @RequestMapping(value = "/deviceType",method = RequestMethod.GET)
    @RequiresPermissions("scyf:deviceinfo:list")
    public R deviceType(){
        String type="device_info";
        List<Dict> list = industryService.dictIndustryType(type);
        return R.ok().put("data",list);
    }
    /**
     * 信息
     */
    @ApiOperation(value = "设备修改信息",notes = "设备修改信息")
    @RequestMapping(value="/info/{deviceId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:deviceinfo:info")
    @ApiImplicitParam(name = "deviceId", value = "主键id", required = false, paramType = "query", dataType = "Long")
    public R info(@PathVariable("deviceId") Long deviceId){
        DeviceInfo DeviceInfo = DeviceInfoService.getById(deviceId);

        return R.ok().put("data", DeviceInfo);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "添加设备",notes = "添加设备")
    @PostMapping("/save")
    @RequiresPermissions("scyf:deviceinfo:save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceTypeId", value = "设备类型:1生产2消防3办公", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "deviceName", value = "设备名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceNumber", value = "设备序列号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "deviceBrand", value = "设备品牌", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "deviceModel", value = "设备型号", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "devicePosition", value = "安装位置", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "responsible", value = "责任人", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "groupId", value = "使用部门", required = false, paramType = "query", dataType = "int")
    })
    public R save(@RequestBody DeviceInfo deviceInfo){

        ValidatorUtils.validateEntity(deviceInfo);
        deviceInfo.addPrefixInit(getUserId(), getCompanyId());

        DeviceInfoService.save(deviceInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改设备",notes = "修改设备")
    @PostMapping("/update")
    @RequiresPermissions("scyf:deviceinfo:update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "主键ID", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "deviceTypeId", value = "设备类型:1生产2消防3办公", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "deviceName", value = "设备名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceNumber", value = "设备序列号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "deviceBrand", value = "设备品牌", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "deviceModel", value = "设备型号", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "devicePosition", value = "安装位置", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "responsible", value = "责任人", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "groupId", value = "使用部门", required = false, paramType = "query", dataType = "int")
    })
    public R update(@RequestBody DeviceInfo DeviceInfo){
        ValidatorUtils.validateEntity(DeviceInfo);
        DeviceInfo.setUpdatedTime(new Date());
        DeviceInfo.setUpdatedBy(getUserId());
        DeviceInfoService.update(DeviceInfo);
        
        return R.ok();
    }
    /**
    * @Author: zyj
    * @Description:删除设备信息
    * @Param
    * @Date 16:10 2020/6/16
    */
    @ApiOperation(value = "删除设备信息",notes = "删除设备信息")
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:deviceinfo:delete")
    @ApiImplicitParam(name = "deviceIds", value = "设备id", required = true, paramType = "query", dataType = "Long")
    public R delete(Long deviceIds){
        DeviceInfoService.removeById(deviceIds);

        return R.ok();
    }
    /**
    * @Author: zyj
    * @Description:设备弃用或启用
    * @Param
    * @Date 16:17 2020/6/16
    */
    @ApiOperation(value = "设备弃用或启用",notes = "设备弃用或启用")
    @RequestMapping(value = "/abandon/{deviceId}",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:deviceinfo:delete")
    @ApiImplicitParam(name = "deviceIds", value = "设备id", required = true, paramType = "query", dataType = "Long")
    public R abandon(@PathVariable("deviceId") Long deviceId){
        DeviceInfo byId = DeviceInfoService.getById(deviceId);
        if (byId==null){
            return R.error("失败");
        }
        Integer status = byId.getDeviceStatus();
        if ("1".equals(status.toString())){
            byId.setDeviceStatus(0);
        }else if ("0".equals(status.toString())){
            byId.setDeviceStatus(1);
        }
        DeviceInfoService.update(byId);
        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping(value="/deletes",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:deviceinfo:delete")
    public R deletes(@RequestBody Long[] deviceIds){
        DeviceInfoService.removeByIds(Arrays.asList(deviceIds));

        return R.ok();
    }

    /**
     * 导入设备信息
     * zyj
     * @return
     */
    @RequestMapping(value = "importDevice",method = RequestMethod.POST)
    @ApiOperation(value = "导入设备信息",notes = "导入设备信息")
    public R importDevice(MultipartFile file,HttpServletRequest request) throws Exception {
        String path="";
        //新建文件夹 放入excel表格 并返回文件路径
        path =saveFileAndBackPath(file,request);
        Long companyId=getCompanyId();
        Long groupId=getGroupId();
        Long userId=getUserId();
       DeviceInfoService.importExcel(path,companyId,groupId,userId);
       return R.ok("导入成功");
    }
    /**
     * 导出设备信息
     * zyj
     * @return
     */
    @RequestMapping(value = "exportDevice",method = RequestMethod.GET)
    @ApiOperation(value = "导出设备信息",notes = "导出设备信息")
    public void exportDevice(HttpServletResponse response){
        try {
            XSSFWorkbook wb = this.DeviceInfoService.exportExcel(getCompanyId());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=设备信息.xlsx");
            response.flushBuffer();
            OutputStream output = response.getOutputStream();
            wb.write(output);
            output.close();
        } catch (Exception var5) {
            R.error(var5.getMessage());
        }
    }


    /**
     * Excel模板下载
     * yfh
     * 2020/09/01
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "Excel模板下载", notes="Excel模板下载")
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response) throws IOException {
        String path = "/excel/device.xlsx" ;
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
     * 新建文件夹 放入excel表格 并返回文件路径
     * yfh
     * 2020/06/04
     * @param uploadfile
     * @param request
     * @return
     */
    public String saveFileAndBackPath(MultipartFile uploadfile, HttpServletRequest request) {
        String excelPath = request.getSession().getServletContext().getRealPath("/importExcel");
        FileBean fileBean = this.saveFile(uploadfile, excelPath, "/upload");
        return excelPath + "/" + fileBean.getSaveFileName();
    }

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
}
