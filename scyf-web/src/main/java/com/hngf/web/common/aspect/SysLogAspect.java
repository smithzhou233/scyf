package com.hngf.web.common.aspect;

import com.google.gson.Gson;
import com.hngf.common.utils.HttpContextUtils;
import com.hngf.common.utils.IPUtils;
import com.hngf.entity.sys.Log;
import com.hngf.entity.sys.User;
import com.hngf.service.sys.LogService;
import com.hngf.web.common.annotation.SysLog;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 系统日志切面
 */
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private LogService logService;

    @Pointcut("@annotation(com.hngf.web.common.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();

        //执行方法
        Object result = point.proceed();

        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(point, time);

        return result;
    }

    //保存日志到数据库
    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Log sysLog = new Log();
        SysLog annotation = method.getAnnotation(SysLog.class);
        if(annotation != null){
            //注解上的描述
            sysLog.setSysLogDesc(annotation.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setSysLogMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        Gson gson = new Gson();
        try{
            //保存请求的参数1
            String params = gson.toJson(args[0]);
            sysLog.setSysLogParam1(params);
        }catch (Exception e){

        }
        try{
            //保存请求的参数2
            String params = gson.toJson(args[1]);
            sysLog.setSysLogParam2(params);
        }catch (Exception e){

        }

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLog.setSysLogIp(IPUtils.getIpAddr(request));

        try{
            //保存用户ID
            Long userId = ((User) SecurityUtils.getSubject().getPrincipal()).getUserId();
            sysLog.setSysLogUserid(userId);
        }catch (Exception e){

        }

        sysLog.setSysLogExetime(time);
        sysLog.setSysLogTime(new Date());
        //保存系统日志
        logService.save(sysLog);
    }
}
