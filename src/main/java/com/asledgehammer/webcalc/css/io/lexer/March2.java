package com.asledgehammer.webcalc.css.io.lexer;

import com.asledgehammer.webcalc.css.io.token.*;
import com.asledgehammer.webcalc.io.WCParseError;
import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.asledgehammer.webcalc.css.CSSUtils.PATTERN_NEWLINE;

public class March2 implements Runnable {

  private static final Pattern REGEX_HEX_DIGIT = Pattern.compile("[0-9A-Fa-f]");
  private static final Pattern REGEX_IDENT_CODE_POINT = Pattern.compile("[A-Za-z0-9_\\-]");
  private static final Pattern REGEX_IDENT_START_CODE_POINT = Pattern.compile("[A-Za-z_]");
  private static final int[] BLANK_ROW_COL = new int[] {1, 1};

  @Getter private final List<WCSSCommentBlockToken> comments = new ArrayList<>();
  @Getter private final List<WCSSToken> tokens = new ArrayList<>();
  @Getter private final List<int[]> rangesIgnored = new ArrayList<>();
  @Getter private final List<WCParseError> errors = new ArrayList<>();
  @Getter private final int[] rowIndexes;

  private final String contents;
  private final @NonNull URL path;
  private int offset = 0;
  private final int len;

  public March2(@NonNull URL path, @NonNull final String contents) {
    if (contents.isEmpty()) {
      throw new IllegalArgumentException("contents must not be empty");
    }
    this.path = path;
    this.contents = contents;
    this.len = contents.length();
    this.rowIndexes = getRowIndexes(contents);
  }

  public void run() {
    marchComments();
    marchTokens();
  }

  private void marchComments() {

    int indexStart = 0;
    int indexEnd;

    while ((indexStart = contents.indexOf("/*", indexStart)) != -1) {
      indexEnd = contents.indexOf("*/", indexStart + 2);

      // EOS
      if (indexEnd == -1) {
        indexEnd = len;
        rangesIgnored.add(new int[] {indexStart, indexEnd});
        val comment = contents.substring(indexStart, indexEnd);
        val start = getRowCol(indexStart);
        val end = getRowCol(indexEnd);
        val ref = new WCReferenceRangeImpl(path, indexStart, comment.length(), start, end);
        errors.add(new WCParseError(ref, "Unclosed comment block"));
        comments.add(new WCSSCommentBlockTokenImpl(ref, comment));
        break;
      }

      // Retain outer raw indexed range.
      indexEnd += 2;
      rangesIgnored.add(new int[] {indexStart, indexEnd});
      val comment = contents.substring(indexStart, indexEnd);
      val start = getRowCol(indexStart);
      val end = getRowCol(indexEnd);
      val ref = new WCReferenceRangeImpl(path, indexStart, comment.length(), start, end);
      comments.add(new WCSSCommentBlockTokenImpl(ref, comment));
      indexStart = indexEnd;
    }

    // Reset head to 0.
    offset = 0;
  }

