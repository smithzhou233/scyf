package com.hngf.web.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiJsonProperty {
    /**
     * json对象key
     * @return
     */
    String key();
    /**
     * json对象key的名称
     * @return
     */
    String value() default "";

    String defaultValue() default "";

    String example() default "";

    String type() default "string";  //支持string 和 int 、long

    String description() default "";

    /**
     * 是否允许有多个，若true就表明是个list集合
     * @return
     */
    boolean allowMultiple() default false;

    /**
     * 是否必填
     * @return
     */
    boolean required() default false;
}
