package com.asledgehammer.webcalc.css.io.lexer.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.Getter;
import lombok.NonNull;

public class WCReferencedTokenImpl implements WCReferencedToken {

    private final @NonNull WCReferenceRange reference;
    @Getter private final @NonNull String contents;
    private final boolean generic;

    protected WCReferencedTokenImpl(final @NonNull WCReferenceRange reference, final @NonNull String contents, final boolean generic) {
        this.reference = reference;
        this.contents = contents;
        this.generic = generic;
    }

    @Override
    public @NonNull WCReferenceRange getReference() {
        return reference;
    }

    @Override
    public boolean isGeneric() {
        return generic;
    }
}
