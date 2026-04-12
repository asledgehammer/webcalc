package com.asledgehammer.webcalc.css.io.token;

import lombok.NonNull;

public interface WCSSUrlToken {

    @NonNull
    String getURL();

    boolean isBad();
}
