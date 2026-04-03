package com.asledgehammer.webcalc.css.io.lexer.token;

import com.asledgehammer.webcalc.css.io.WCFileReferenceBlock;
import lombok.NonNull;

public class WCWhiteSpaceTokenImpl extends WCStyleSheetTokenImpl implements WCWhiteSpaceToken {
    public WCWhiteSpaceTokenImpl(@NonNull WCFileReferenceBlock reference, @NonNull String raw) {
        super(reference, raw);
    }

    @Override
    public String toString() {
        return "<whitespace>";
    }
}
