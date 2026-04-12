package com.asledgehammer.webcalc.io.token;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class WCReferenceRange extends WCReference {

  @Getter private final int rowEnd;
  @Getter private final int colEnd;
  @Getter private final int length;

  public WCReferenceRange(
      @Nullable final URL path,
      final int index,
      final int length,
      final int[] start,
      final int[] end) {
    super(path, index, start);
    this.length = length;

    if (end.length != 2) {
      throw new IllegalArgumentException("end length must be 2");
    }
    this.rowEnd = end[0];
    this.colEnd = end[1];
    if (this.rowEnd < 0 || this.colEnd < 0) {
      throw new IllegalArgumentException("row or col end less than zero!");
    }
  }

  public WCReferenceRange(
      final @Nullable URL path,
      final int index,
      final int length,
      final int rowStart,
      final int colStart,
      final int rowEnd,
      final int colEnd) {
    super(path, index, rowStart, colStart);
    this.length = length;
    this.rowEnd = rowEnd;
    this.colEnd = colEnd;
  }
}
