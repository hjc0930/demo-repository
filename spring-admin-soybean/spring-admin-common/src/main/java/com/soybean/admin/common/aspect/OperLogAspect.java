package com.soybean.admin.common.aspect;

import com.soybean.admin.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * 操作日志记录切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperLogAspect {

    /**
     * 记录操作日志
     */
    @Async
    public void recordLog(OperLogInfo logInfo) {
        // TODO: 异步保存到数据库
        log.info("操作日志: {}", logInfo);
    }

    /**
     * 环绕通知
     */
    @Around("@annotation(com.soybean.admin.common.annotation.Log)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();

        // 获取RequestAttributes
        ServletRequestAttributes attributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return point.proceed();
        }

        HttpServletRequest request = attributes.getRequest();

        // 获取当前登录用户
        String username = getCurrentUsername();

        // 获取注解信息
        com.soybean.admin.common.annotation.Log log = getAnnotation(point, com.soybean.admin.common.annotation.Log.class);

        // 构建操作日志信息
        OperLogInfo logInfo = new OperLogInfo();
        logInfo.setStatus("0");
        logInfo.setOperTime(LocalDateTime.now().toString());
        logInfo.setOperIp(getIpAddr(request));
        logInfo.setOperName(username);
        logInfo.setCostTime(System.currentTimeMillis() - beginTime);

        if (log != null) {
            logInfo.setTitle(log.title());
            logInfo.setBusinessType(log.businessType().ordinal());
            logInfo.setOperatorType(log.operatorType().ordinal());
            logInfo.setDeptName(getDeptName(request));
            logInfo.setOperUrl(request.getRequestURI());
            logInfo.setRequestMethod(request.getMethod());

            if (log.isSaveRequestData()) {
                logInfo.setOperParam(getRequestParams(request));
            }
        }

        // 执行方法
        Object result = null;
        Exception exception = null;
        try {
            result = point.proceed();
            logInfo.setStatus("0");
        } catch (Exception e) {
            exception = e;
            logInfo.setStatus("1");
            logInfo.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            // 记录操作日志
            if (log != null && isLogNeedRecord(log)) {
                // 异步记录
                recordLog(logInfo);
            }
        }

        return result;
    }

    /**
     * 是否需要记录日志
     */
    private boolean isLogNeedRecord(com.soybean.admin.common.annotation.Log log) {
        return log != null && (
            log.operatorType().ordinal() > 0 ||
            log.businessType().ordinal() > 0 ||
            log.isSaveRequestData() ||
            log.isSaveResponseData()
        );
    }

    /**
     * 获取当前登录用户名
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "anonymous";
    }

    /**
     * 获取请求参数
     */
    private String getRequestParams(HttpServletRequest request) {
        String params = request.getQueryString();
        if (StringUtils.isEmpty(params)) {
            return request.getRequestURI();
        }
        return params;
    }

    /**
     * 获取IP地址
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取部门名称
     */
    private String getDeptName(HttpServletRequest request) {
        // TODO: 从用户信息中获取部门名称
        return "";
    }

    /**
     * 获取注解
     */
    private <T extends java.lang.annotation.Annotation> T getAnnotation(ProceedingJoinPoint point, Class<T> annotationClass) {
        // 尝试从方法获取
        T annotation = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }

        // 从类获取
        Class<?> targetClass = point.getTarget().getClass();
        return targetClass.getAnnotation(annotationClass);
    }

    /**
     * 操作日志信息
     */
    @lombok.Data
    public static class OperLogInfo {
        private String title;
        private Integer businessType;
        private Integer operatorType;
        private String operName;
        private String deptName;
        private String operUrl;
        private String requestMethod;
        private String operParam;
        private String jsonResult;
        String errorMsg;
        private String operIp;
        private String operTime;
        private String status;
        private Long costTime;
    }
}