  private void marchTokens() {
    while (offset < len) {

      // Ignore comment ranges.
      if (isIgnoredIndex(offset)) {
        offset = skipIgnoredIndex(offset);
        continue;
      }

      // Consume as much whitespace as possible.
      consumeWhitespace();

      // Ignore comment ranges.
      if (isIgnoredIndex(offset)) {
        offset = skipIgnoredIndex(offset);
        continue;
      }

      // Any non-whitespace character starts the next token.
      char curr = contents.charAt(offset);
      if (contents.indexOf("/*", offset) == offset) {
        offset = skipIgnoredIndex(offset + 2);
      } else if (curr == '"') {
        consumeStringToken('"');
      } else if (curr == '#') {
        consumeHashToken();
      } else if (curr == '\'') {
        consumeStringToken('\'');
      } else if (curr == '(') {
        consumeParenthesisToken(true);
      } else if (curr == ')') {
        consumeParenthesisToken(false);
      } else if (curr == '+') {

        // Explicit positive numeric values
        if (isNumericStartChar(contents.charAt(offset + 1))) {
          offset++;
          consumeNumericToken('+');
          val next = contents.charAt(offset);
          if (isIdentStartCodePoint(next) && isIdentStartCodePoint(contents.charAt(offset + 1))) {
            consumeDimensionToken();
          } else if (next == '%') {
            consumePercentageToken();
          }
        } else {
          consumeDelimiterToken("+");
        }
      } else if (curr == ',') {
        consumeCommaToken();
      } else if (curr == '-') {

        if (contents.indexOf("-->") == offset) {
          consumeCDCToken();
        } else if (isNumericStartChar(contents.charAt(offset + 1))) {
          offset++;
          consumeNumericToken('-');
          val next = contents.charAt(offset);
          if (isIdentStartCodePoint(next) && isIdentStartCodePoint(contents.charAt(offset + 1))) {
            consumeDimensionToken();
          } else if (next == '%') {
            consumePercentageToken();
          }
        } else if (isIdentStartSequence(offset + 1)) {
          consumeIdentityToken();
        } else {
          consumeDelimiterToken("-");
        }
      } else if (curr == '.') {
        consumeDotToken();
      } else if (curr == ':') {
        consumeColonToken();
      } else if (curr == ';') {
        consumeSemicolonToken();
      } else if (curr == '<') {
        if (contents.indexOf("<!--") == offset) {
          consumeCDOToken();
        } else {
          consumeDelimiterToken("<");
        }
      } else if (curr == '@') {
        consumeAtToken();
      } else if (curr == '[') {
        consumeSquareBracketToken(true);
      } else if (curr == '\\') {
        // I think this is only a parse error at this point??
        consumeDelimiterToken("\\");
      } else if (curr == ']') {
        consumeSquareBracketToken(false);
      } else if (curr == '{') {
        consumeCurlyBracketToken(true);
      } else if (curr == '}') {
        consumeCurlyBracketToken(false);
      }
      // TODO: Parse `url(` without string and try to recover from bad URLs.
      else if (isNumericChar(curr)) {
        // Catch for comments range.
        if (isIgnoredIndex(offset)) {
          offset = skipIgnoredIndex(offset);
          continue;
        }
        consumeNumericToken(null);
        val next = contents.charAt(offset);
        if (isIdentStartCodePoint(next) && isIdentStartCodePoint(contents.charAt(offset + 1))) {
          consumeDimensionToken();
        } else if (next == '%') {
          consumePercentageToken();
        }
      } else if (isIdentStartSequence(offset)) {
        // Catch for comments range.
        if (isIgnoredIndex(offset)) {
          offset = skipIgnoredIndex(offset);
          continue;
        }
        consumeIdentLikeToken();
      } else {
        // Catch for comments range.
        if (isIgnoredIndex(offset)) {
          offset = skipIgnoredIndex(offset);
          continue;
        }
        consumeDelimiterToken("" + curr);
      }
    }
  }

  /** <a href="https://www.w3.org/TR/css-syntax-3/#consume-ident-like-token">W3 Docs</a> */
  private void consumeIdentLikeToken() {
    val start = getRowCol(offset);
    val leading = getIdentitySequence(offset);
    val len = leading.length();
    char curr = contents.charAt(offset + len);

    // W3 docs says this is a case-sensitive scenario.
    if (leading.equals("url")) {
      if (curr == '(') {
        char next = contents.charAt(offset + len + 1);
        if (isWhitespaceChar(offset + len + 1)) {
          consumeWhitespace();
        }
        if (next == '"' || next == '\'') {
          val end = getRowCol(offset + len + 1);
          val ref = new WCReferenceRangeImpl(path, offset, len + 1, start, end);
          tokens.add(new WCSSFunctionTokenImpl(ref, leading));
          offset += len + 1;
          return;
        }
        offset += len + 1;
        consumeUrlToken();
        return;
      }
    } else if (curr == '(') {
      val end = getRowCol(offset + len + 1);
      val ref = new WCReferenceRangeImpl(path, offset, len + 1, start, end);
      tokens.add(new WCSSFunctionTokenImpl(ref, leading));
      offset += len + 1;
      return;
    }

    // No <function-token>, <url-token> or <bad-url-token>
    val end = getRowCol(offset + len);
    val ref = new WCReferenceRangeImpl(path, offset, len, start, end);
    tokens.add(new WCSSIdentTokenImpl(ref, leading));
    this.offset += len;
  }

