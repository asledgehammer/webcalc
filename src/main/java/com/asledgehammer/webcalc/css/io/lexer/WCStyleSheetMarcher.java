package com.asledgehammer.webcalc.css.io.lexer;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import com.asledgehammer.webcalc.css.io.lexer.token.WCReferencedToken;
import com.asledgehammer.webcalc.css.io.lexer.token.WCWhiteSpaceTokenImpl;
import com.asledgehammer.webcalc.io.MultiLineContext;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WCStyleSheetMarcher {
  final List<WCReferencedToken> tokens = new ArrayList<>();

  final MultiLineContext context;
  private final URL path;
  int offset = 0;

  public WCStyleSheetMarcher(@Nullable URL path, @NonNull String contents) {
    this.path = path;
    this.context = new MultiLineContext(contents);
  }

  void march() {
    while (!context.isEOS(offset)) {
      untilWhiteSpace();
    }
  }

  void march2() {

  }

  /** NOTE: Assumes EOS is checked prior to call. */
  private void untilWhiteSpace() {
    if (isWhiteSpaceChar(offset)) {
      untilNonWhiteSpace();
      return;
    }

    if (context.isEOS(offset)) return;

    final int indexStart = offset;
    int indexEnd = offset;
    while (!isWhiteSpaceChar(indexEnd++)) {
      if (context.isEOS(indexEnd)) break;
    }
    indexEnd--;
    val len = indexEnd - indexStart;
    if (len != 0) {
      int[] start = context.getRowCol(indexStart);
      int[] end = context.getRowCol(indexEnd);
      val ref = new WCReferenceRangeImpl(path, indexStart, len, start, end);
      tokens.add(new WCGenericTokenImpl(ref, context.sub(indexStart, indexEnd)));
      offset = indexEnd;
    } else if (context.isEOS(indexEnd + 1)) {
      int[] start = context.getRowCol(indexStart);
      int[] end = context.getRowCol(indexEnd + 1);
      val ref = new WCReferenceRangeImpl(path, indexStart, len + 1, start, end);
      tokens.add(new WCGenericTokenImpl(ref, context.sub(indexStart, indexEnd + 1)));
      offset = indexEnd + 1;
    }
  }

  private void untilNonWhiteSpace() {
    if (!isWhiteSpaceChar(offset)) {
      untilWhiteSpace();
      return;
    }

    if (context.isEOS(offset)) return;

    final int indexStart = offset;
    int indexEnd = offset;
    while (isWhiteSpaceChar(indexEnd++)) {
      if (context.isEOS(indexEnd)) break;
    }
    indexEnd--;
    val len = indexEnd - indexStart;
    if (len != 0) {
      int[] start = context.getRowCol(indexStart);
      int[] end = context.getRowCol(indexEnd);
      val ref = new WCReferenceRangeImpl(path, indexStart, len, start, end);
      tokens.add(new WCWhiteSpaceTokenImpl(ref, context.sub(indexStart, indexEnd)));
      offset = indexEnd;
    } else if (context.isEOS(indexEnd + 1)) {
      int[] start = context.getRowCol(indexStart);
      int[] end = context.getRowCol(indexEnd + 1);
      val ref = new WCReferenceRangeImpl(path, indexStart, len + 1, start, end);
      tokens.add(new WCWhiteSpaceTokenImpl(ref, context.sub(indexStart, indexEnd + 1)));
      offset = indexEnd + 1;
    }
  }

  boolean isWhiteSpaceChar(int index) {
    return switch (context.charAt(index)) {
      case '\f', '\n', '\r', '\t', ' ' -> true;
      default -> false;
    };
  }
}
