package com.eun.triple.error;

import com.eun.triple.constant.ErrorCode;
import com.eun.triple.dto.TripleErrorResponse;
import com.eun.triple.exception.TripleException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(annotations = RestController.class)
public class TripleExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> tripleException(TripleException e, WebRequest request) {
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.isClientSideError() ?
            HttpStatus.BAD_REQUEST :
            HttpStatus.INTERNAL_SERVER_ERROR;

        return super.handleExceptionInternal(
            e,
            TripleErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(e)),
            HttpHeaders.EMPTY,
            status,
            request
        );
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return super.handleExceptionInternal(
            e,
            TripleErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(e)),
            HttpHeaders.EMPTY,
            status,
            request
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorCode errorCode = status.is4xxClientError() ?
            ErrorCode.SPRING_BAD_REQUEST :
            ErrorCode.SPRING_INTERNAL_ERROR;

        return super.handleExceptionInternal(
            ex,
            TripleErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(ex)),
            headers,
            status,
            request
        );
    }

}
