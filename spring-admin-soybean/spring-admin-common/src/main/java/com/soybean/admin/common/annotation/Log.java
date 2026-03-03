package com.soybean.admin.common.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 用于标记需要记录操作日志的方法
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块标题
     */
    String title() default "";

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别（0其它 1后台用户 2手机端用户 3定时任务）
     */
    OperatorType operatorType() default OperatorType.OTHER;

    /**
     * 是否保存请求参数
     */
    boolean isSaveRequestData() default false;

    /**
     * 是否保存响应参数
     */
    boolean isSaveResponseData() default false;

    /**
     * 业务类型枚举
     */
    enum BusinessType {
        OTHER,      // 其它
        INSERT,     // 新增
        UPDATE,     // 修改
        DELETE,     // 删除
        GRANT,      // 授权
        EXPORT,     // 导出
        IMPORT,     // 导入
        FORCE,      // 强退
        GENCODE,    // 生成代码
        CLEAN,      // 清空数据
    }

    /**
     * 操作人类别枚举
     */
    enum OperatorType {
        OTHER,      // 其它
        BACKEND,    // 后台用户
        MOBILE,     // 手机端用户
        SCHEDULED,   // 定时任务
    }
}
