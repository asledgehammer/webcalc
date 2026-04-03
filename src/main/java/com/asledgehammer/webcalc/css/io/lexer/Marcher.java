package com.asledgehammer.webcalc.css.io.lexer;

import com.asledgehammer.webcalc.css.io.WCFileReferenceBlockImpl;
import com.asledgehammer.webcalc.css.io.lexer.token.WCStyleSheetToken;
import com.asledgehammer.webcalc.css.io.lexer.token.WCWhiteSpaceTokenImpl;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Marcher {
  final List<WCStyleSheetToken> tokens = new ArrayList<>();
  final @NonNull String contents;
  private final int[] rowIndexes;
  final int cLength;
  private final URL path;
  int index = 0;

  public Marcher(@Nullable URL path, @NonNull String contents) {
    this.path = path;
    this.contents = contents;
    this.cLength = contents.length();
    this.rowIndexes = WCStyleSheetLexer.getRowIndexesFromString(contents);
  }

  void march() {
    while (!isEOF()) {
      untilWhiteSpace();
    }
  }

  /** NOTE: Assumes {@link Marcher#isEOF isEOF()} is checked prior to call. */
  private void untilWhiteSpace() {
    if (isWhiteSpaceChar(index)) {
      untilNonWhiteSpace();
      return;
    }

    if (isEOF()) return;

    final int indexStart = index;
    int indexEnd = index;
    while (!isWhiteSpaceChar(indexEnd++)) {
      if (isEOF(indexEnd)) break;
    }
    indexEnd--;
    val len = indexEnd - indexStart;
    if (len != 0) {
      int[] start = getRowCol(indexStart);
      int[] end = getRowCol(indexEnd);
      val ref = new WCFileReferenceBlockImpl(path, indexStart, len, start, end);
      tokens.add(new WCGenericTokenImpl(ref, contents.substring(indexStart, indexEnd)));
      index = indexEnd;
    } else if (isEOF(indexEnd + 1)) {
      int[] start = getRowCol(indexStart);
      int[] end = getRowCol(indexEnd + 1);
      val ref = new WCFileReferenceBlockImpl(path, indexStart, len + 1, start, end);
      tokens.add(new WCGenericTokenImpl(ref, contents.substring(indexStart, indexEnd + 1)));
      index = indexEnd + 1;
    }
  }

  private void untilNonWhiteSpace() {
    if (!isWhiteSpaceChar(index)) {
      untilWhiteSpace();
      return;
    }

    if (isEOF()) return;

    final int indexStart = index;
    int indexEnd = index;
    while (isWhiteSpaceChar(indexEnd++)) {
      if (isEOF(indexEnd)) break;
    }
    indexEnd--;
    val len = indexEnd - indexStart;
    if (len != 0) {
      int[] start = getRowCol(indexStart);
      int[] end = getRowCol(indexEnd);
      val ref = new WCFileReferenceBlockImpl(path, indexStart, len, start, end);
      tokens.add(new WCWhiteSpaceTokenImpl(ref, contents.substring(indexStart, indexEnd)));
      index = indexEnd;
    } else if (isEOF(indexEnd + 1)) {
      int[] start = getRowCol(indexStart);
      int[] end = getRowCol(indexEnd + 1);
      val ref = new WCFileReferenceBlockImpl(path, indexStart, len + 1, start, end);
      tokens.add(new WCWhiteSpaceTokenImpl(ref, contents.substring(indexStart, indexEnd + 1)));
      index = indexEnd + 1;
    }
  }

  private int[] getRowCol(int cIndex) {
    if (rowIndexes == null) {
      throw new IllegalArgumentException("indexes cannot be null", new NullPointerException());
    } else if (cIndex < 0) {
      throw new IllegalArgumentException("The index out of bounds!");
    } else if (rowIndexes.length == 0) {
      throw new IllegalArgumentException("No indexes!");
    }

    // First line.
    if (cIndex < rowIndexes[0]) {
      return new int[] {1, cIndex + 1};
    }

    for (int row = 0; row < rowIndexes.length - 1; row++) {
      int rowIndexCurr = rowIndexes[row];
      int rowIndexNext = rowIndexes[row + 1];
      if (rowIndexCurr <= cIndex && rowIndexNext > cIndex) {
        return new int[] {row + 1, (cIndex - rowIndexCurr) + 1};
      }
    }

    // Last line.
    return new int[] {rowIndexes.length, (cIndex - rowIndexes[rowIndexes.length - 1]) + 1};
  }

  boolean isWhiteSpaceChar(int index) {
    return switch (contents.charAt(index)) {
      case '\f', '\n', '\r', '\t', ' ' -> true;
      default -> false;
    };
  }

  boolean isEOF(int index) {
    return index >= contents.length();
  }

  boolean isEOF() {
    return index >= contents.length();
  }
}
