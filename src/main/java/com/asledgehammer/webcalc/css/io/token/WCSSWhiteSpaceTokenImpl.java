package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import com.asledgehammer.webcalc.io.token.WCReferencedTokenImpl;
import lombok.NonNull;

public class WCSSWhiteSpaceTokenImpl extends WCReferencedTokenImpl implements WCSSWhiteSpaceToken {
    public WCSSWhiteSpaceTokenImpl(@NonNull WCReferenceRange reference, @NonNull String contents) {
        super(reference, contents, false);
    }
}
