package com.asledgehammer.webcalc.io.token;

import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class WCReferenceImpl implements WCReference {

  private final @Nullable URL path;
  private final int index;
  private final int rowStart;
  private final int colStart;

  public WCReferenceImpl(@Nullable URL path, int index, int rowStart, int colStart) {
    this.path = path;
    this.index = index;
    this.rowStart = rowStart;
    this.colStart = colStart;
  }

  public WCReferenceImpl(@Nullable URL path, int index, int[] start) {
    this.path = path;
    this.index = index;

    if (start.length != 2) {
      throw new IllegalArgumentException("start length must be 2");
    }
    this.rowStart = start[0];
    this.colStart = start[1];
    if (this.rowStart < 0 || this.colStart < 0) {
      throw new IllegalArgumentException(
          "row or col end less than zero! (row: "
              + this.rowStart
              + ", col: "
              + this.colStart
              + ")");
    }
  }

  @Override
  public String toString() {
    return toString(false);
  }

  public String toString(boolean showPath) {
    StringBuilder result = new StringBuilder();
    result.append("[");
    if (showPath) {
      if (path != null) {
        result.append(path);
      } else {
        result.append("anonymous");
      }
      result.append(": ");
    }
    result.append(rowStart);
    result.append(", ");
    result.append(colStart);
    result.append("]");
    return result.toString();
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
