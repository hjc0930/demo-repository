package com.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回的包装类
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return new Result<T>(Code.SUCCESS.getValue(), Code.SUCCESS.getMessage(), null);
    }
    public static <T> Result<T> success(T data) {
        return new Result<T>(Code.SUCCESS.getValue(), Code.SUCCESS.getMessage(), data);
    }
    public static <T> Result<T> success(int code,T data) {
        return new Result<T>(code, Code.SUCCESS.getMessage(), data);
    }
    public static <T> Result<T> success(int code, String msg, T data) {
        return new Result<T>(code, msg, data);
    }

    public static Result error() {
        return new Result(Code.ERROR.getValue(), Code.ERROR.getMessage(), null);
    }

    public static Result error(String msg) {
        return new Result(Code.ERROR.getValue(), msg, null);
    }

    public static Result error(int code, String msg) {
        return new Result(code, msg, null);
    }
}
