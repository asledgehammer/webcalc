package com.asledgehammer.webcalc.css;

import com.asledgehammer.webcalc.css.io.WCFileReference;
import org.jetbrains.annotations.Nullable;

public abstract class WCStyleElementImpl implements WCStyleElement {

    @Nullable
    private WCFileReference reference;

    protected WCStyleElementImpl() {
    }

    @Nullable
    @Override
    public WCFileReference getReference() {
        return this.reference;
    }

    @Override
    public void setReference(@Nullable WCFileReference reference) {
        this.reference = reference;
    }
}
