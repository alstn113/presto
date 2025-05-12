package com.presto.server.domain;

public record CursorPageInfo(
        boolean hasNext,
        boolean hasPrev
) {
}
