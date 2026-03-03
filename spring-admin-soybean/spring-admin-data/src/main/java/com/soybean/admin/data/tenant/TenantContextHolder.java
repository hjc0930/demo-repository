package com.soybean.admin.data.tenant;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 租户上下文持有者
 * 使用TransmittableThreadLocal在异步操作中传递租户信息
 *
 * 提供租户隔离的核心上下文管理功能，支持：
 * - 租户ID的设置和获取
 * - 忽略租户过滤的控制
 * - 临时切换租户执行操作
 * - 请求链路追踪
 */
public class TenantContextHolder {

    /**
     * 超级管理员租户ID
     */
    public static final String SUPER_TENANT_ID = "000000";

    /**
     * 租户上下文
     */
    private static final ThreadLocal<TenantContext> CONTEXT = new TransmittableThreadLocal<>();

    /**
     * 租户上下文数据
     */
    public static class TenantContext {
        /**
         * 租户ID
         */
        private String tenantId;

        /**
         * 是否忽略租户过滤
         */
        private boolean ignoreTenant;

        /**
         * 请求ID（用于链路追踪）
         */
        private String requestId;

        public TenantContext(String tenantId) {
            this.tenantId = tenantId;
            this.ignoreTenant = false;
        }

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public boolean isIgnoreTenant() {
            return ignoreTenant;
        }

        public void setIgnoreTenant(boolean ignoreTenant) {
            this.ignoreTenant = ignoreTenant;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }
    }

    /**
     * 设置租户ID
     */
    public static void setTenantId(String tenantId) {
        TenantContext context = CONTEXT.get();
        if (context == null) {
            context = new TenantContext(tenantId);
            CONTEXT.set(context);
        } else {
            context.setTenantId(tenantId);
        }
    }

    /**
     * 获取租户ID
     */
    public static String getTenantId() {
        TenantContext context = CONTEXT.get();
        return context != null ? context.getTenantId() : null;
    }

    /**
     * 设置忽略租户过滤
     */
    public static void setIgnoreTenant(boolean ignoreTenant) {
        TenantContext context = CONTEXT.get();
        if (context != null) {
            context.setIgnoreTenant(ignoreTenant);
        }
    }

    /**
     * 检查是否忽略租户过滤
     */
    public static boolean isIgnoreTenant() {
        TenantContext context = CONTEXT.get();
        return context != null && context.isIgnoreTenant();
    }

    /**
     * 设置请求ID
     */
    public static void setRequestId(String requestId) {
        TenantContext context = CONTEXT.get();
        if (context != null) {
            context.setRequestId(requestId);
        }
    }

    /**
     * 获取请求ID
     */
    public static String getRequestId() {
        TenantContext context = CONTEXT.get();
        return context != null ? context.getRequestId() : null;
    }

    /**
     * 获取当前上下文
     */
    public static TenantContext getContext() {
        return CONTEXT.get();
    }

    /**
     * 清空上下文
     */
    public static void clear() {
        CONTEXT.remove();
    }

    /**
     * 判断是否为超级管理员租户
     */
    public static boolean isSuperTenant() {
        return SUPER_TENANT_ID.equals(getTenantId());
    }

    /**
     * 判断当前是否在租户上下文中
     */
    public static boolean hasContext() {
        return CONTEXT.get() != null;
    }

    /**
     * 判断是否应该应用租户过滤
     *
     * 以下情况不应用租户过滤：
     * - 没有租户上下文
     * - 设置了忽略租户
     * - 是超级租户
     */
    public static boolean shouldApplyTenantFilter() {
        if (!hasContext()) {
            return false;
        }
        if (isIgnoreTenant()) {
            return false;
        }
        if (isSuperTenant()) {
            return false;
        }
        return true;
    }

    /**
     * 临时切换租户执行操作
     */
    public static <T> T runWithTenant(String tenantId, java.util.function.Supplier<T> action) {
        String originalTenantId = getTenantId();
        boolean originalIgnoreTenant = isIgnoreTenant();

        try {
            setTenantId(tenantId);
            setIgnoreTenant(false);
            return action.get();
        } finally {
            if (originalTenantId != null) {
                setTenantId(originalTenantId);
                setIgnoreTenant(originalIgnoreTenant);
            } else {
                clear();
            }
        }
    }

    /**
     * 临时忽略租户过滤执行操作
     */
    public static <T> T runIgnoringTenant(java.util.function.Supplier<T> action) {
        String originalTenantId = getTenantId();
        boolean originalIgnoreTenant = isIgnoreTenant();

        try {
            setIgnoreTenant(true);
            return action.get();
        } finally {
            if (originalTenantId != null) {
                setTenantId(originalTenantId);
                setIgnoreTenant(originalIgnoreTenant);
            } else {
                clear();
            }
        }
    }
}