  private void consumeUrlToken() {
    val offsetStart = offset - 4;
    val start = getRowCol(offsetStart);
    val builder = new StringBuilder();
    if (isWhitespaceChar(offset)) {
      consumeWhitespace();
    }

    boolean bad = false;
    char last = 0;
    char curr;
    do {
      curr = contents.charAt(offset);
      if (last != '\\' && curr == ')') {
        break;
      } else if (last != '\\' && (curr == '\"' || curr == '\'' || curr == '(')) {
        bad = true;
      }
      builder.append(curr);
      last = curr;
      offset++;
    } while (!isEOS());
    String value = builder.toString().trim();

    if (isEOS()) {
      val len = value.length();
      val end = getRowCol(this.len - 1);
      val ref = new WCReferenceRangeImpl(path, offsetStart, len + 4, start, end);
      tokens.add(new WCSSURLTokenImpl(ref, value, true));
      // EOS position.
      offset = this.len;
      return;
    } else if (bad) {
      val len = value.length();
      val end = getRowCol(offset);
      val ref = new WCReferenceRangeImpl(path, offsetStart, len + 4, start, end);
      tokens.add(new WCSSURLTokenImpl(ref, value, true));
      // EOS position.
    }

    val len = value.length();
    val end = getRowCol(this.len - 1);
    val ref = new WCReferenceRangeImpl(path, offsetStart, len + 4, start, end);
    tokens.add(new WCSSURLTokenImpl(ref, value, false));
  }

  private boolean isEOS() {
    return offset >= len;
  }

  private void consumePercentageToken() {
    val start = getRowCol(offset);
    val end = getRowCol(offset + 1);
    val ref = new WCReferenceRangeImpl(path, offset, 1, start, end);
    tokens.add(new WCSSPercentageTokenImpl(ref));
    offset++;
  }

  private void consumeDimensionToken() {
    val start = getRowCol(offset);
    val value = getIdentitySequence(offset);
    val len = value.length();
    val end = getRowCol(offset + len);
    val ref = new WCReferenceRangeImpl(path, offset, len, start, end);
    tokens.add(new WCSSDimensionTokenImpl(ref, value));
    offset += value.length();
  }

  private void consumeNumericToken(@Nullable Character prepend) {
    val start = getRowCol(offset);
    val value = (prepend != null ? prepend : "") + getNumericSequence(offset);
    val len = value.length();
    val end = getRowCol(offset + len);
    val ref = new WCReferenceRangeImpl(path, offset, len, start, end);
    tokens.add(new WCSSNumericTokenImpl(ref, value));
    this.offset += value.length();
  }

  private boolean isNumericChar(char c) {
    return Character.isDigit(c);
  }

  private boolean isNumericStartChar(char c) {
    return c == '.' || Character.isDigit(c);
  }

  private void consumeCDOToken() {
    val start = getRowCol(offset);
    val end = getRowCol(offset + 4);
    val value = contents.substring(offset, offset + 4);
    val len = value.length();
    val ref = new WCReferenceRangeImpl(path, offset, len, start, end);
    tokens.add(new WCSSCDOTokenImpl(ref));
    offset += 4;
  }

  private void consumeCDCToken() {
    val start = getRowCol(offset);
    val end = getRowCol(offset + 3);
    val value = contents.substring(offset, offset + 3);
    val len = value.length();
    val ref = new WCReferenceRangeImpl(path, offset, len, start, end);
    tokens.add(new WCSSCDOTokenImpl(ref));
    offset += 3;
  }

  private void consumeDelimiterToken(@NotNull String delimiter) {
    if (delimiter.equals("1")) {
      throw new RuntimeException(delimiter);
    }
    val start = getRowCol(offset);
    val end = getRowCol(offset + 1);
    val ref = new WCReferenceRangeImpl(path, offset, 1, start, end);
    tokens.add(new WCSSDelimiterTokenImpl(ref, delimiter));
    offset++;
  }

  private void consumeParenthesisToken(boolean left) {
    val start = getRowCol(offset);
    val end = getRowCol(offset + 1);
    val ref = new WCReferenceRangeImpl(path, offset, 1, start, end);
    if (left) {
      tokens.add(new WCSSLeftParenthesisTokenImpl(ref));
    } else {
      tokens.add(new WCSSRightParenthesisTokenImpl(ref));
    }
    offset++;
  }

