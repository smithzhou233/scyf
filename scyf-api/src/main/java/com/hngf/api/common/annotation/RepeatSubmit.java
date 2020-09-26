package com.hngf.api.common.annotation;

import java.lang.annotation.*;

/**
 * 防止重复提交注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {

    /**
     * 设置请求锁定时间,单位(秒)
     * @return
     */
    int lockTime() default 5;
}
