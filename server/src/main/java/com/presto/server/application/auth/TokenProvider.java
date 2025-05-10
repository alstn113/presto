package com.presto.server.application.auth;

public interface TokenProvider {

    String createToken(Long memberId);

    Long getMemberId(String token);
}
