package com.asledgehammer.webcalc.css.io;

import org.jetbrains.annotations.Nullable;

import java.net.URL;

public interface WCFileReference {
    @Nullable
    URL getPath();

    int getIndex();

    int getStartRow();

    int getStartCol();
}
