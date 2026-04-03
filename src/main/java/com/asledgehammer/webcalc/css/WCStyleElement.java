package com.asledgehammer.webcalc.css;

import com.asledgehammer.webcalc.css.io.WCFileReference;
import org.jetbrains.annotations.Nullable;

public interface WCStyleElement {
    /**
     * @return If provided, the reference & range of the element on a stylesheet. Otherwise <code>null</code>.
     */
    @Nullable
    WCFileReference getReference();

    void setReference(@Nullable WCFileReference reference);
}
