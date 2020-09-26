package com.hngf.web.common.aspect;

import com.hngf.common.exception.ScyfException;
import com.hngf.common.utils.HttpContextUtils;
import com.hngf.common.utils.RedisUtils;
import com.hngf.common.validator.Assert;
import com.hngf.web.common.annotation.RepeatSubmit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 重复提交切面类
 */
@Aspect
@Component
public class RepeatSubmitAspect {

    @Autowired
    private RedisUtils redisUtils;

    @Pointcut("@annotation(repeatSubmit)")
    public void repeatSubmitPointCut(RepeatSubmit repeatSubmit) {

    }


    @Around("repeatSubmitPointCut(repeatSubmit)")
    public Object around(ProceedingJoinPoint point,RepeatSubmit repeatSubmit) throws Throwable {
        int lockSeconds = repeatSubmit.lockTime();

        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        Assert.isNull(request,"request是空的");

        //获取sessionID
        String sessionId = request.getRequestedSessionId();
        //sessionId + RequestURI组装成redis的key
        String key = sessionId + "-" + request.getRequestURI();

        // 如果缓存中有这个url视为重复提交
        if (null == redisUtils.get(key)) {
            Object o = point.proceed();
            //将当前key放入redis中，value为空，时间为 lockSeconds
            redisUtils.set(key,null,lockSeconds);
            return o;
        }else{
            throw new ScyfException("请勿重复提交");
        }
    }
}
