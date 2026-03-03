package com.soybean.admin.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据接口
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<T> rows;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(List<T> rows, long total) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRows(rows);
        pageResult.setTotal(total);
        return pageResult;
    }

    /**
     * 创建分页结果（带分页参数）
     */
    public static <T> PageResult<T> of(List<T> rows, long total, Integer pageNum, Integer pageSize) {
        PageResult<T> pageResult = of(rows, total);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        if (pageSize != null && pageSize > 0) {
            pageResult.setPages((int) Math.ceil((double) total / pageSize));
        }
        return pageResult;
    }
}
