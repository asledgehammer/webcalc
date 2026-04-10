package com.asledgehammer.webcalc.css.io.lexer;

import com.asledgehammer.webcalc.css.io.token.*;
import com.asledgehammer.webcalc.io.WCParseError;
import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import com.asledgehammer.webcalc.io.MultiLineContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WCStyleSheetMarcher {

  @Getter private final List<WCSSToken> tokens = new ArrayList<>();
  @Getter private final List<WCSSCommentBlockToken> comments = new ArrayList<>();
  @Getter private final List<WCParseError> errors = new ArrayList<>();
  @Getter private final List<int[]> rangesIgnored = new ArrayList<>();

  private final List<WCSSStringToken> strings = new ArrayList<>();

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

  void march() {
    marchComments();
    marchStrings();
//    while (!context.isEOS(offset)) {
//      if (isIgnoredIndex(offset)) {
//        offset = skipIgnoredIndex(offset);
//        continue;
//      }
//      untilWhiteSpace();
//    }
    refine();
    combine();
  }

  private void marchStrings() {
    marchStrings('"');
    marchStrings('\'');
  }

  private void marchStrings(char encasingCharacter) {
    final int len = context.getLength();
    final String content = context.getContent();
    int indexStart = 0;
    int indexEnd;
    int indexNewLine;

    while ((indexStart = content.indexOf(encasingCharacter, indexStart)) != -1) {

      // Cycle through escaped double-quotes inside strings.
      do {
        indexEnd = content.indexOf(encasingCharacter, indexStart + 1);
      } while (indexEnd > 0 && indexEnd < len && content.charAt(indexEnd - 1) == '\\');

      indexNewLine = context.getNextNewlineIndex(indexStart);

      // Make sure the string doesn't contain any line-returns.
      if (indexNewLine != -1 && (indexEnd == -1 || indexNewLine < indexEnd)) {
        val string = content.substring(indexStart, indexNewLine - 1);
        val start = context.getRowCol(indexStart);
        val end = context.getRowCol(indexNewLine);
        val ref = new WCReferenceRangeImpl(path, indexStart, string.length(), start, end);
        rangesIgnored.add(new int[] {indexStart, indexNewLine});
        errors.add(new WCParseError(ref, "Broken string"));
        strings.add(new WCSSStringTokenImpl(ref, string, encasingCharacter, true));
        indexStart = indexNewLine + 1;
        continue;
      }

      // EOS
      if (indexEnd == -1) {
        indexEnd = len;
        val string = content.substring(indexStart, content.length() - 1);
        val start = context.getRowCol(indexStart);
        val end = context.getRowCol(indexEnd);
        val ref = new WCReferenceRangeImpl(path, indexStart, string.length(), start, end);
        rangesIgnored.add(new int[] {indexStart, indexEnd});
        errors.add(new WCParseError(ref, "Unclosed string value"));
        strings.add(
            new WCSSStringTokenImpl(
                ref, context.sub(indexStart, indexEnd), encasingCharacter, true));
        break;
      }

      // Retain outer raw indexed range.
      indexEnd += 1;
      rangesIgnored.add(new int[] {indexStart, indexEnd});
      val string = content.substring(indexStart, indexEnd);
      val start = context.getRowCol(indexStart);
      val end = context.getRowCol(indexEnd);
      val ref = new WCReferenceRangeImpl(path, indexStart, string.length(), start, end);
      strings.add(new WCSSStringTokenImpl(ref, string, encasingCharacter, string.contains("\r\n")));
      indexStart = indexEnd;
    }
  }

  private void marchComments() {

    int indexStart = 0;
    int indexEnd;

    String content = context.getContent();
    while ((indexStart = content.indexOf("/*", indexStart)) != -1) {
      indexEnd = content.indexOf("*/", indexStart + 2);

      // EOS
      if (indexEnd == -1) {
        indexEnd = context.getLength();
        val comment = context.sub(indexStart, indexEnd);
        val start = context.getRowCol(indexStart);
        val end = context.getRowCol(indexEnd);
        val ref = new WCReferenceRangeImpl(path, indexStart, comment.length(), start, end);
        rangesIgnored.add(new int[] {indexStart, indexEnd});
        errors.add(new WCParseError(ref, "Unclosed comment block"));
        comments.add(new WCSSCommentBlockTokenImpl(ref, comment));
        break;
      }

      // Retain outer raw indexed range.
      indexEnd += 2;
      rangesIgnored.add(new int[] {indexStart, indexEnd});
      val comment = context.sub(indexStart, indexEnd);
      val start = context.getRowCol(indexStart);
      val end = context.getRowCol(indexEnd);
      val ref = new WCReferenceRangeImpl(path, indexStart, content.length(), start, end);
      comments.add(new WCSSCommentBlockTokenImpl(ref, comment));
      indexStart = indexEnd;
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
      tokens.add(new WCSSGenericTokenImpl(ref, context.sub(indexStart, indexEnd)));
      offset = indexEnd;
    } else if (context.isEOS(indexEnd + 1)) {
      int[] start = context.getRowCol(indexStart);
      int[] end = context.getRowCol(indexEnd + 1);
      val ref = new WCReferenceRangeImpl(path, indexStart, len + 1, start, end);
      tokens.add(new WCSSGenericTokenImpl(ref, context.sub(indexStart, indexEnd + 1)));
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

  private void refine() {
    final List<WCSSToken> refined = new ArrayList<>();
    final List<WCSSToken> tokens = new ArrayList<>(this.tokens);
  }

  /** Takes all parsed tokens and sorts them based on their place in the stream. */
  private void combine() {
    final List<WCSSToken> combined = new ArrayList<>();
    combined.addAll(this.tokens);
    combined.addAll(this.strings);
    combined.sort(Comparator.comparingInt(a -> a.getReference().getIndex()));

    this.tokens.clear();
    this.tokens.addAll(combined);
  }

  boolean isWhiteSpaceChar(int index) {
    return switch (context.charAt(index)) {
      case '\f', '\n', '\r', '\t', ' ' -> true;
      default -> false;
    };
  }

class Something {
  boolean keepGoing = true;
  WCReferenceRange ref;
  String contents;
  int len;
  int tokenIndex = 0;
  int indexContents;

  char curr;

  Something(List<WCSSToken> tokens) {
    keepGoing = !tokens.isEmpty();
  }

  void next() {
    if (indexContents >= len) {

    }
  }
}
}
