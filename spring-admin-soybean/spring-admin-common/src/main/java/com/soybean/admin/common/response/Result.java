package com.soybean.admin.common.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 统一响应结果类
 *
 * @description 企业级标准响应封装，支持泛型类型安全
 * @example
 * // 成功响应
 * return Result.ok(data);
 * return Result.ok(data, "创建成功");
 *
 * // 失败响应
 * return Result.fail(ResponseCode.USER_NOT_FOUND);
 * return Result.fail(ResponseCode.PARAM_INVALID, "用户名不能为空");
 *
 * // 分页响应
 * return Result.page(rows, total, pageNum, pageSize);
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    /**
     * 响应码
     */
    private String code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 请求ID，用于追踪
     */
    private String requestId;

    /**
     * 时间戳
     */
    private String timestamp;

    public Result() {
    }

    public Result(String code, String msg, T data, String requestId, String timestamp) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.requestId = requestId;
        this.timestamp = timestamp;
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return ResponseCode.SUCCESS.getCode().equals(this.code);
    }

    /**
     * 成功响应
     *
     * @param data 响应数据
     * @param msg  响应消息（可选）
     */
    public static <T> Result<T> ok(T data, String msg) {
        return new Result<>(
            ResponseCode.SUCCESS.getCode(),
            msg != null ? msg : ResponseCode.SUCCESS.getMsg(),
            data,
            generateRequestId(),
            getCurrentTimestamp()
        );
    }

    /**
     * 成功响应（使用默认消息）
     */
    public static <T> Result<T> ok(T data) {
        return ok(data, null);
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> ok() {
        return ok(null);
    }

    /**
     * 成功响应（success方法别名，兼容性方法）
     *
     * @param data 响应数据
     */
    public static <T> Result<T> success(T data) {
        return ok(data);
    }

    /**
     * 成功响应（无数据，success方法别名）
     */
    public static <T> Result<T> success() {
        return ok(null);
    }

    /**
     * 失败响应
     *
     * @param responseCode 响应码枚举
     * @param msg          错误消息（可选，不传则使用默认消息）
     * @param data         额外数据（可选）
     */
    public static <T> Result<T> fail(ResponseCode responseCode, String msg, T data) {
        return new Result<>(
            responseCode.getCode(),
            msg != null ? msg : responseCode.getMsg(),
            data,
            generateRequestId(),
            getCurrentTimestamp()
        );
    }

    /**
     * 失败响应（使用默认消息）
     */
    public static <T> Result<T> fail(ResponseCode responseCode, String msg) {
        return fail(responseCode, msg, null);
    }

    /**
     * 失败响应（使用默认错误码和消息）
     */
    public static <T> Result<T> fail() {
        return fail(ResponseCode.BUSINESS_ERROR, null, null);
    }

    /**
     * 失败响应（指定消息）
     */
    public static Result fail(String code, String msg) {
        ResponseCode responseCode = ResponseCode.getResponseCodeByCode(code);
        return fail(responseCode, msg, null);
    }
    /**
     * 失败响应（指定消息）
     */
    public static <T> Result<T> fail(String msg) {
        return fail(ResponseCode.BUSINESS_ERROR, msg, null);
    }

    /**
     * 失败响应（指定响应码和消息）
     * @param responseCode 响应码枚举
     */
    public static <T> Result<T> fail(ResponseCode responseCode) {
        return new Result<>(
                responseCode.getCode(),
                responseCode.getMsg(),
            null,
            generateRequestId(),
            getCurrentTimestamp()
        );
    }

    /**
     * 失败响应（指定响应码、消息和数据）
     *
     * @param data 额外数据
     */
    public static <T> Result<T> fail(ResponseCode responseCode, T data) {
        return new Result<>(
            responseCode.getCode(),
            responseCode.getMsg(),
            data,
            generateRequestId(),
            getCurrentTimestamp()
        );
    }

    /**
     * 条件响应
     *
     * @param condition 条件
     * @param successData 成功时的数据
     * @param failCode   失败时的错误码
     * @param failMsg    失败时的消息
     */
    public static <T> Result<T> when(boolean condition, T successData, ResponseCode failCode, String failMsg) {
        return condition ? ok(successData) : fail(failCode, failMsg);
    }

    /**
     * 分页响应
     *
     * @param rows    数据列表
     * @param total   总记录数
     * @param pageNum 当前页码（可选）
     * @param pageSize 每页条数（可选）
     */
    public static <T> Result<PageResult<T>> page(java.util.List<T> rows, long total, Integer pageNum, Integer pageSize) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRows(rows);
        pageResult.setTotal(total);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        if (pageSize != null && pageSize > 0) {
            pageResult.setPages((int) Math.ceil((double) total / pageSize));
        }
        return ok(pageResult);
    }

    /**
     * 分页响应（无分页参数）
     */
    public static <T> Result<PageResult<T>> page(java.util.List<T> rows, long total) {
        return page(rows, total, null, null);
    }

    /**
     * 分页响应（long参数版本）
     */
    public static <T> Result<PageResult<T>> page(java.util.List<T> rows, long total, long pageNum, long pageSize) {
        return page(rows, total, (int) pageNum, (int) pageSize);
    }

    /**
     * 生成请求ID
     */
    private static String generateRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取当前时间戳
     */
    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }

    /**
     * 设置请求ID（用于传递链路追踪ID）
     */
    public void setRequestIdIfAbsent(String requestId) {
        if (this.requestId == null) {
            this.requestId = requestId;
        }
    }
}
