package com.soybean.admin.tenant.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.soybean.admin.common.annotation.IgnoreTenant;
import com.soybean.admin.data.tenant.TenantContextHolder;
import com.soybean.admin.data.tenant.TenantHandler;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * MyBatis-Plus多租户配置
 * 自动在SQL中注入租户ID条件
 */
@Configuration
public class MybatisPlusTenantConfiguration {

    /**
     * 配置MyBatis-Plus插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(TenantHandler tenantHandler) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 添加多租户插件
        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
        tenantInterceptor.setTenantLineHandler(tenantHandler);
        interceptor.addInnerInterceptor(tenantInterceptor);

        return interceptor;
    }

    /**
     * 租户注解拦截器
     * 用于处理@IgnoreTenant注解
     */
    @Intercepts({
        @Signature(
            type = Executor.class,
            method = "update",
            args = {MappedStatement.class, Object.class}
        )
    })
    public static class IgnoreTenantInterceptor implements Interceptor {

        @Override
        public Object intercept(Invocation invocation) throws Throwable {
            // 检查当前方法是否有@IgnoreTenant注解
            if (shouldIgnoreTenant(invocation)) {
                // 临时设置忽略租户过滤
                boolean originalIgnore = TenantContextHolder.isIgnoreTenant();
                try {
                    TenantContextHolder.setIgnoreTenant(true);
                    return invocation.proceed();
                } finally {
                    TenantContextHolder.setIgnoreTenant(originalIgnore);
                }
            }
            return invocation.proceed();
        }

        private boolean shouldIgnoreTenant(Invocation invocation) {
            MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
            String id = ms.getId();

            try {
                String className = id.substring(0, id.lastIndexOf('.'));
                String methodName = id.substring(id.lastIndexOf('.') + 1);

                Class<?> clazz = Class.forName(className);
                Method[] methods = clazz.getMethods();

                for (Method method : methods) {
                    if (method.getName().equals(methodName)) {
                        if (method.isAnnotationPresent(IgnoreTenant.class) ||
                            clazz.isAnnotationPresent(IgnoreTenant.class)) {
                            return true;
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                // 忽略
            }

            return false;
        }

        @Override
        public Object plugin(Object target) {
            return Plugin.wrap(target, this);
        }

        @Override
        public void setProperties(Properties properties) {
        }
    }
}