  private void consumeSquareBracketToken(boolean left) {
    val start = getRowCol(offset);
    val end = getRowCol(offset + 1);
    val ref = new WCReferenceRangeImpl(path, offset, 1, start, end);
    if (left) {
      tokens.add(new WCSSLeftSquareBracketTokenImpl(ref));
    } else {
      tokens.add(new WCSSRightSquareBracketTokenImpl(ref));
    }
    offset++;
  }

  private void consumeCurlyBracketToken(boolean left) {
    val start = getRowCol(offset);
    val end = getRowCol(offset + 1);
    val ref = new WCReferenceRangeImpl(path, offset, 1, start, end);
    if (left) {
      tokens.add(new WCSSLeftCurlyBracketTokenImpl(ref));
    } else {
      tokens.add(new WCSSRightCurlyBracketTokenImpl(ref));
    }
    offset++;
  }

  private void consumeCommaToken() {
    val start = getRowCol(offset);
    val end = getRowCol(offset + 1);
    val ref = new WCReferenceRangeImpl(path, offset, 1, start, end);
    tokens.add(new WCSSCommaTokenImpl(ref));
    offset++;
  }

  private void consumeColonToken() {
    val start = getRowCol(offset);
    val end = getRowCol(offset + 1);
    val ref = new WCReferenceRangeImpl(path, offset, 1, start, end);
    tokens.add(new WCSSColonTokenImpl(ref));
    offset++;
  }

  private void consumeSemicolonToken() {
    val start = getRowCol(offset);
    val end = getRowCol(offset + 1);
    val ref = new WCReferenceRangeImpl(path, offset, 1, start, end);
    tokens.add(new WCSSSemicolonTokenImpl(ref));
    offset++;
  }

  private void consumeWhitespace() {
    int offset = this.offset;
    val start = getRowCol(offset);
    val builder = new StringBuilder();
    char curr;
    do {
      curr = contents.charAt(offset);
      if (!isWhitespaceChar(offset)) break;
      builder.append(curr);
      offset++;
    } while (offset < len);
    if (builder.isEmpty()) return;
    val value = builder.toString();
    val len = value.length();
    val end = getRowCol(offset);
    val ref = new WCReferenceRangeImpl(path, offset, len, start, end);
    tokens.add(new WCSSWhiteSpaceTokenImpl(ref, value));
    this.offset += len;
  }

  private void consumeAtToken() {
    val start = getRowCol(offset);
    if (isIdentStartSequence(offset + 1)) {
      val identitySequence = getIdentitySequence(offset + 1);
      val len = identitySequence.length() + 1;
      val end = getRowCol(offset + identitySequence.length());
      val ref = new WCReferenceRangeImpl(path, offset, len, start, end);
      tokens.add(new WCSSAtKeywordTokenImpl(ref, identitySequence));
      this.offset += len;
    } else {
      consumeDelimiterToken("@");
    }
  }

  private void consumeDotToken() {
    if (isNumericChar(contents.charAt(offset + 1))) {
      offset++;
      consumeNumericToken(null);
      val next = contents.charAt(offset);
      if (isIdentStartCodePoint(next) && isIdentStartCodePoint(contents.charAt(offset + 1))) {
        consumeDimensionToken();
      } else if (next == '%') {
        consumePercentageToken();
      }
    } else if (isIdentStartSequence(offset + 1)) {
      val identitySequence = getIdentitySequence(offset + 1);
      int len = identitySequence.length();
      val ref = new WCReferenceRangeImpl(path, offset, 1, getRowCol(offset), getRowCol(offset + 1));
      val refIS =
          new WCReferenceRangeImpl(
              path, offset, len, getRowCol(offset + 1), getRowCol(offset + 1 + len));
      tokens.add(new WCSSDotTokenImpl(ref));
      tokens.add(new WCSSIdentTokenImpl(refIS, identitySequence));
      this.offset += len + 1;
    } else {
      consumeDelimiterToken(".");
    }
  }

