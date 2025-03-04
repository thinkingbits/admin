package com.jerry.ff.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = HttpStatus.BAD_REQUEST.value();
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = HttpStatus.BAD_REQUEST.value();
    }

}