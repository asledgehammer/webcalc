package com.asledgehammer.webcalc.css.io;

public interface WCFileReferenceBlock extends WCFileReference {
    int getRowEnd();
    int getColEnd();

    int getLength();
}
