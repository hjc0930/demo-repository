package com.soybean.admin.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.GenTable;
import com.soybean.admin.generator.dto.GenTableDTO;

import java.util.List;
import java.util.Map;

/**
 * 代码生成表服务接口
 */
public interface GenTableService extends IService<GenTable> {

    /**
     * 分页查询数据库表列表
     */
    IPage<GenTable> selectTablePage(Page<GenTable> page, GenTableDTO query);

    /**
     * 查询数据库表列表
     */
    List<GenTable> selectTableList(GenTableDTO query);

    /**
     * 导入数据库表结构
     */
    void importTable(List<String> tableNames);

    /**
     * 预览代码
     */
    Map<String, String> previewCode(Long tableId);

    /**
     * 生成代码（下载）
     */
    byte[] downloadCode(Long tableId);

    /**
     * 生成代码到项目目录
     */
    void generateCode(Long tableId);

    /**
     * 更新表信息
     */
    boolean updateGenTable(GenTable genTable);

    /**
     * 删除表
     */
    boolean deleteGenTable(Long tableId);

    /**
     * 同步表结构
     */
    void syncTable(Long tableId);
}
