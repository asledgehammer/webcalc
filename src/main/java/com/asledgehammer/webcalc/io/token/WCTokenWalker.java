package com.asledgehammer.webcalc.io.token;

import lombok.NonNull;

import java.util.List;

public interface WCTokenWalker {
    @NonNull
    List<WCToken> digest();
}
