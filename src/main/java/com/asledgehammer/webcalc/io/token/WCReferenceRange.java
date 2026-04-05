package com.asledgehammer.webcalc.io.token;

public interface WCReferenceRange extends WCReference {
    int getRowEnd();
    int getColEnd();
    int getLength();
}
