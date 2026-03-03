package com.soybean.admin.common.permission;

import java.lang.annotation.*;

/**
 * 数据权限注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

    /**
     * 用户表的别名
     */
    String userAlias() default "u";

    /**
     * 部门表的别名
     */
    String deptAlias() default "d";

    /**
     * 权限字符（根据用户角色动态获取）
     */
    String permission() default "";
}
