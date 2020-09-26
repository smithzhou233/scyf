package com.hngf.api.controller.sys;

import com.hngf.api.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hngf.entity.sys.UserSite;
import com.hngf.service.sys.UserSiteService;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 手机APP位置坐标
 *
 * @author hngf
 * @email 
 * @date 2020-07-09 14:03:19
 */
@RestController
@RequestMapping("/api/sys/usersite")
@Api(value = "手机APP位置坐标管理", tags = {"手机APP位置坐标管理"})
public class UserSiteController extends BaseController {

    @Autowired
    private UserSiteService userSiteService;

    /**
     * 信息
     */
    @GetMapping("/info")
    @ApiOperation(value="查询坐标信息",response = UserSite.class)
    @ApiImplicitParam(name = "phoneCode", value = "手机型号", required = true, paramType = "query", dataType = "string")
    public R info(@RequestParam(required=false) String phoneCode){
        Map<String, Object> params = new HashMap<>();
        params.put("userId", getUserId());
        params.put("companyId", getCompanyId());
        params.put("groupId", getGroupId());
        params.put("phoneCode", phoneCode);
        return R.ok().put("data", userSiteService.getSite(params));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value="保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "longitude", value = "经度", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "phoneCode", value = "手机型号", required = true, paramType = "query", dataType = "string")
    })
    public R save(@RequestBody UserSite userSite){

        ValidatorUtils.validateEntity(userSite);

        userSite.setUserId(getUserId());
        userSite.setCompanyId(getCompanyId());
        userSite.setGroupId(getGroupId());

        int res = userSiteService.save(userSite);

        return res > 0 ? R.ok() : R.error();
    }
}
