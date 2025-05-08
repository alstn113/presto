package com.presto.server.security;

import java.util.Objects;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthentication extends AbstractAuthenticationToken {

    private final Long memberId;

    public JwtAuthentication(Long memberId) {
        super(null);
        this.memberId = memberId;
        setAuthenticated(true);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), memberId);
    }

    @Override
    public Object getPrincipal() {
        return memberId;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
