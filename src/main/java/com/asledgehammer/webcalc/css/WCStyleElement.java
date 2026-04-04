package com.asledgehammer.webcalc.css;

import com.asledgehammer.webcalc.io.token.WCReference;
import org.jetbrains.annotations.Nullable;

public interface WCStyleElement {
    /**
     * @return If provided, the reference & range of the element on a stylesheet. Otherwise <code>null</code>.
     */
    @Nullable
    WCReference getReference();

    void setReference(@Nullable WCReference reference);
}
