package com.soybean.admin.common.response;

import java.util.Arrays;

/**
 * 统一响应码枚举
 * 企业级项目的标准响应结构
 * 所有响应码统一为4位数字，不足位补零
 */
public enum ResponseCode {
    // ========== 成功 ==========
    SUCCESS("2000", "操作成功"),

    // ========== 客户端错误 0400-0499 ==========
    BAD_REQUEST("0400", "请求参数错误"),
    UNAUTHORIZED("0401", "未授权访问"),
    FORBIDDEN("0403", "禁止访问"),
    NOT_FOUND("0404", "资源不存在"),
    METHOD_NOT_ALLOWED("0405", "请求方法不允许"),
    REQUEST_TIMEOUT("0408", "请求超时"),
    CONFLICT("0409", "数据冲突"),
    GONE("0410", "资源已被删除"),
    UNPROCESSABLE_ENTITY("0422", "请求数据无法处理"),
    TOO_MANY_REQUESTS("0429", "请求过于频繁"),

    // ========== 服务端错误 0500-0599 ==========
    INTERNAL_SERVER_ERROR("0500", "服务器内部错误"),
    NOT_IMPLEMENTED("0501", "功能未实现"),
    BAD_GATEWAY("0502", "网关错误"),
    SERVICE_UNAVAILABLE("0503", "服务暂不可用"),
    GATEWAY_TIMEOUT("0504", "网关超时"),

    // ========== 业务错误码 1000+ ==========
    // 通用业务错误 (1000-1999)
    BUSINESS_ERROR("1000", "业务处理失败"),
    PARAM_INVALID("1001", "参数验证失败"),
    DATA_NOT_FOUND("1002", "数据不存在"),
    DATA_ALREADY_EXISTS("1003", "数据已存在"),
    DATA_IN_USE("1004", "数据正在使用中"),
    OPERATION_FAILED("1005", "操作执行失败"),
    DATA_CANNOT_DELETE("1006", "数据无法删除"),

    // 认证授权错误 (2000-2999)
    TOKEN_INVALID("2001", "无效的令牌"),
    TOKEN_EXPIRED("2002", "令牌已过期"),
    TOKEN_REFRESH_EXPIRED("2003", "刷新令牌已过期"),
    ACCOUNT_DISABLED("2004", "账户已禁用"),
    ACCOUNT_LOCKED("2005", "账户已锁定"),
    PASSWORD_ERROR("2006", "密码错误"),
    CAPTCHA_ERROR("2007", "验证码错误"),
    PERMISSION_DENIED("2008", "权限不足"),

    // 用户相关错误 (3000-3999)
    USER_NOT_FOUND("3001", "用户不存在"),
    USER_ALREADY_EXISTS("3002", "用户已存在"),
    USER_DISABLED("3003", "用户已禁用"),
    PASSWORD_WEAK("3004", "密码强度不足"),
    OLD_PASSWORD_ERROR("3005", "原密码错误"),

    // 租户相关错误 (4000-4999)
    TENANT_NOT_FOUND("4001", "租户不存在"),
    TENANT_DISABLED("4002", "租户已禁用"),
    TENANT_EXPIRED("4003", "租户已过期"),
    TENANT_QUOTA_EXCEEDED("4004", "租户配额已超限"),

    // 文件相关错误 (5000-5999)
    FILE_NOT_FOUND("5001", "文件不存在"),
    FILE_TYPE_NOT_ALLOWED("5002", "文件类型不允许"),
    FILE_SIZE_EXCEEDED("5003", "文件大小超限"),
    FILE_UPLOAD_FAILED("5004", "文件上传失败"),

    // 第三方服务错误 (6000-6999)
    EXTERNAL_SERVICE_ERROR("6001", "外部服务调用失败"),
    REDIS_ERROR("6002", "Redis服务异常"),
    DATABASE_ERROR("6003", "数据库服务异常");

    private final String code;
    private final String msg;

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ResponseCode getResponseCodeByCode(String code) {
        return  Arrays.stream(ResponseCode.values()).filter(i -> i.getCode().equals(code)).findFirst().orElse(null);
    }
}
