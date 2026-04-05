package com.asledgehammer.webcalc.io.token;

import lombok.NonNull;

public interface WCReferencedToken extends WCToken {
    @NonNull
    WCReferenceRange getReference();
}
