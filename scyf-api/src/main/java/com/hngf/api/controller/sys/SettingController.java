package com.hngf.api.controller.sys;

import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.entity.sys.Setting;
import com.hngf.service.sys.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@RestController
@RequestMapping("api/sys/setting")
@Api(value="app版本升级",tags = {"app版本升级"})
public class SettingController {
    @Autowired
    private SettingService settingService;
    @Value("${scyf.appDownBasePath}")
    private String appDownBasePath;


    /**
     * 列表
     */
    @ApiIgnore
    @ApiOperation(value="app配置信息",response = Setting.class)
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token",value = "登录之后返回的token值",dataType = "string", required = true)
    })
    public R list(@RequestParam Map<String, Object> params){
        params.put("ids","1,2,3");
        PageUtils page = settingService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @ApiOperation(value="App版本信息,android版")
    @RequestMapping(value="/getAndroidLastestVersion",method = RequestMethod.GET)
    public R androidLastestVersionInfo(){
        Map<String,String> versionMap = settingService.getAndroidLastestVersion();
        StringBuffer versionUrl = new StringBuffer(appDownBasePath);
        if(appDownBasePath.endsWith("/")){
            versionUrl.deleteCharAt(versionUrl.length()-1);
        }
        String temUrl = versionMap.get("versionUrl");
        if(!temUrl.startsWith("/")){
            versionUrl.append("/");
        }
        versionUrl.append(temUrl);
        versionMap.put("versionUrl",versionUrl.toString());
        return R.ok().put("data", versionMap);
    }

}
