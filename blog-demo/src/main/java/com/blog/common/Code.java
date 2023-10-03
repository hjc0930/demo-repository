package com.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public enum Code {
    SUCCESS(200, "Success"),
    PARAMEXCEPTION(400, "Param exception"),
    NOTACCESS(401, "Not access"),
    NOTFUND(404, "Not fund"),
    ERROR(500, "Error");

    private int value;
    private String message;

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
