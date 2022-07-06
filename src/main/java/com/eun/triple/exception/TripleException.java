package com.eun.triple.exception;

import com.eun.triple.constant.ErrorCode;
import lombok.Getter;

@Getter
public class TripleException extends RuntimeException {

    private final ErrorCode errorCode;

    public TripleException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public TripleException(ErrorCode errorCode, String message) {
        super(errorCode.getMessage(message));
        this.errorCode = errorCode;
    }

    public TripleException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode.getMessage(message), cause);
        this.errorCode = errorCode;
    }

}
