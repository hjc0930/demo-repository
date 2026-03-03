package com.soybean.admin.common.annotation;

import java.lang.annotation.*;

/**
 * 权限校验注解
 * 用于标记需要特定权限才能访问的方法
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /**
     * 需要的权限，支持通配符
     * 例如：
     * - "system:user:view" 查看用户
     * - "system:user:*" 用户所有权限
     * - "*:*:*" 所有权限
     */
    String value();

    /**
     * 权限逻辑关系，true表示需要满足所有权限（AND），false表示满足任一即可（OR）
     * 默认为false（OR逻辑）
     */
    boolean requireAll() default false;
}
