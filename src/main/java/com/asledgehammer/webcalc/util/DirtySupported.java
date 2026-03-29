package com.asledgehammer.webcalc.util;

public interface DirtySupported {

    boolean isDirty();

    void setDirty(boolean flag);

    default void setDirty(boolean flag, boolean cascadeDownstream) {
        // Optional API call to route for hierarchical dirty objects.
        setDirty(flag);
    }
}