  private void consumeHashToken() {
    if (isHashCharacter(offset + 1)) {
      val hashSequence = getHashSequence(offset + 1);
      int len = hashSequence.length();
      val ref = new WCReferenceRangeImpl(path, offset, 1, getRowCol(offset), getRowCol(offset + 1));
      val refHS =
          new WCReferenceRangeImpl(
              path, offset, len, getRowCol(offset + 1), getRowCol(offset + 1 + len));
      tokens.add(new WCSSHashTokenImpl(ref));
      tokens.add(new WCSSHashColorTokenImpl(refHS, hashSequence));
      this.offset += len + 1;
    } else if (isIdentStartSequence(offset + 1)) {
      val identitySequence = getIdentitySequence(offset + 1);
      int len = identitySequence.length();
      val ref = new WCReferenceRangeImpl(path, offset, 1, getRowCol(offset), getRowCol(offset + 1));
      val refIS =
          new WCReferenceRangeImpl(
              path, offset, len, getRowCol(offset + 1), getRowCol(offset + 1 + len));
      tokens.add(new WCSSHashTokenImpl(ref));
      tokens.add(new WCSSIdentTokenImpl(refIS, identitySequence));
      this.offset += len + 1;
    } else {
      consumeDelimiterToken("#");
    }
  }

  private boolean isHashCharacter(int offset) {
    return REGEX_HEX_DIGIT.matcher("" + contents.charAt(offset)).matches();
  }

  @NotNull
  private String getHashSequence(int offset) {
    val builder = new StringBuilder();
    char curr;
    do {
      curr = contents.charAt(offset);
      if (!isHashCharacter(offset)) break;
      builder.append(curr);
      offset++;
    } while (offset < len && isHashCharacter(offset));
    return builder.toString();
  }

  @NotNull
  private String getNumericSequence(int offset) {
    val builder = new StringBuilder();
    boolean hasDot = false;
    boolean hasE = false;
    char curr;
    do {
      curr = contents.charAt(offset);
      if (!hasE && !hasDot && curr == '.') {
        char next = contents.charAt(offset + 1);
        if (!isNumericChar(next)) break;
        hasDot = true;
        builder.append(curr);
        offset++;
        continue;
      } else if (!hasE && (curr == 'E' || curr == 'e')) {
        hasE = true;
        char next = contents.charAt(offset + 1);
        if (next == '+' || next == '-') {
          char next2 = contents.charAt(offset + 2);
          if (!isNumericChar(next2)) break;
          builder.append(next).append(next2);
          offset += 2;
          continue;
        }
        if (!isNumericChar(next)) break;
        builder.append(curr);
        offset++;
        continue;
      } else if (!isNumericChar(curr)) {
        break;
      }
      builder.append(curr);
      offset++;
    } while (offset < len);
    return builder.toString();
  }

  @NotNull
  private String getIdentitySequence(int offset) {
    val builder = new StringBuilder();
    char curr;
    do {
      curr = contents.charAt(offset);
      if (!isIdentCodePoint(curr)) {
        break;
      }
      builder.append(curr);
      offset++;
    } while (offset < len);
    return builder.toString();
  }

  private void consumeIdentityToken() {
    int offset = this.offset;
    val builder = new StringBuilder();
    val start = getRowCol(offset);
    char curr;
    do {
      curr = contents.charAt(offset);
      if (!isIdentCodePoint(curr)) {
        break;
      }
      builder.append(curr);
      offset++;
    } while (offset < len);
    offset--;
    val end = getRowCol(offset);
    val value = builder.toString();
    val len = value.length();
    val ref = new WCReferenceRangeImpl(path, offset, len, start, end);
    tokens.add(new WCSSIdentTokenImpl(ref, value));
    this.offset += len;
  }

  private void consumeStringToken(char casing) {
    int offset = this.offset;
    val builder = new StringBuilder().append(casing);
    val start = getRowCol(offset++);
    boolean backslashLast;
    boolean backslash = false;
    boolean bad = false;
    char curr;
    do {
      curr = contents.charAt(offset++);
      builder.append(curr);
      backslashLast = backslash;
      backslash = curr == '\\';

      if (!backslashLast && (curr == '\r' || curr == '\n')) {
        bad = true;
        break;
      }

    } while (backslashLast || curr != casing);

    val contents = builder.toString();
    val len = contents.length();
    val end = getRowCol(offset);
    val ref = new WCReferenceRangeImpl(path, offset, len, start, end);
    WCSSStringToken token = new WCSSStringTokenImpl(ref, contents, casing, bad);
    tokens.add(token);
    this.offset += len;
  }

