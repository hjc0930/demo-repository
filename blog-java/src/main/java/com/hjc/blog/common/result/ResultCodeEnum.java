package com.hjc.blog.common.result;

import lombok.Getter;

/**
 * 返回码枚举
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS("20000", "操作成功"),
    ERROR("50000", "操作失败"),

    // 用户相关 1000-1999
    USER_NOT_EXIST("10001", "用户不存在"),
    USER_ACCOUNT_EXIST("10002", "账号已存在"),
    USER_PASSWORD_ERROR("10003", "密码错误"),
    USER_ACCOUNT_LOCKED("10004", "账号已被锁定"),
    USER_LOGIN_EXPIRED("10005", "登录已过期"),
    USER_EMAIL_EXIST("10006", "邮箱已被注册"),

    // 参数相关 2000-2999
    PARAM_ERROR("20001", "参数错误"),
    PARAM_MISSING("20002", "缺少必要参数"),

    // 业务相关 3000-3999
    DATA_NOT_EXIST("30001", "数据不存在"),
    DATA_EXIST("30002", "数据已存在"),

    // 权限相关 4000-4999
    UNAUTHORIZED("40001", "未认证"),
    FORBIDDEN("40003", "权限不足");

    private final String code;
    private final String message;

    ResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
