package com.presto.server.infra.security;

public record Accessor(String id) {

    private static final String GUEST_ID = "GUEST";
    public static final Accessor GUEST = new Accessor(GUEST_ID);

    public boolean isGuest() {
        return GUEST_ID.equals(id);
    }
}
