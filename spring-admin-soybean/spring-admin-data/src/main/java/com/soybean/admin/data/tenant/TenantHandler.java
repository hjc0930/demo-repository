package com.soybean.admin.data.tenant;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.stereotype.Component;

/**
 * MyBatis-Plus 多租户处理器
 */
@Component
public class TenantHandler implements com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler {

    private static final String TENANT_FIELD = "tenant_id";

    /**
     * 获取租户ID
     */
    @Override
    public Expression getTenantId() {
        String tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) {
            return new StringValue("000000"); // 默认租户
        }
        return new StringValue(tenantId);
    }

    /**
     * 获取租户字段名
     */
    @Override
    public String getTenantIdColumn() {
        return TENANT_FIELD;
    }

    /**
     * 判断是否忽略该表
     */
    @Override
    public boolean ignoreTable(String tableName) {
        // 不需要租户隔离的表
        return tableName.startsWith("sys_tenant_") ||
            tableName.equals("sys_tenant") ||
            tableName.equals("sys_tenant_package") ||
            tableName.equals("sys_client") ||
            tableName.equals("gen_template_group") ||
            tableName.equals("sys_system_config") ||
            tableName.equals("sys_sms_channel") ||
            tableName.equals("sys_mail_account") ||
            tableName.equals("sys_notify_template") ||
            tableName.equals("sys_job_log") ||
            tableName.equals("sys_logininfor") ||
            tableName.equals("sys_oper_log") ||
            tableName.equals("sys_audit_log");
    }
}
