package com.asledgehammer.webcalc.css;

import com.asledgehammer.webcalc.io.token.WCReference;
import org.jetbrains.annotations.Nullable;

public abstract class WCStyleElementImpl implements WCStyleElement {

    @Nullable
    private WCReference reference;

    protected WCStyleElementImpl() {
    }

    @Nullable
    @Override
    public WCReference getReference() {
        return this.reference;
    }

    @Override
    public void setReference(@Nullable WCReference reference) {
        this.reference = reference;
    }
}
