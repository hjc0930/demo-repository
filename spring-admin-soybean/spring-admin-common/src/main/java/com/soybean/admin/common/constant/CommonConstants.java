package com.soybean.admin.common.constant;

/**
 * 通用常量
 */
public class CommonConstants {

    /**
     * 成功标记（业务状态）
     */
    public static final String SUCCESS = "0";

    /**
     * 失败标记（业务状态）
     */
    public static final String FAIL = "1";

    /**
     * 成功响应码（4位数字）
     */
    public static final String SUCCESS_CODE = "0000";

    /**
     * 超级管理员租户ID
     */
    public static final String SUPER_TENANT_ID = "000000";

    /**
     * 超级管理员角色标识
     */
    public static final String SUPER_ADMIN_ROLE_KEY = "admin";

    /**
     * 正常状态
     */
    public static final String NORMAL = "0";

    /**
     * 异常状态
     */
    public static final String ABNORMAL = "1";

    /**
     * 删除标记
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETED = "1";

    /**
     * JWT相关
     */
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";

    /**
     * Redis相关
     */
    public static final String LOGIN_TOKEN_KEY = "login:token:";
    public static final String TOKEN_BLACKLIST_KEY = "token:blacklist:";
    public static final String CAPTCHA_CODE_KEY = "captcha:code:";
    public static final String RATE_LIMIT_KEY = "rate:limit:";

    /**
     * 编码格式
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 通用请求头
     */
    public static final String HEADER_TENANT_ID = "X-Tenant-Id";
    public static final String HEADER_REQUEST_ID = "X-Request-Id";
}
