package com.presto.server.api;

import com.presto.server.support.error.CoreException;
import com.presto.server.support.error.ErrorType;
import com.presto.server.support.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ApiResponse<Void>> handleCoreException(CoreException e) {
        switch (e.getErrorType().getLogLevel()) {
            case ERROR -> log.error("CoreException : {}", e.getMessage(), e);
            case WARN -> log.warn("CoreException : {}", e.getMessage(), e);
            default -> log.info("CoreException : {}", e.getMessage(), e);
        }

        ApiResponse<Void> response = ApiResponse.error(e.getErrorType(), e.getData());
        HttpStatus status = e.getErrorType().getStatus();
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);

        ErrorType errorType = ErrorType.INTERNAL_SERVER_ERROR;
        ApiResponse<Void> response = ApiResponse.error(errorType);
        HttpStatus status = errorType.getStatus();
        return new ResponseEntity<>(response, status);
    }
}
