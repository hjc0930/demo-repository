package com.soybean.admin.common.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * 审计日志切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    /**
     * 环绕通知，记录审计日志
     */
    @Around("@annotation(com.soybean.admin.common.annotation.AuditLog)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();

        ServletRequestAttributes attributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return point.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        com.soybean.admin.common.annotation.AuditLog auditLog = getAnnotation(point, com.soybean.admin.common.annotation.AuditLog.class);

        // 构建审计日志信息
        AuditLogInfo logInfo = new AuditLogInfo();
        logInfo.setModule(auditLog != null ? auditLog.module() : "");
        logInfo.setAction(getAction(point));
        logInfo.setTargetType(getTargetType(point));
        logInfo.setTargetId(getTargetId(point));
        logInfo.setIp(getIpAddr(request));
        logInfo.setUrl(request.getRequestURI());
        logInfo.setMethod(request.getMethod());
        logInfo.setRequestParams(getRequestParams(request));
        logInfo.setStartTime(LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(startTime), java.time.ZoneId.systemDefault()));
        logInfo.setStatus("0");

        Object result = null;
        Exception exception = null;
        try {
            result = point.proceed();
            logInfo.setStatus("0");

            // 记录响应数据
            if (auditLog != null && auditLog.saveResponse()) {
                logInfo.setResponse(serializeResult(result));
            }
        } catch (Exception e) {
            exception = e;
            logInfo.setStatus("1");
            logInfo.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            logInfo.setDuration(System.currentTimeMillis() - startTime);

            // 异步保存审计日志
            saveAuditLog(logInfo);
        }

        return result;
    }

    /**
     * 保存审计日志
     */
    @Async
    public void saveAuditLog(AuditLogInfo logInfo) {
        log.info("审计日志: {}", logInfo);
        // TODO: 保存到数据库
    }

    /**
     * 获取IP地址
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取请求参数
     */
    private String getRequestParams(HttpServletRequest request) {
        String params = request.getQueryString();
        if (params == null || params.isEmpty()) {
            return request.getRequestURI();
        }
        return params;
    }

    /**
     * 获取操作名称
     */
    private String getAction(ProceedingJoinPoint point) {
        String methodName = point.getSignature().getName();
        return methodName;
    }

    /**
     * 获取目标类型
     */
    private String getTargetType(ProceedingJoinPoint point) {
        String className = point.getTarget().getClass().getSimpleName();
        return className;
    }

    /**
     * 获取目标ID
     */
    private String getTargetId(ProceedingJoinPoint point) {
        Object[] args = point.getArgs();
        if (args != null && args.length > 0 && args[0] != null) {
            return args[0].toString();
        }
        return "";
    }

    /**
     * 序列化结果
     */
    private String serializeResult(Object result) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writeValueAsString(result);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取注解
     */
    private <T extends java.lang.annotation.Annotation> T getAnnotation(ProceedingJoinPoint point, Class<T> annotationClass) {
        T annotation = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }
        return point.getTarget().getClass().getAnnotation(annotationClass);
    }

    @lombok.Data
    public static class AuditLogInfo {
        private String module;
        private String action;
        private String targetType;
        private String targetId;
        private String oldValue;
        private String newValue;
        private String ip;
        private String userAgent;
        private String requestId;
        private String url;
        private String method;
        private String requestParams;
        private String response;
        private String errorMsg;
        private Long duration;
        private String status;
        private LocalDateTime startTime;
    }
}
