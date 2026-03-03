package com.soybean.admin.generator.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.PageResult;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.GenTable;
import com.soybean.admin.generator.dto.GenTableDTO;
import com.soybean.admin.generator.service.GenTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 代码生成控制器
 */
@RestController
@RequestMapping("/api/tool/gen")
@RequiredArgsConstructor
public class GenController {

    private final GenTableService genTableService;

    /**
     * 分页查询数据库表列表
     */
    @GetMapping("/page")
    @RequirePermission("tool:gen:list")
    public Result<PageResult<GenTable>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String tableName,
            @RequestParam(required = false) String tableComment) {

        GenTableDTO query = new GenTableDTO();
        query.setTableName(tableName);
        query.setTableComment(tableComment);

        Page<GenTable> page = new Page<>(pageNum, pageSize);
        IPage<GenTable> result = genTableService.selectTablePage(page, query);

        return Result.success(PageResult.of(result.getRecords(), result.getTotal()));
    }

    /**
     * 查询数据库表列表
     */
    @GetMapping("/list")
    @RequirePermission("tool:gen:list")
    public Result<List<GenTable>> list(
            @RequestParam(required = false) String tableName,
            @RequestParam(required = false) String tableComment) {

        GenTableDTO query = new GenTableDTO();
        query.setTableName(tableName);
        query.setTableComment(tableComment);

        List<GenTable> list = genTableService.selectTableList(query);
        return Result.success(list);
    }

    /**
     * 根据表ID查询表信息
     */
    @GetMapping("/{tableId}")
    @RequirePermission("tool:gen:query")
    public Result<GenTable> getTable(@PathVariable Long tableId) {
        GenTable table = genTableService.getById(tableId);
        return Result.success(table);
    }

    /**
     * 导入表结构
     */
    @PostMapping("/importTable")
    @RequirePermission("tool:gen:import")
    public Result<Void> importTable(@RequestBody List<String> tableNames) {
        genTableService.importTable(tableNames);
        return Result.success();
    }

    /**
     * 修改代码生成配置
     */
    @PutMapping
    @RequirePermission("tool:gen:edit")
    public Result<Void> edit(@RequestBody GenTable genTable) {
        genTableService.updateGenTable(genTable);
        return Result.success();
    }

    /**
     * 删除表
     */
    @DeleteMapping("/{tableIds}")
    @RequirePermission("tool:gen:remove")
    public Result<Void> remove(@PathVariable Long[] tableIds) {
        for (Long tableId : tableIds) {
            genTableService.deleteGenTable(tableId);
        }
        return Result.success();
    }

    /**
     * 预览代码
     */
    @GetMapping("/preview/{tableId}")
    @RequirePermission("tool:gen:preview")
    public Result<Map<String, String>> preview(@PathVariable Long tableId) {
        Map<String, String> files = genTableService.previewCode(tableId);
        return Result.success(files);
    }

    /**
     * 生成代码（下载）
     */
    @GetMapping("/download/{tableId}")
    @RequirePermission("tool:gen:code")
    public void download(@PathVariable Long tableId, HttpServletResponse response) throws IOException {
        byte[] data = genTableService.downloadCode(tableId);

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=code.zip");
        response.setContentLength(data.length);

        try (OutputStream os = response.getOutputStream()) {
            os.write(data);
            os.flush();
        }
    }

    /**
     * 生成代码到项目目录
     */
    @GetMapping("/genCode/{tableId}")
    @RequirePermission("tool:gen:code")
    public Result<Void> genCode(@PathVariable Long tableId) {
        genTableService.generateCode(tableId);
        return Result.success();
    }

    /**
     * 同步表结构
     */
    @PostMapping("/sync/{tableId}")
    @RequirePermission("tool:gen:edit")
    public Result<Void> sync(@PathVariable Long tableId) {
        genTableService.syncTable(tableId);
        return Result.success();
    }
}
