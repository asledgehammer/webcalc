package com.asledgehammer.webcalc.io.token;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class WCReference {

  private final @Nullable URL path;
  @Getter private final int index;
  private final int rowStart;
  private final int colStart;

  public WCReference(@Nullable URL path, int index, int rowStart, int colStart) {
    this.path = path;
    this.index = index;
    this.rowStart = rowStart;
    this.colStart = colStart;
  }

  public WCReference(@Nullable URL path, int index, int[] start) {
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
      result.append(":");
    }
    result.append(shiftRight(rowStart, 4));
    result.append(",");
    result.append(shiftRight(colStart, 4));
    result.append("]");
    return result.toString();
  }

  @Nullable
  public URL getPath() {
    return path;
  }

  public int getStartRow() {
    return rowStart;
  }

  public int getStartCol() {
    return colStart;
  }

  private static String shiftRight(int value, int spaces) {
    StringBuilder s = new StringBuilder("" + value);
    while (s.length() < spaces) {
      s.insert(0, " ");
    }
    return s.toString();
  }
}
