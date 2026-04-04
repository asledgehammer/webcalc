package com.asledgehammer.webcalc.css.io.lexer.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.NonNull;

public class WCWhiteSpaceTokenImpl extends WCReferencedTokenImpl implements WCWhiteSpaceToken {
    public WCWhiteSpaceTokenImpl(@NonNull WCReferenceRange reference, @NonNull String contents) {
        super(reference, contents, false);
    }

    @Override
    public String toString() {
        return "<whitespace>";
    }
}
