package com.soybean.admin.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 * 用于标记需要分布式锁的方法
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

    /**
     * 锁的key前缀
     */
    String prefix() default "lock:";

    /**
     * 锁的key，支持SpEL表达式
     */
    String key() default "";

    /**
     * 锁的名称，默认为方法全限定名
     */
    String name() default "";

    /**
     * 获取锁的最大等待时间
     */
    long waitTime() default 10;

    /**
     * 获取锁的最大等待时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 锁的持有时间
     */
    long leaseTime() default -1L;

    /**
     * 是否公平锁
     */
    boolean isFair() default false;

    /**
     * 提示信息
     */
    String message() default "操作频繁，请稍后再试";
}
