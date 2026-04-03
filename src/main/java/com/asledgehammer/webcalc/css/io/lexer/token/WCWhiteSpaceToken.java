package com.asledgehammer.webcalc.css.io.lexer.token;

import lombok.NonNull;

public interface WCWhiteSpaceToken extends WCStyleSheetToken {

    enum Type {
        TAB,
        NEW_LINE,
        SPACE;

        public static @NonNull Type fromRaw(@NonNull String raw) {
            if (raw.isEmpty()) throw new IllegalArgumentException("raw cannot be empty");
            return switch (raw) {
                case "\t" -> TAB;
                case "\f", "\n", "\r", "\r\n" -> NEW_LINE;
                case " " -> SPACE;
                default -> throw new IllegalArgumentException("Unknown WCWhiteSpaceToken.Type: \"" + raw + "\"");
            };
        }
    }

}
