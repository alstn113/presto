package com.presto.server.auth.infra.exception;

public class TokenExpiredException extends RuntimeException {

    private static final String MESSAGE = "토큰이 만료되었습니다.";

    public TokenExpiredException() {
        super(MESSAGE);
    }

    public TokenExpiredException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
