package com.soybean.admin.tenant.interceptor;

import com.soybean.admin.common.annotation.IgnoreTenant;
import com.soybean.admin.common.constant.CommonConstants;
import com.soybean.admin.data.tenant.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 租户拦截器
 * 从请求头中提取租户ID并设置到上下文中
 */
@Slf4j
@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                            @NonNull HttpServletResponse response,
                            @NonNull Object handler) {
        // 检查是否有@IgnoreTenant注解
        if (handler instanceof HandlerMethod handlerMethod) {
            IgnoreTenant ignoreTenant = handlerMethod.getMethodAnnotation(IgnoreTenant.class);
            if (ignoreTenant == null) {
                ignoreTenant = handlerMethod.getBeanType().getAnnotation(IgnoreTenant.class);
            }

            if (ignoreTenant != null) {
                // 设置默认租户但忽略过滤
                TenantContextHolder.setTenantId(CommonConstants.SUPER_TENANT_ID);
                TenantContextHolder.setIgnoreTenant(true);
                return true;
            }
        }

        // 从请求头中获取租户ID
        String tenantId = request.getHeader(CommonConstants.HEADER_TENANT_ID);

        // 如果没有租户ID，使用默认租户
        if (tenantId == null || tenantId.isEmpty()) {
            tenantId = CommonConstants.SUPER_TENANT_ID;
        }

        // 设置租户ID到上下文
        TenantContextHolder.setTenantId(tenantId);

        // 设置请求ID
        String requestId = request.getHeader(CommonConstants.HEADER_REQUEST_ID);
        if (requestId != null && !requestId.isEmpty()) {
            TenantContextHolder.setRequestId(requestId);
        }

        log.debug("设置租户上下文: tenantId={}", tenantId);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                               @NonNull HttpServletResponse response,
                               @NonNull Object handler,
                               Exception ex) {
        // 清空租户上下文
        TenantContextHolder.clear();
    }
}
