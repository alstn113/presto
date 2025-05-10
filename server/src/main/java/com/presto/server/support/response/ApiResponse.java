package com.presto.server.support.response;

import com.presto.server.support.error.ErrorMessage;
import com.presto.server.support.error.ErrorType;
import java.time.Instant;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final ResultType result;
    private final T data;
    private final ErrorMessage error;
    private final Instant timestamp;

    public static ApiResponse<?> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static ApiResponse<?> error(ErrorType error) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(error));
    }

    public static ApiResponse<?> error(ErrorType error, Object errorData) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(error, errorData));
    }

    private ApiResponse(ResultType result, T data, ErrorMessage error) {
        this.result = result;
        this.data = data;
        this.error = error;
        this.timestamp = Instant.now();
    }
}