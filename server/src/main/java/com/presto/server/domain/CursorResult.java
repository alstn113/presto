package com.presto.server.domain;

import java.util.List;

// nextCursor 와 prevCursor 는 data 의 첫번째, 마지막 요소의 id 를 의미한다.
public record CursorResult<T>(
        List<T> content,
        String prevCursor,
        String nextCursor,
        String lastReadMessageId
) {
}
