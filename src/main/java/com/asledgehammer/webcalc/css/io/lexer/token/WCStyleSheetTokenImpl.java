package com.asledgehammer.webcalc.css.io.lexer.token;

import com.asledgehammer.webcalc.css.io.WCFileReferenceBlock;
import lombok.NonNull;

public class WCStyleSheetTokenImpl implements WCStyleSheetToken {

    private final @NonNull WCFileReferenceBlock reference;
    private final @NonNull String raw;

    protected WCStyleSheetTokenImpl(final @NonNull WCFileReferenceBlock reference, final @NonNull String raw) {
        this.reference = reference;
        this.raw = raw;
    }

    @Override
    public @NonNull WCFileReferenceBlock getFileReference() {
        return reference;
    }

    @Override
    public @NonNull String getRaw() {
        return raw;
    }

}
