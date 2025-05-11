package com.presto.server.application.auth;

public interface TokenProvider {

    String createToken(String memberId);

    String getMemberId(String token);
}
