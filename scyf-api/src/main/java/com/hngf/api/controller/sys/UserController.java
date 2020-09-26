package com.hngf.api.controller.sys;

import com.hngf.api.common.annotation.RepeatSubmit;
import com.hngf.api.common.utils.ShiroUtils;
import com.hngf.api.controller.BaseController;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.entity.sys.User;
import com.hngf.service.sys.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/api/sys/user")
@Api(value = "用户管理", tags = {"用户管理"})
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 查询用户详情
     */
    @GetMapping("/info")
    @ApiOperation(value="查询用户详情",response = User.class)
    public R info(){
        return R.ok().put("data", getUser());
    }

    @PostMapping("/password")
    @ApiOperation(value="修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "旧密码", paramType = "form", required = true, dataType = "string"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", paramType = "form", required = true, dataType = "string")
    })
    @RepeatSubmit()
    public R password(String password, String newPassword){
        Assert.isBlank(password, "旧密码不为能空");
        Assert.isBlank(newPassword, "新密码不为能空");

        //原密码
        password = ShiroUtils.sha256(password, getUser().getSalt());
        //新密码
        newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt());

        //更新密码
        boolean flag = userService.updatePassword(getUserId(), password, newPassword);
        if(!flag){
            return R.error("原密码不正确");
        }
        return R.ok();
    }
}
