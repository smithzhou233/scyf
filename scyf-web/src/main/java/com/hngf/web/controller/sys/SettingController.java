package com.hngf.web.controller.sys;

import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.entity.sys.Setting;
import com.hngf.entity.sys.User;
import com.hngf.service.sys.SettingService;
import com.hngf.web.common.annotation.ApiParameterJsonObject;
import com.hngf.web.common.annotation.ApiJsonProperty;
import com.hngf.web.common.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@RestController
@RequestMapping("sys/setting")
@Api(value = "系统配置接口",tags = {"系统配置接口"})
public class SettingController  {

    private final static String SUFFIX_NAME = "apk";

    private final static String APP_FILE_PATH_NAME = "androidApp/" ;

    private final static String SETTING_ANDROID_KEY = "android" ;
    private final static String SETTING_URL_KEY = "android_url" ;
    private final static String SETTING_TITLE_KEY = "android_title" ;
    private final static String SETTING_VERSION_CODE_KEY = "android_version" ;
    private final static String SETTING_VERSION_NAME_KEY = "android_version_name" ;
    private final static String SETTING_DESC_KEY = "android_desc" ;

    @Autowired
    private SettingService settingService;

    @Value("${scyf.uploaddir}")
    private String uploadDir;

    /**
     * 列表
     */
    @ApiOperation(value="app配置信息",response = Setting.class)
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    /* @RequiresPermissions("sys:setting:list")*/
    public R list(@ApiIgnore @RequestParam(required = false) Map<String, Object> params, HttpServletRequest req){
        PageUtils page = settingService.queryPage(params);
        return R.ok().put("page", page).put("path",req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/source");
    }


    /**
     * 信息
     */
    @ApiOperation(value="配置信息",response = Setting.class)
    @RequestMapping(value="/info/{settingId}",method = RequestMethod.GET)
    public R info(@PathVariable("settingId") Long settingId){
        Setting setting = settingService.getById(settingId);
        return R.ok().put("Setting", setting);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:setting:save")
    public R save(@RequestBody Setting Setting){

        ValidatorUtils.validateEntity(Setting);
        settingService.save(Setting);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:setting:update")
    public R update(@RequestBody Setting Setting){
        ValidatorUtils.validateEntity(Setting);
        settingService.update(Setting);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("sys:setting:delete")
    public R delete(@RequestBody Long[] settingIds){
        settingService.removeByIds(Arrays.asList(settingIds));
        return R.ok();
    }

    /**
     * 上传app安装包
     * lxf
     * 2020/08/31
     * @param file
     * @param req
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "app安装包上传", notes="上传app安装包")
    @PostMapping("/upload/app/android")
    //@RequiresPermissions("sys:setting:update")
    public R uploadAppInstallationPackage(MultipartFile file, @ApiParameterJsonObject(name = "params", value = {
            @ApiJsonProperty(key = "versionCode", example = "必填项", description = "版本号"),
            @ApiJsonProperty(key = "versionName", example = "必填项", description = "版本名称"),
            @ApiJsonProperty(key = "androidTitle", example = "必填项", description = "app标题"),
            @ApiJsonProperty(key = "androidDesc", example = "非必填项", description = "app描述")
    }) @RequestParam Map<String,Object> params, HttpServletRequest req)  {
        if(null == file || file.isEmpty() ){
            throw new ScyfException("安装包不能为空");
        }
        Object versionCodeObj = params.get("versionCode");
        if(null == versionCodeObj || StringUtils.isBlank(versionCodeObj.toString())){
            throw new ScyfException("版本号不能为空");
        }
        String versionCode = versionCodeObj.toString();
        if(versionCode.length() >200 ){
            throw new ScyfException("版本号长度不能超过200");
        }
        Object versionNameObj = params.get("versionName");
        if(null == versionNameObj || StringUtils.isBlank(versionNameObj.toString())){
            throw new ScyfException("版本名称不能为空");
        }
        String versionName = versionNameObj.toString() ;
        if(versionName.length() >200 ){
            throw new ScyfException("版本名称不能超过200");
        }
        Object androidTitleObj = params.get("androidTitle");
        if(null == androidTitleObj || StringUtils.isBlank(androidTitleObj.toString())){
            throw new ScyfException("app标题不能为空");
        }
        String androidTitle = androidTitleObj.toString();
        if(androidTitle.length() >200 ){
            throw new ScyfException("app标题长度不能超过200");
        }
        Object androidDescObj = params.get("androidDesc");
        String androidDesc = "";
        if(null != androidDescObj && StringUtils.isNotBlank(androidDescObj.toString())){
            androidDesc = androidDescObj.toString();
            if(androidDesc.length() >200 ){
                throw new ScyfException("app描述长度不能超过200");
            }
        }
        StringBuffer uploadPath = new StringBuffer(uploadDir);
        if(!uploadDir.endsWith("/")){
            uploadPath.append("/");
        }
        uploadPath.append(APP_FILE_PATH_NAME);
        File folder = new File(uploadPath.toString());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String extendName = oldName .substring(oldName .lastIndexOf(".")+1);
        if(!SUFFIX_NAME.equals(extendName)){
            throw new ScyfException("安装包格式错误");
        }
        String newName = androidTitle.concat("_").concat(versionCode).concat(".").concat(SUFFIX_NAME) ;
        try {
            file.transferTo(new File(folder,newName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //更新地址
        StringBuffer urlNewValue = new StringBuffer("/sys/setting/download/app/android");
        Date nowTime = new Date();
        updateBySettingKey(SETTING_URL_KEY, urlNewValue.toString(),nowTime);
        // 更新版本号
        updateBySettingKey(SETTING_VERSION_CODE_KEY,versionCode, nowTime);
        // 更新版本名称
        updateBySettingKey(SETTING_VERSION_NAME_KEY,versionName, nowTime);
        // 更新app标题
        updateBySettingKey(SETTING_TITLE_KEY,androidTitle, nowTime);
        // 更新app描述
        updateBySettingKey(SETTING_DESC_KEY,androidDesc,nowTime);

        return R.ok();
    }

    private void updateBySettingKey(String key, String value, Date updatedTime){
        Setting setting = new Setting ();
        setting.updatePrefixInit(getUserId(), updatedTime);
        // 设置key-value
        setting.setSettingValue(value);
        setting.setSettingKey(key);
        this.settingService.updateBySettingKey(setting);
    }

    /**
     * android app安装包 下载
     * lxf
     * 2020/08/31
     * @param response
     */
    @ApiOperation(value = "app安装包下载", notes="下载app安装包")
    @RequestMapping(value = "/download/app/android",method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response)  {
        List<Setting> settingList = this.settingService.selectListBySettingKey(SETTING_ANDROID_KEY);
        String versionCode = null ;
        String versionName = null ;
        String androidTitle = null;
        for(Setting setting : settingList ){
            if(SETTING_VERSION_CODE_KEY.equals(setting.getSettingKey())){
                versionCode = setting.getSettingValue();
                continue;
            }
            if(SETTING_VERSION_NAME_KEY.equals(setting.getSettingKey())){
                versionName = setting.getSettingValue();
                continue;
            }
            if(SETTING_TITLE_KEY.equals(setting.getSettingKey())){
                androidTitle = setting.getSettingValue();
                continue;
            }
        }
        String fileName = androidTitle.concat("_").concat(versionCode).concat(".").concat(SUFFIX_NAME) ;
        StringBuffer uploadPath = new StringBuffer(uploadDir);
        if(!uploadDir.endsWith("/")){
            uploadPath.append("/");
        }
        uploadPath.append(APP_FILE_PATH_NAME);
        String filePath = uploadPath.append(fileName).toString();
        InputStream inputStream = null ;
        OutputStream outputStream = null;
        File sourceFile = new File(filePath);
        if(!sourceFile.exists()){
            noFileResponse(response,"此版本还没有上线!");
        }
        String outPutFileName = androidTitle.concat("_").concat(versionName).concat(".").concat(SUFFIX_NAME) ;
        try {
            /** 将文件名称进行编码 */
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(outPutFileName, "UTF-8"));
            response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            response.setHeader("Last-Modified", new Date().toString());
            response.setContentType("application/octet-stream; charset=UTF-8");
            /** 读取服务器端模板文件*/
            inputStream = new FileInputStream(filePath);
            /** 用来显示下载进度 百分比 */
            response.setContentLength(inputStream.available());
            /** 将流中内容写出去 .*/
            outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (UnsupportedEncodingException e) {
            response.reset();
            noFileResponse(response,"此版本还没有上线!");
        } catch (IllegalStateException e){

        } catch (IOException e) {
            if(!response.isCommitted()){
                response.reset();
                noFileResponse(response,"此版本还没有上线！");
            }
        } finally {
            if( null != inputStream && null != outputStream ){
                try {
                    inputStream.close();
                    if(!response.isCommitted()){
                        outputStream.flush();
                        outputStream.close();
                        response.flushBuffer();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void noFileResponse(HttpServletResponse response, String msg){
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter responseWriter = null ;
        try {
            responseWriter = response.getWriter();
            responseWriter.print(msg);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            responseWriter.flush();
            responseWriter.close();
        }
    }

    private User getUser(){
        return (User) ShiroUtils.getSubject().getPrincipal() ;
    }

    private Long getUserId(){
        return getUser().getUserId();
    }

}
