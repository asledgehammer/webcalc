package com.asledgehammer.webcalc.css.io.lexer.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import com.asledgehammer.webcalc.io.token.WCToken;
import lombok.NonNull;

public interface WCReferencedToken extends WCToken {
    @NonNull
    WCReferenceRange getReference();
}
