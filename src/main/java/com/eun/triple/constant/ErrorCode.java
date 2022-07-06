package com.eun.triple.constant;

import java.util.Optional;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    OK(0, ErrorCategory.SUCCESS, "Ok"),

    BAD_REQUEST(10000, ErrorCategory.CLIENT_SIDE_ERROR, "bad request"),
    SPRING_BAD_REQUEST(10001, ErrorCategory.CLIENT_SIDE_ERROR, "Spring-detected bad request"),

    INTERNAL_ERROR(20000, ErrorCategory.SERVER_SIDE_ERROR, "internal error"),
    SPRING_INTERNAL_ERROR(20001, ErrorCategory.SERVER_SIDE_ERROR, "Spring-detected internal error")
    ;

    private final Integer code;
    private final ErrorCategory errorCategory;
    private final String message;


    public String getMessage(Exception e) {
        return getMessage(e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(getMessage());
    }

    public boolean isClientSideError() {
        return this.getErrorCategory() == ErrorCategory.CLIENT_SIDE_ERROR;
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", name(), this.getCode());
    }


    public enum ErrorCategory {
        SUCCESS, CLIENT_SIDE_ERROR, SERVER_SIDE_ERROR
    }

}
