package com.hngf.api.common.annotation;

import java.lang.annotation.*;

/**
 * 数据过滤
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFilter {

    String value() default "";

    /**  组织ID所在表的别名 */
    String tableAliasGroup() default "";

    /**  用户ID所在表的别名 */
    String tableAliasUser()  default "";

    /**  true：没有本部门数据权限，也能查询本人数据 */
    boolean user() default true;

    /**  true：拥有子部门数据权限 */
    boolean subGroup() default false;

    /**  组织ID */
    String groupId() default "group_id";

    /**  用户ID */
    String userId()  default "user_id";
}
