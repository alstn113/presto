package com.presto.server.domain;

import java.util.List;

public record CursorPaginatedResponse<T>(
        CursorPageInfo pageInfo,
        List<T> content
) {
}
