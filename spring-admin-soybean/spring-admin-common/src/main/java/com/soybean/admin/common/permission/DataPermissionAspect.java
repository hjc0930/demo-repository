package com.soybean.admin.common.permission;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * 数据权限切面
 */
@Slf4j
@Component
public class DataPermissionAspect implements InnerInterceptor {

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter,
                           RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        // 在SQL执行前添加数据权限过滤条件
        String sql = boundSql.getSql();

        // 检查是否需要数据权限过滤
        if (needDataPermission(ms.getId())) {
            String permissionSql = buildDataPermissionSql();
            if (permissionSql != null && !permissionSql.isEmpty()) {
                // 将数据权限条件添加到SQL的WHERE子句中
                sql = addDataPermissionToSql(sql, permissionSql);
                // 修改SQL
                // 注意：这里需要使用反射或MyBatis-Plus提供的API来修改SQL
            }
        }
    }

    /**
     * 判断是否需要数据权限过滤
     */
    private boolean needDataPermission(String statementId) {
        // 检查当前用户是否有数据权限
        // 这里需要从SecurityContext中获取当前用户信息
        return true;
    }

    /**
     * 构建数据权限SQL
     */
    private String buildDataPermissionSql() {
        // 根据当前用户的数据权限范围构建SQL
        // 1. 全部数据权限
        // 2. 自定义数据权限
        // 3. 本部门数据权限
        // 4. 本部门及以下数据权限
        // 5. 仅本人数据权限

        // 这里需要从用户角色和权限中获取数据权限范围
        // 返回类似：AND (u.dept_id = 1 OR u.dept_id IN (SELECT dept_id FROM sys_dept WHERE parent_id = 1))

        return ""; // 暂时返回空
    }

    /**
     * 将数据权限条件添加到SQL
     */
    private String addDataPermissionToSql(String sql, String permissionSql) {
        // 解析SQL，添加WHERE条件
        if (sql.toUpperCase().contains(" WHERE ")) {
            // 已有WHERE子句
            return sql + " AND " + permissionSql;
        } else {
            // 没有WHERE子句
            return sql + " WHERE " + permissionSql;
        }
    }
}
