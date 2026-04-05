package com.asledgehammer.webcalc.css.io.lexer;

import com.asledgehammer.webcalc.css.io.token.WCSSCommentBlockToken;
import com.asledgehammer.webcalc.css.io.token.WCSSCommentBlockTokenImpl;
import com.asledgehammer.webcalc.io.WCParseError;
import com.asledgehammer.webcalc.io.token.WCGenericTokenImpl;
import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import com.asledgehammer.webcalc.io.token.WCReferencedToken;
import com.asledgehammer.webcalc.css.io.token.WCSSWhiteSpaceTokenImpl;
import com.asledgehammer.webcalc.io.MultiLineContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WCStyleSheetMarcher {

  @Getter private final List<WCReferencedToken> tokens = new ArrayList<>();
  @Getter private final List<WCSSCommentBlockToken> comments = new ArrayList<>();
  @Getter private final List<WCParseError> errors = new ArrayList<>();
  @Getter private final List<int[]> rangesIgnored = new ArrayList<>();

  final MultiLineContext context;
  private final URL path;
  int offset = 0;

  public WCStyleSheetMarcher(@Nullable URL path, @NonNull String contents) {
    this.path = path;
    this.context = new MultiLineContext(contents);
  }

  private boolean isIgnoredIndex(int index) {
    for (final int[] range : this.rangesIgnored) {
      if (range[0] <= index && index <= range[1]) {
        return true;
      }
    }
    return false;
  }

  private int skipIgnoredIndex(int index) {
    for (final int[] range : this.rangesIgnored) {
      if (range[0] <= index && index <= range[1]) {
        return range[1] + 1;
      }
    }
    return index;
  }

  private void marchComments() {

    int indexStart = 0;
    int indexEnd;

    String content = context.getContent();
    while ((indexStart = content.indexOf("/*", indexStart)) != -1) {
      indexEnd = content.indexOf("*/", indexStart + 2);
      if (indexEnd == -1) {
        indexEnd = context.getLength();
        val length = indexEnd - indexStart;
        val start = context.getRowCol(indexStart);
        val end = context.getRowCol(indexEnd);
        val ref = new WCReferenceRangeImpl(path, indexStart, length, start, end);
        rangesIgnored.add(new int[] {indexStart, indexEnd});
        errors.add(new WCParseError(ref, "Unclosed comment block"));
        break;
      }

      // Retain outer raw indexed range.
      indexEnd += 2;
      rangesIgnored.add(new int[] {indexStart, indexEnd});
      val length = indexEnd - indexStart;
      val start = context.getRowCol(indexStart);
      val end = context.getRowCol(indexEnd);
      val ref = new WCReferenceRangeImpl(path, indexStart, length, start, end);
      comments.add(new WCSSCommentBlockTokenImpl(ref, context.sub(indexStart, indexEnd)));
      indexStart = indexEnd;
    }
  }

  void march() {
    marchComments();
    while (!context.isEOS(offset)) {
      if (isIgnoredIndex(offset)) {
        offset = skipIgnoredIndex(offset);
        continue;
      }
      untilWhiteSpace();
    }
  }

  /** NOTE: Assumes EOS is checked prior to call. */
  private void untilWhiteSpace() {
    if (isIgnoredIndex(offset)) {
      offset = skipIgnoredIndex(offset);
      return;
    }
    if (isWhiteSpaceChar(offset)) {
      untilNonWhiteSpace();
      return;
    }

    if (context.isEOS(offset)) return;

    final int indexStart = offset;
    int indexEnd = offset;
    while (!isWhiteSpaceChar(indexEnd++)) {
      if (isIgnoredIndex(indexEnd) || context.isEOS(indexEnd)) break;
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
    } else {
      offset = indexEnd + 1;
    }
  }

  private void untilNonWhiteSpace() {
    if (isIgnoredIndex(offset)) {
      offset = skipIgnoredIndex(offset);
      return;
    }
    if (!isWhiteSpaceChar(offset)) {
      untilWhiteSpace();
      return;
    }

    if (context.isEOS(offset)) return;

    final int indexStart = offset;
    int indexEnd = offset;
    while (isWhiteSpaceChar(indexEnd++)) {
      if (isIgnoredIndex(indexEnd) || context.isEOS(indexEnd)) break;
    }
    indexEnd--;
    val len = indexEnd - indexStart;
    if (len != 0) {
      int[] start = context.getRowCol(indexStart);
      int[] end = context.getRowCol(indexEnd);
      val ref = new WCReferenceRangeImpl(path, indexStart, len, start, end);
      tokens.add(new WCSSWhiteSpaceTokenImpl(ref, context.sub(indexStart, indexEnd)));
      offset = indexEnd;
    } else if (context.isEOS(indexEnd + 1)) {
      int[] start = context.getRowCol(indexStart);
      int[] end = context.getRowCol(indexEnd + 1);
      val ref = new WCReferenceRangeImpl(path, indexStart, len + 1, start, end);
      tokens.add(new WCSSWhiteSpaceTokenImpl(ref, context.sub(indexStart, indexEnd + 1)));
      offset = indexEnd + 1;
    } else {
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
