package com.presto.server.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthentication extends AbstractAuthenticationToken {

    private final Accessor accessor;

    public JwtAuthentication(Accessor accessor) {
        super(null);
        this.accessor = accessor;
        setAuthenticated(true);
    }

    @Override
    public Accessor getPrincipal() {
        return accessor;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