  private boolean isValidEscapeSequence(int offset) {
    char curr = contents.charAt(offset);
    if (curr == '\\') {
      char curr1 = contents.charAt(offset + 1);
      return curr1 == '\\';
    }
    return false;
  }

  private boolean isIdentStartSequence(final int offset) {

    char c0 = contents.charAt(offset);

    //  If a U+002D HYPHEN-MINUS, or the second and third code points are a valid escape, return
    // true.
    if (c0 == '-') {
      char c1 = contents.charAt(offset + 1);
      if (c1 == '-') {
        return true;
      } else if (isValidEscapeSequence(offset + 1)) {
        return true;
      }
      // If the second code point is an ident-start code point return true.
      else return isIdentStartCodePoint(c1);
    }

    // A letter, a non-ASCII code point, or U+005F LOW LINE (_)
    else if (isIdentStartCodePoint(c0)) {
      return true;
    }

    // If the first and second code points are a valid escape, return true.
    else return isValidEscapeSequence(offset);
  }

  private int getNextNewlineIndex(int index) {
    if (rowIndexes.length <= 1 || len == 0) return -1;
    for (int next : rowIndexes) {
      if (index < next) return next;
    }
    throw new RuntimeException("There be dragons.");
  }

  public int[] getRowCol(int offset) {
    return getRowCol(rowIndexes, offset);
  }

  public void debug() {
    System.out.println("Comments: [" + comments.size() + "]");
    for (WCSSCommentBlockToken token : comments) {
      System.out.println(token);
    }
    System.out.println("\nTokens: [" + tokens.size() + "]");
    for (WCSSToken token : tokens) {
      if (token.getType() == CSSTokenType.WHITESPACE) {
        continue;
      }
      System.out.println(token);
    }
  }

  private boolean isWhitespaceChar(int index) {
    return switch (contents.charAt(index)) {
      case '\f', '\n', '\r', '\t', ' ' -> true;
      default -> false;
    };
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

  public static int[] getRowCol(int[] rows, int index) {
    if (index < 0) {
      throw new IllegalArgumentException("The index out of bounds!");
    }
    int[] result = null;
    // No rows.
    if (rows.length == 0) return BLANK_ROW_COL;
    else if (rows.length == 1 || rows[0] > index) {
      // First OR only line
      result = new int[] {1, index + 1};
    } else {
      for (int row = 0; row < rows.length - 1; row++) {
        int rowIndexCurr = rows[row];
        int rowIndexNext = rows[row + 1];
        if (index < rowIndexNext) {
          result = new int[] {row + 2, (index - rowIndexCurr) + 1};
          break;
        }
      }
      // Last line.
      if (result == null) {
        result = new int[] {rows.length, (index - rows[rows.length - 1]) + 1};
      }
    }
    if (result[1] < 1) {
      throw new RuntimeException(
          "The index out of bounds!" + "(index: " + index + ", col: " + result[1] + ")");
    }
    return result;
  }

  private static int[] getRowIndexes(@NotNull String contents) {
    final List<Integer> list = new ArrayList<>();
    val matcher = PATTERN_NEWLINE.matcher(contents);
    while (matcher.find()) {
      list.add(matcher.end());
    }
    val indexes = new int[list.size()];
    for (int i = 0; i < list.size(); i++) {
      indexes[i] = list.get(i);
    }
    return indexes;
  }

  private static boolean isIdentStartCodePoint(char c) {
    return REGEX_IDENT_START_CODE_POINT.matcher("" + c).matches() || isNonASCIICodePoint(c);
  }

  private static boolean isIdentCodePoint(char c) {
    return REGEX_IDENT_CODE_POINT.matcher("" + c).matches() || isNonASCIICodePoint(c);
  }

  private static boolean isNonASCIICodePoint(char c) {
    // non-ASCII code point: A code point with a value equal to or greater than U+0080 <control>.
    return c > 0x0080;
  }
}
