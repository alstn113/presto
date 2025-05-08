package com.presto.server.auth.application;

public interface TokenProvider {

    String createToken(Long memberId);

    Long getMemberId(String token);
}
