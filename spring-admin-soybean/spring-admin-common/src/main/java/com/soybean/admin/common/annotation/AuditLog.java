package com.soybean.admin.common.annotation;

import java.lang.annotation.*;

/**
 * 审计日志注解
 * 用于标记需要记录审计日志的方法
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {

    /**
     * 模块名称
     */
    String module() default "";

    /**
     * 操作类型（如：create, update, delete, query等）
     */
    String action() default "";

    /**
     * 目标实体类型
     */
    String targetType() default "";

    /**
     * 是否保存请求数据
     */
    boolean saveRequest() default true;

    /**
     * 是否保存响应数据
     */
    boolean saveResponse() default false;

    /**
     * 描述信息
     */
    String description() default "";
}
