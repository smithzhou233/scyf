package com.hngf.web.controller.sys;

import com.hngf.common.validator.Assert;
import com.hngf.entity.sys.Info;
import com.hngf.entity.sys.User;
import com.hngf.service.sys.InfoService;
import com.hngf.web.common.annotation.SysLog;
import com.hngf.web.common.shiro.UserRealm;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hngf.common.utils.R;
import com.hngf.web.common.shiro.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统登录
 * @author zhangfei
 * @since 2020/5/21 15:46
 */
@RestController
@Api(value = "系统登录",tags = {"系统登录"})
public class LoginController {

    @Autowired
    private UserRealm userRealm;
    @Autowired
    private InfoService infoService;

    /**
     * 登录
     */
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    @ApiOperation(value="登录")
    @SysLog("用户登录")
    public R login(String username, String password) {
        try{
            Assert.isBlank(username,"用户名不能为空");
            Assert.isBlank(password,"密码不能为空");

            Subject subject = ShiroUtils.getSubject();
            //用户登录之前，先清空之前的的授权缓存记录
            PrincipalCollection principals =  subject.getPrincipals();
            if (null != principals) {
                userRealm.clearCachedAuthorizationInfo(principals);
                userRealm.clearCachedAuthenticationInfo(principals);
            }

            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);

            User user = ShiroUtils.getUser();
            //如果当前登录用户不是超级管理员，做以下判断
            if (user.getUserId().longValue() != 1 && !user.getLoginName().equals("admin")) {
                //1，判断用户是否拥有角色
                if (null == user.getRoleId() || user.getRoleId() == 0L) {
                    return R.error("该用户没有分配角色, 没有访问权限，请联系超级管理员");
                }
                //2，判断用户是否有所属部门
                if (null == user.getGroupId() || user.getGroupId() == 0L) {
                    return R.error("该用户没有所属部门，请联系管理员");
                }
                //3，判断用户是否有所属岗位
                if (null == user.getPositionId() || user.getPositionId() == 0L) {
                    return R.error("该用户没有分配岗位信息，请联系管理员");
                }
            }
            Info info = infoService.getByCId(user.getCompanyId(),user.getUserType());
            String screen = "" ;
            if (info != null) {
                //获取大屏地址
                screen = info.getWebUrl();
               /* if (screen.indexOf("?") != -1) {
                    screen = screen + "&groupId=" + user.getGroupId();
                    screen = screen + "&companyId=" + user.getCompanyId();
                }*/
            }
            user.setScreen(screen);
            return R.ok().put("data", user);
        }catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        }catch (IncorrectCredentialsException e) {
            return R.error("账号或密码不正确");
        }catch (LockedAccountException e) {
            return R.error("账号已被锁定,请联系管理员");
        }catch (AuthenticationException e) {
            return R.error("账户验证失败");
        }
    }

    /**
     * 退出
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    @ApiOperation(value="登出")
    public R logout() {
        ShiroUtils.logout();
        return R.ok("登出成功");
    }
}
