package com.soybean.admin.generator.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.data.entity.GenTable;
import com.soybean.admin.data.entity.GenTableColumn;
import com.soybean.admin.data.mapper.GenTableColumnMapper;
import com.soybean.admin.data.mapper.GenTableMapper;
import com.soybean.admin.generator.dto.GenTableDTO;
import com.soybean.admin.generator.service.GenTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成表服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements GenTableService {

    private final GenTableMapper genTableMapper;
    private final GenTableColumnMapper genTableColumnMapper;
    private final JdbcTemplate jdbcTemplate;

    @Value("${generator.base.package:com.soybean.admin}")
    private String basePackage;

    @Value("${generator.author:soybean}")
    private String author;

    @Override
    public IPage<GenTable> selectTablePage(Page<GenTable> page, GenTableDTO query) {
        // 先从数据库查询所有表
        List<Map<String, Object>> tables = listDbTables(query);

        // 设置总数
        page.setTotal(tables.size());

        // 转换为GenTable对象并分页
        List<GenTable> genTables = new ArrayList<>();
        int start = (int) ((page.getCurrent() - 1) * page.getSize());
        int end = Math.min(start + (int) page.getSize(), tables.size());

        for (int i = start; i < end; i++) {
            Map<String, Object> tableMap = tables.get(i);
            GenTable genTable = mapToGenTable(tableMap);
            genTables.add(genTable);
        }

        page.setRecords(genTables);
        return page;
    }

    @Override
    public List<GenTable> selectTableList(GenTableDTO query) {
        List<Map<String, Object>> tables = listDbTables(query);
        List<GenTable> genTables = new ArrayList<>();

        for (Map<String, Object> tableMap : tables) {
            genTables.add(mapToGenTable(tableMap));
        }

        return genTables;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importTable(List<String> tableNames) {
        for (String tableName : tableNames) {
            // 查询表信息
            Map<String, Object> tableInfo = getTableInfo(tableName);
            if (tableInfo == null) {
                continue;
            }

            GenTable genTable = mapToGenTable(tableInfo);
            genTableMapper.insert(genTable);

            // 查询列信息
            List<Map<String, Object>> columns = getTableColumns(tableName);
            for (Map<String, Object> columnMap : columns) {
                GenTableColumn column = mapToGenTableColumn(columnMap, genTable.getTableId());
                genTableColumnMapper.insert(column);
            }

            log.info("Imported table: {}", tableName);
        }
    }

    @Override
    public Map<String, String> previewCode(Long tableId) {
        GenTable genTable = genTableMapper.selectById(tableId);
        if (genTable == null) {
            throw new RuntimeException("表不存在");
        }

        List<GenTableColumn> columns = genTableColumnMapper.selectList(
            new LambdaQueryWrapper<GenTableColumn>()
                .eq(GenTableColumn::getTableId, tableId)
                .orderByAsc(GenTableColumn::getSort)
        );

        Map<String, String> result = new LinkedHashMap<>();
        result.put("entity.java", generateEntityCode(genTable, columns));
        result.put("mapper.java", generateMapperCode(genTable));
        result.put("mapper.xml", generateMapperXml(genTable, columns));
        result.put("service.java", generateServiceCode(genTable));
        result.put("serviceImpl.java", generateServiceImplCode(genTable));
        result.put("controller.java", generateControllerCode(genTable));

        return result;
    }

    @Override
    public byte[] downloadCode(Long tableId) {
        Map<String, String> codes = previewCode(tableId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            for (Map.Entry<String, String> entry : codes.entrySet()) {
                ZipEntry zipEntry = new ZipEntry(entry.getKey());
                zos.putNextEntry(zipEntry);
                zos.write(entry.getValue().getBytes());
                zos.closeEntry();
            }

            zos.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Failed to generate zip file", e);
            throw new RuntimeException("生成代码失败", e);
        }
    }

    @Override
    public void generateCode(Long tableId) {
        // TODO: 实现将代码写入项目目录的逻辑
        log.info("Generate code for table: {}", tableId);
    }

    @Override
    public boolean updateGenTable(GenTable genTable) {
        return genTableMapper.updateById(genTable) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGenTable(Long tableId) {
        genTableColumnMapper.delete(new LambdaQueryWrapper<GenTableColumn>()
            .eq(GenTableColumn::getTableId, tableId));
        return genTableMapper.deleteById(tableId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncTable(Long tableId) {
        GenTable genTable = genTableMapper.selectById(tableId);
        if (genTable == null) {
            throw new RuntimeException("表不存在");
        }

        // 同步表结构
        List<Map<String, Object>> columns = getTableColumns(genTable.getTableName());

        // 删除旧列信息
        genTableColumnMapper.delete(new LambdaQueryWrapper<GenTableColumn>()
            .eq(GenTableColumn::getTableId, tableId));

        // 插入新列信息
        for (Map<String, Object> columnMap : columns) {
            GenTableColumn column = mapToGenTableColumn(columnMap, tableId);
            genTableColumnMapper.insert(column);
        }

        log.info("Synced table: {}", genTable.getTableName());
    }

    /**
     * 从数据库查询表列表
     */
    private List<Map<String, Object>> listDbTables(GenTableDTO query) {
        String sql = "SELECT table_name, table_comment FROM information_schema.tables " +
            "WHERE table_schema = 'public' AND table_type = 'BASE TABLE'";

        if (StrUtil.isNotBlank(query.getTableName())) {
            sql += " AND table_name LIKE '%" + query.getTableName() + "%'";
        }
        if (StrUtil.isNotBlank(query.getTableComment())) {
            sql += " AND table_comment LIKE '%" + query.getTableComment() + "%'";
        }
        if (StrUtil.isNotBlank(query.getKeyword())) {
            sql += " AND (table_name LIKE '%" + query.getKeyword() + "%' " +
                "OR table_comment LIKE '%" + query.getKeyword() + "%')";
        }

        sql += " ORDER BY table_name";

        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 获取表信息
     */
    private Map<String, Object> getTableInfo(String tableName) {
        String sql = "SELECT table_name, table_comment FROM information_schema.tables " +
            "WHERE table_schema = 'public' AND table_name = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, tableName);
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * 获取表的列信息
     */
    private List<Map<String, Object>> getTableColumns(String tableName) {
        String sql = "SELECT " +
            "column_name, data_type, column_default, is_nullable, column_comment, " +
            "character_maximum_length, numeric_precision, numeric_scale, ordinal_position " +
            "FROM information_schema.columns " +
            "WHERE table_schema = 'public' AND table_name = ? " +
            "ORDER BY ordinal_position";

        return jdbcTemplate.queryForList(sql, tableName);
    }

    /**
     * Map转换为GenTable
     */
    private GenTable mapToGenTable(Map<String, Object> map) {
        GenTable genTable = new GenTable();
        genTable.setTableName((String) map.get("table_name"));
        genTable.setTableComment((String) map.get("table_comment"));

        // 生成类名
        String tableName = genTable.getTableName();
        String className = toClassName(tableName);
        genTable.setClassName(className);

        // 生成其他配置
        genTable.setPackageName(basePackage);
        genTable.setModuleName(toModuleName(tableName));
        genTable.setBusinessName(toBusinessName(tableName));
        genTable.setFunctionName(genTable.getTableComment());
        genTable.setFunctionAuthor(author);
        genTable.setGenType("0"); // 0-压缩包 1-自定义路径
        genTable.setGenPath("/");
        genTable.setTplCategory("crud"); // crud/tree

        return genTable;
    }

    /**
     * Map转换为GenTableColumn
     */
    private GenTableColumn mapToGenTableColumn(Map<String, Object> map, Long tableId) {
        GenTableColumn column = new GenTableColumn();
        column.setTableId(tableId);
        column.setColumnName((String) map.get("column_name"));
        column.setColumnComment((String) map.get("column_comment"));
        column.setDataType((String) map.get("data_type"));
        column.setNullable("YES".equals(map.get("is_nullable")) ? "1" : "0");

        // 设置Java类型
        column.setJavaType(toJavaType((String) map.get("data_type")));

        // 设置Java字段名
        column.setJavaField(toCamelCase((String) map.get("column_name")));

        // 设置是否主键
        // 简单判断：字段名包含id或为第一个字段
        String columnName = column.getColumnName();
        column.setIsPk(columnName.toLowerCase().contains("id") ? "1" : "0");

        // 设置是否自增
        column.setIsIncrement("nextval".equals(String.valueOf(map.get("column_default"))) ? "1" : "0");

        // 设置是否必填
        column.setIsRequired("0".equals(column.getNullable()) ? "1" : "0");

        // 设置默认显示配置
        column.setIsInsert("1");
        column.setIsEdit("1");
        column.setIsList("1");
        column.setIsQuery("0");
        column.setQueryType("EQ");
        column.setHtmlType("input");
        column.setDictType("");

        // 设置排序
        column.setSort(((Number) map.getOrDefault("ordinal_position", 0)).intValue());

        return column;
    }

    /**
     * 表名转类名
     */
    private String toClassName(String tableName) {
        String[] parts = tableName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                sb.append(Character.toUpperCase(part.charAt(0)))
                  .append(part.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 表名转模块名
     */
    private String toModuleName(String tableName) {
        String[] parts = tableName.split("_");
        return parts.length > 1 ? parts[0] : "system";
    }

    /**
     * 表名转业务名
     */
    private String toBusinessName(String tableName) {
        String[] parts = tableName.split("_");
        return parts.length > 1 ? parts[1] : tableName.replace("sys_", "");
    }

    /**
     * 字段名转驼峰
     */
    private String toCamelCase(String columnName) {
        String[] parts = columnName.split("_");
        StringBuilder sb = new StringBuilder(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                sb.append(Character.toUpperCase(parts[i].charAt(0)))
                  .append(parts[i].substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 数据库类型转Java类型
     */
    private String toJavaType(String dbType) {
        if (dbType == null) {
            return "String";
        }

        String type = dbType.toLowerCase();
        if (type.contains("int")) {
            return type.contains("bigint") ? "Long" : "Integer";
        } else if (type.contains("decimal") || type.contains("numeric")) {
            return "BigDecimal";
        } else if (type.contains("char") || type.contains("text") || type.contains("varchar")) {
            return "String";
        } else if (type.contains("date") || type.contains("time")) {
            return "LocalDateTime";
        } else if (type.contains("bit")) {
            return "Boolean";
        } else if (type.contains("blob")) {
            return "byte[]";
        }

        return "String";
    }

    /**
     * 生成Entity代码
     */
    private String generateEntityCode(GenTable table, List<GenTableColumn> columns) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(table.getPackageName()).append(".").append(table.getModuleName()).append(".entity;\n\n");
        sb.append("import com.baomidou.mybatisplus.annotation.*;\n");
        sb.append("import lombok.Data;\n");
        sb.append("import lombok.EqualsAndHashCode;\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(table.getTableComment()).append("\n");
        sb.append(" */\n");
        sb.append("@Data\n");
        sb.append("@EqualsAndHashCode(callSuper = true)\n");
        sb.append("@TableName(\"").append(table.getTableName()).append("\")\n");
        sb.append("public class ").append(table.getClassName()).append(" extends BaseEntity {\n\n");
        sb.append("    private static final long serialVersionUID = 1L;\n\n");

        for (GenTableColumn column : columns) {
            sb.append("    /**\n");
            sb.append("     * ").append(column.getColumnComment()).append("\n");
            sb.append("     */\n");

            if ("1".equals(column.getIsPk())) {
                sb.append("    @TableId(value = \"").append(column.getColumnName()).append("\", type = IdType.AUTO)\n");
            }

            sb.append("    private ").append(column.getJavaType()).append(" ").append(column.getJavaField()).append(";\n\n");
        }

        sb.append("}\n");

        return sb.toString();
    }

    /**
     * 生成Mapper代码
     */
    private String generateMapperCode(GenTable table) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(table.getPackageName()).append(".").append(table.getModuleName()).append(".mapper;\n\n");
        sb.append("import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n");
        sb.append("import ").append(table.getPackageName()).append(".").append(table.getModuleName()).append(".entity.").append(table.getClassName()).append(";\n");
        sb.append("import org.apache.ibatis.annotations.Mapper;\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(table.getTableComment()).append(" Mapper接口\n");
        sb.append(" */\n");
        sb.append("@Mapper\n");
        sb.append("public interface ").append(table.getClassName()).append("Mapper extends BaseMapper<").append(table.getClassName()).append("> {\n");
        sb.append("}\n");

        return sb.toString();
    }

    /**
     * 生成Mapper XML代码
     */
    private String generateMapperXml(GenTable table, List<GenTableColumn> columns) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        sb.append("<mapper namespace=\"").append(table.getPackageName()).append(".").append(table.getModuleName())
          .append(".mapper.").append(table.getClassName()).append("Mapper\">\n\n");
        sb.append("    <!-- 结果映射 -->\n");
        sb.append("    <resultMap id=\"BaseResultMap\" type=\"").append(table.getPackageName()).append(".").append(table.getModuleName())
          .append(".entity.").append(table.getClassName()).append("\">\n");

        for (GenTableColumn column : columns) {
            sb.append("        <result column=\"").append(column.getColumnName()).append("\" property=\"")
              .append(column.getJavaField()).append("\" />\n");
        }

        sb.append("    </resultMap>\n\n");
        sb.append("</mapper>\n");

        return sb.toString();
    }

    /**
     * 生成Service代码
     */
    private String generateServiceCode(GenTable table) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(table.getPackageName()).append(".").append(table.getModuleName()).append(".service;\n\n");
        sb.append("import com.baomidou.mybatisplus.extension.service.IService;\n");
        sb.append("import ").append(table.getPackageName()).append(".").append(table.getModuleName()).append(".entity.").append(table.getClassName()).append(";\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(table.getTableComment()).append(" 服务接口\n");
        sb.append(" */\n");
        sb.append("public interface ").append(table.getClassName()).append("Service extends IService<").append(table.getClassName()).append("> {\n");
        sb.append("}\n");

        return sb.toString();
    }

    /**
     * 生成ServiceImpl代码
     */
    private String generateServiceImplCode(GenTable table) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(table.getPackageName()).append(".").append(table.getModuleName()).append(".service.impl;\n\n");
        sb.append("import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\n");
        sb.append("import ").append(table.getPackageName()).append(".").append(table.getModuleName()).append(".entity.").append(table.getClassName()).append(";\n");
        sb.append("import ").append(table.getPackageName()).append(".").append(table.getModuleName()).append(".mapper.").append(table.getClassName()).append("Mapper;\n");
        sb.append("import ").append(table.getPackageName()).append(".").append(table.getModuleName()).append(".service.").append(table.getClassName()).append("Service;\n");
        sb.append("import lombok.RequiredArgsConstructor;\n");
        sb.append("import org.springframework.stereotype.Service;\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(table.getTableComment()).append(" 服务实现\n");
        sb.append(" */\n");
        sb.append("@Service\n");
        sb.append("@RequiredArgsConstructor\n");
        sb.append("public class ").append(table.getClassName()).append("ServiceImpl extends ServiceImpl<").append(table.getClassName())
          .append("Mapper, ").append(table.getClassName()).append("> implements ").append(table.getClassName()).append("Service {\n");
        sb.append("}\n");

        return sb.toString();
    }

    /**
     * 生成Controller代码
     */
    private String generateControllerCode(GenTable table) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(table.getPackageName()).append(".").append(table.getModuleName()).append(".controller;\n\n");
        sb.append("import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;\n");
        sb.append("import com.baomidou.mybatisplus.core.metadata.IPage;\n");
        sb.append("import com.baomidou.mybatisplus.extension.plugins.pagination.Page;\n");
        sb.append("import com.soybean.admin.common.response.PageResult;\n");
        sb.append("import com.soybean.admin.common.response.Result;\n");
        sb.append("import ").append(table.getPackageName()).append(".").append(table.getModuleName()).append(".entity.").append(table.getClassName()).append(";\n");
        sb.append("import ").append(table.getPackageName()).append(".").append(table.getModuleName()).append(".service.").append(table.getClassName()).append("Service;\n");
        sb.append("import lombok.RequiredArgsConstructor;\n");
        sb.append("import org.springframework.web.bind.annotation.*;\n");
        sb.append("import javax.validation.Valid;\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(table.getTableComment()).append(" 控制器\n");
        sb.append(" */\n");
        sb.append("@RestController\n");
        sb.append("@RequestMapping(\"/api/").append(table.getModuleName()).append("/").append(table.getBusinessName()).append("\")\n");
        sb.append("@RequiredArgsConstructor\n");
        sb.append("public class ").append(table.getClassName()).append("Controller {\n\n");
        sb.append("    private final ").append(table.getClassName()).append("Service service;\n\n");
        sb.append("    /**\n");
        sb.append("     * 分页查询").append(table.getTableComment()).append("\n");
        sb.append("     */\n");
        sb.append("    @GetMapping(\"/page\")\n");
        sb.append("    public Result<PageResult<").append(table.getClassName()).append(">> page(\n");
        sb.append("            @RequestParam(defaultValue = \"1\") Integer pageNum,\n");
        sb.append("            @RequestParam(defaultValue = \"10\") Integer pageSize) {\n");
        sb.append("        Page<").append(table.getClassName()).append("> page = new Page<>(pageNum, pageSize);\n");
        sb.append("        IPage<").append(table.getClassName()).append("> result = service.page(page);\n");
        sb.append("        return Result.success(PageResult.of(result.getRecords(), result.getTotal()));\n");
        sb.append("    }\n\n");
        sb.append("    /**\n");
        sb.append("     * 根据ID查询").append(table.getTableComment()).append("\n");
        sb.append("     */\n");
        sb.append("    @GetMapping(\"/{id}\")\n");
        sb.append("    public Result<").append(table.getClassName()).append("> getById(@PathVariable Long id) {\n");
        sb.append("        return Result.success(service.getById(id));\n");
        sb.append("    }\n\n");
        sb.append("    /**\n");
        sb.append("     * 新增").append(table.getTableComment()).append("\n");
        sb.append("     */\n");
        sb.append("    @PostMapping\n");
        sb.append("    public Result<Void> add(@Valid @RequestBody ").append(table.getClassName()).append(" entity) {\n");
        sb.append("        service.save(entity);\n");
        sb.append("        return Result.success();\n");
        sb.append("    }\n\n");
        sb.append("    /**\n");
        sb.append("     * 修改").append(table.getTableComment()).append("\n");
        sb.append("     */\n");
        sb.append("    @PutMapping\n");
        sb.append("    public Result<Void> update(@Valid @RequestBody ").append(table.getClassName()).append(" entity) {\n");
        sb.append("        service.updateById(entity);\n");
        sb.append("        return Result.success();\n");
        sb.append("    }\n\n");
        sb.append("    /**\n");
        sb.append("     * 删除").append(table.getTableComment()).append("\n");
        sb.append("     */\n");
        sb.append("    @DeleteMapping(\"/{id}\")\n");
        sb.append("    public Result<Void> delete(@PathVariable Long id) {\n");
        sb.append("        service.removeById(id);\n");
        sb.append("        return Result.success();\n");
        sb.append("    }\n");
        sb.append("}\n");

        return sb.toString();
    }
}
