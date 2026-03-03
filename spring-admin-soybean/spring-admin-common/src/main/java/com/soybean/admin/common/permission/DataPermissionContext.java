package com.soybean.admin.common.permission;

import lombok.Data;

import java.util.Set;

/**
 * 数据权限上下文
 */
@Data
public class DataPermissionContext {

    /**
     * 当前用户ID
     */
    private Long userId;

    /**
     * 当前用户名
     */
    private String username;

    /**
     * 当前用户部门ID
     */
    private Long deptId;

    /**
     * 数据权限范围：1-全部，2-自定义，3-本部门，4-本部门及以下，5-仅本人
     */
    private String dataScope;

    /**
     * 自定义部门ID列表（当dataScope=2时使用）
     */
    private Set<Long> deptIds;

    private static final ThreadLocal<DataPermissionContext> CONTEXT = new ThreadLocal<>();

    public static DataPermissionContext get() {
        return CONTEXT.get();
    }

    public static void set(DataPermissionContext context) {
        CONTEXT.set(context);
    }

    public static void clear() {
        CONTEXT.remove();
    }

    /**
     * 构建数据权限SQL
     */
    public String buildDataPermissionSql(String userAlias, String deptAlias) {
        if (dataScope == null || "1".equals(dataScope)) {
            // 全部数据权限，不加过滤条件
            return "";
        }

        StringBuilder sql = new StringBuilder();

        switch (dataScope) {
            case "2":
                // 自定义数据权限
                if (deptIds != null && !deptIds.isEmpty()) {
                    sql.append(" AND ").append(deptAlias).append(".dept_id IN (");
                    for (Long deptId : deptIds) {
                        sql.append(deptId).append(",");
                    }
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(")");
                }
                break;

            case "3":
                // 本部门数据权限
                sql.append(" AND ").append(deptAlias).append(".dept_id = ").append(deptId);
                break;

            case "4":
                // 本部门及以下数据权限
                sql.append(" AND ").append(deptAlias).append(".dept_id IN (")
                    .append("SELECT dept_id FROM sys_dept WHERE FIND_IN_SET(")
                    .append(deptId).append(", ancestors))");
                break;

            case "5":
                // 仅本人数据权限
                sql.append(" AND ").append(userAlias).append(".user_id = ").append(userId);
                break;

            default:
                break;
        }

        return sql.toString();
    }
}
