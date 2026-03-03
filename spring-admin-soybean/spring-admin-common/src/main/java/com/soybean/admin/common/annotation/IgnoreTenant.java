package com.soybean.admin.common.annotation;

import java.lang.annotation.*;

/**
 * 忽略租户过滤注解
 * 用于标记不需要进行租户隔离的方法或类
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreTenant {
}
