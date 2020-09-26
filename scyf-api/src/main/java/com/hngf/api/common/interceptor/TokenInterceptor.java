package com.hngf.api.common.interceptor;

import com.hngf.api.common.utils.JwtUtil;
import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.Constant;
import com.hngf.service.sys.UserService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token拦截器
 * @author zhangfei
 * @date 2020-06-22
 */
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        //登录接口直接放行，其他接口验证token
        if ("/api/sys/login".equals(uri) || uri.indexOf("api/sys/login") > 0) {
            return true;
        }
        //app版本信息接口直接放行，其他接口验证token
        if ("api/sys/setting/getAndroidLastestVersion".equals(uri) || uri.indexOf("api/sys/setting/getAndroidLastestVersion") > 0) {
            return true;
        }


        String token = "";
        //获取用户凭证
        try {
            token = request.getHeader(jwtUtil.getHeader());
        } catch (Exception e) {
            token = "" ;
        }

        //如果Header不存在token参数，从request中获取
        if(StringUtils.isBlank(token)){
            token = request.getParameter(jwtUtil.getHeader());
        }

        //凭证为空
        if(StringUtils.isBlank(token)){
            throw new ScyfException(jwtUtil.getHeader() + "不能为空", HttpStatus.UNAUTHORIZED.value());
        }

        Claims claims = jwtUtil.checkJWT(token);
        if(claims == null || jwtUtil.isExpired(claims.getExpiration())){
            throw new ScyfException(jwtUtil.getHeader() + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        }

        Integer userId = (Integer)claims.get("userId");
        //暂定去掉token最新比对，让同一个账号可以多台手机登录，方便测试
        //获取数据中最新的token信息
        /*UserTokenDto userTokenDto = userService.getTokenById(userId.longValue());
        if (null == userTokenDto) {
            throw new ScyfException(jwtUtil.getHeader() + "信息不存在，请重新登录", HttpStatus.NOT_FOUND.value());
        }*/
        //如果用户传入token与数据库不匹配，提示登录
        /*if (!token.equals(userTokenDto.getApiToken())) {
            throw new ScyfException(jwtUtil.getHeader() + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        }*/
        //设置loginName和userId到request里
        request.setAttribute(Constant.USER_LOGIN_NAME, claims.getSubject());
        request.setAttribute(Constant.USER_ID, userId);
        return true;
    }
}
