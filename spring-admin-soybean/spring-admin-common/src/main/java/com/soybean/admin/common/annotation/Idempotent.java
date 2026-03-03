package com.soybean.admin.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 幂等性注解
 * 用于标记需要幂等性校验的接口
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    /**
     * 幂等性key的前缀
     */
    String prefix() default "idempotent:";

    /**
     * 幂等性key，支持SpEL表达式
     * 例如: "#user.id", "#request.getRequestURI()"
     */
    String key() default "";

    /**
     * 等待时间（单位：秒）
     */
    long timeout() default 5;

    /**
     * 时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 操作完成后是否删除key
     */
    boolean delKey() default false;

    /**
     * 提示信息
     */
    String message() default "操作频繁，请稍后再试";
}
