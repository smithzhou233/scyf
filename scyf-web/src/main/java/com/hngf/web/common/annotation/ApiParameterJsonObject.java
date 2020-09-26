package com.hngf.web.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @ClassName: ApiReturnJson
 * @Description: 返回对象的定义 Map (描述这个类的作用)
 * @author lxf
 * @date 2020年09月04日 下午2:56:33
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParameterJsonObject {
    ApiJsonProperty[] value(); //对象属性值
    String name();  //对象名称
}
