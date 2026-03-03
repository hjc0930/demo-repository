package com.soybean.admin.common.exception;

import com.soybean.admin.common.response.ResponseCode;
import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;
    private final String msg;

    public BusinessException(String msg) {
        super(msg);
        this.code = ResponseCode.BUSINESS_ERROR.getCode();
        this.msg = msg;
    }

    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
    }

    public BusinessException(ResponseCode responseCode, String msg) {
        super(msg);
        this.code = responseCode.getCode();
        this.msg = msg;
    }

    public BusinessException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
