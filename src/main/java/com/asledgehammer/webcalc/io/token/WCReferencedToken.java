package com.asledgehammer.webcalc.io.token;

import lombok.Getter;
import lombok.NonNull;

public class WCReferencedToken extends WCToken {

    @Getter private final @NonNull WCReferenceRange reference;

    protected WCReferencedToken(final @NonNull WCReferenceRange reference, final @NonNull String contents) {
        super(contents);
        this.reference = reference;
    }
    
    @Override
    public String toString() {
        return getReference() + " :: \"" + getContents().replaceAll("\r\n", "\\\\r\\\\n").replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n").replaceAll("\t", "\\\\t") + "\"";
    }
}
