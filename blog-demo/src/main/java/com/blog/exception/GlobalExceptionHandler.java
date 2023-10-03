package com.blog.exception;

import com.blog.common.Code;
import com.blog.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * If throw serviceException, call this method
     * @param serviceException
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public Result handle(ServiceException serviceException) {
        return Result.error(serviceException.getCode(), serviceException.getMessage());
    }

    /**
     * Order exception
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e, HttpServletResponse response){
        return Result.error(Code.ERROR.getValue(), e.getMessage());
    }
}
