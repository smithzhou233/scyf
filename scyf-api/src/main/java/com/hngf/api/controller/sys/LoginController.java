package com.hngf.api.controller.sys;

import com.hngf.api.common.utils.JwtUtil;
import com.hngf.api.common.utils.ShiroUtils;
import com.hngf.common.utils.DateUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.Assert;
import com.hngf.dto.sys.UserTokenDto;
import com.hngf.entity.sys.User;
import com.hngf.service.sys.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@Api(value = "系统登录",tags = {"系统登录"})
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/sys/login")
    @ApiOperation(value="登录",notes = "返回参数：data(用户信息),token,expire(token有效时长，单位秒)")
    public R login(String username, String password) {
        Assert.isBlank(username,"用户名不能为空");
        Assert.isBlank(password,"密码不能为空");
        User user = userService.getByLoginName(username);
        if(null!=user){
            //sha256加密
            String shapwd =ShiroUtils.sha256(password, user.getSalt());
            if(!shapwd.equals(user.getPassword())){
                return R.error("用户名或密码错误！");
            }

            if (user.getUserStatus() == 1) {
                return R.error("用户"+username+"已被锁定，请联系管理员！");
            }

            //根据用户信息生成token
            String token = jwtUtil.generateToken(user);
            if (StringUtils.isBlank(token)) {
                return R.error("生成token失败！");
            }
            Date currentDate = new Date();
            UserTokenDto userToken = new UserTokenDto();
            userToken.setUserId(user.getUserId());
            userToken.setApiToken(token);
            userToken.setApiUpdateTime(currentDate);
            userToken.setApiExpireTime(DateUtils.addDateSeconds(currentDate,(int) jwtUtil.getExpire()));
            //保存token信息
            userService.saveToken(userToken);

            user.setPassword("");
            user.setSalt("");
            //将token和expire(过期时间)放入返回参数中
            return R.ok().put("data",user).put(jwtUtil.getHeader(), token).put("expire", jwtUtil.getExpire());
        }else{
            return R.error("该用户不存在！");
        }
    }
}
