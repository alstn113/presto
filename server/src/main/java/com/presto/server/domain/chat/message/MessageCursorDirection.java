package com.presto.server.domain.chat.message;

public enum MessageCursorDirection {

    PREV, NEXT;

    public boolean isPrev() {
        return this == PREV;
    }

    public boolean isNext() {
        return this == NEXT;
    }
}
