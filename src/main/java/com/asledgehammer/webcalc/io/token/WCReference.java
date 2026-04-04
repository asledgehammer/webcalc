package com.asledgehammer.webcalc.io.token;

import org.jetbrains.annotations.Nullable;

import java.net.URL;

public interface WCReference {
    @Nullable
    URL getPath();

    int getIndex();

    int getStartRow();

    int getStartCol();
}
