package com.asledgehammer.webcalc.css.io;

import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class WCFileReferenceImpl implements WCFileReference {

    private final @Nullable URL path;
    private final int index;
    private final int rowStart;
    private final int colStart;

    public WCFileReferenceImpl(@Nullable URL path, int index, int rowStart, int colStart) {
        this.path = path;
        this.index = index;
        this.rowStart = rowStart;
        this.colStart = colStart;
    }

    public WCFileReferenceImpl(@Nullable URL path, int index, int[] start) {
        this.path = path;
        this.index = index;

        if (start.length != 2) {
            throw new IllegalArgumentException("start length must be 2");
        }
        this.rowStart = start[0];
        this.colStart = start[1];
        if (this.rowStart < 0 || this.colStart < 0) {
            throw new IllegalArgumentException("row or col end less than zero!");
        }
    }

    @Nullable
    @Override
    public URL getPath() {
        return path;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getStartRow() {
        return rowStart;
    }

    @Override
    public int getStartCol() {
        return colStart;
    }
}
