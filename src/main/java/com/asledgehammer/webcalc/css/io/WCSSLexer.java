package com.asledgehammer.webcalc.css.io;

import com.asledgehammer.webcalc.css.io.token.*;
import com.asledgehammer.webcalc.io.WCParseError;
import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class WCSSLexer implements Runnable {

  private static final Pattern PATTERN_NEWLINE = Pattern.compile("(\n|\r\n|\r|\f)");
  private static final Pattern REGEX_HEX_DIGIT = Pattern.compile("[0-9A-Fa-f]");
  private static final Pattern REGEX_IDENT_CODE_POINT = Pattern.compile("[A-Za-z0-9_\\-]");
  private static final Pattern REGEX_IDENT_START_CODE_POINT = Pattern.compile("[A-Za-z_]");
  private static final int[] BLANK_ROW_COL = new int[] {1, 1};

  @Getter private final List<WCSSCommentBlockToken> comments = new ArrayList<>();
  @Getter private final List<WCSSToken> tokens = new ArrayList<>();
  @Getter private final List<WCParseError> errors = new ArrayList<>();
  @Getter private final String contents;
  @Getter private final @Nullable URL path;

  private final List<int[]> rangesIgnored = new ArrayList<>();
  private final int[] rowIndexes;
  private int offset = 0;
  private final int len;
  private boolean ran;

  public WCSSLexer(@Nullable URL path, @NonNull final String contents) {
    if (contents.isEmpty()) {
      throw new IllegalArgumentException("contents must not be empty");
    }
    this.path = path;
    this.contents = contents;
    this.len = contents.length();
    this.rowIndexes = getRowIndexes(contents);
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

  @Override
  public void run() {
    if (ran) return;
    ran = true;
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
        val ref = new WCReferenceRange(path, indexStart, comment.length(), start, end);
        errors.add(new WCParseError(ref, "Unclosed comment block"));
        comments.add(new WCSSCommentBlockToken(ref, comment));
        break;
      }

      // Retain outer raw indexed range.
      indexEnd += 2;
      rangesIgnored.add(new int[] {indexStart, indexEnd});
      val comment = contents.substring(indexStart, indexEnd);
      val start = getRowCol(indexStart);
      val end = getRowCol(indexEnd);
      val ref = new WCReferenceRange(path, indexStart, comment.length(), start, end);
      comments.add(new WCSSCommentBlockToken(ref, comment));
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
        consumeToken(CSSTokenType.LEFT_PARENTHESIS, "(");
      } else if (curr == ')') {
        consumeToken(CSSTokenType.RIGHT_PARENTHESIS, ")");
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
        consumeToken(CSSTokenType.COMMA, ",");
      } else if (curr == '-') {

        if (contents.indexOf("-->") == offset) {
          consumeToken(CSSTokenType.CDO, "-->");
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
        consumeToken(CSSTokenType.COLON, ":");
      } else if (curr == ';') {
        consumeToken(CSSTokenType.SEMICOLON, ";");
      } else if (curr == '<') {
        if (contents.indexOf("<!--") == offset) {
          consumeToken(CSSTokenType.CDO, "<!--");
        } else {
          consumeDelimiterToken("<");
        }
      } else if (curr == '@') {
        consumeAtToken();
      } else if (curr == '[') {
        consumeToken(CSSTokenType.LEFT_SQUAREBRACKET, "[");
      } else if (curr == '\\') {
        // I think this is only a parse error at this point??
        consumeDelimiterToken("\\");
      } else if (curr == ']') {
        consumeToken(CSSTokenType.RIGHT_SQUAREBRACKET, "]");
      } else if (curr == '{') {
        consumeToken(CSSTokenType.LEFT_CURLYBRACKET, "{");
      } else if (curr == '}') {
        consumeToken(CSSTokenType.RIGHT_CURLYBRACKET, "}");
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
          val ref = new WCReferenceRange(path, offset, len + 1, start, end);
          tokens.add(new WCSSFunctionToken(ref, leading));
          offset += len + 1;
          return;
        }
        offset += len + 1;
        consumeUrlToken();
        return;
      }
    } else if (curr == '(') {
      val end = getRowCol(offset + len + 1);
      val ref = new WCReferenceRange(path, offset, len + 1, start, end);
      tokens.add(new WCSSFunctionToken(ref, leading));
      offset += len + 1;
      return;
    }

    // No <function-token>, <url-token> or <bad-url-token>
    val end = getRowCol(offset + len);
    val ref = new WCReferenceRange(path, offset, len, start, end);
    tokens.add(new WCSSToken(CSSTokenType.IDENTITY, ref, leading));
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
      val ref = new WCReferenceRange(path, offsetStart, len + 4, start, end);
      tokens.add(new WCSSURLToken(ref, value, true));
      offset = this.len;
      return;
    }

    offset++; // Skip ')'
    val len = value.length();
    val end = getRowCol(offset + 1);
    val ref = new WCReferenceRange(path, offsetStart, len + 4, start, end);
    tokens.add(new WCSSURLToken(ref, value, bad));
  }

  private void consumeToken(@NonNull CSSTokenType type, @NonNull String contents) {
    val len = contents.length();
    val start = getRowCol(offset);
    val end = getRowCol(offset + len);
    val ref = new WCReferenceRange(path, offset, len, start, end);
    tokens.add(new WCSSToken(type, ref, contents));
    offset += len;
  }

  private void consumePercentageToken() {
    consumeToken(CSSTokenType.PERCENTAGE, "%");
  }

  private void consumeDimensionToken() {
    consumeToken(CSSTokenType.DIMENSION, getIdentitySequence(offset));
  }

  private void consumeNumericToken(@Nullable Character prepend) {
    consumeToken(CSSTokenType.NUMERIC, getNumericSequence(offset));
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
    val ref = new WCReferenceRange(path, offset, len, start, end);
    tokens.add(new WCSSToken(CSSTokenType.WHITESPACE, ref, value));
    this.offset += len;
  }

  private void consumeAtToken() {
    if (isIdentStartSequence(offset + 1)) {
      consumeToken(CSSTokenType.AT, "@");
      consumeIdentityToken();
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
      val ref = new WCReferenceRange(path, offset, 1, getRowCol(offset), getRowCol(offset + 1));
      val refIS =
          new WCReferenceRange(
              path, offset, len, getRowCol(offset + 1), getRowCol(offset + 1 + len));
      tokens.add(new WCSSToken(CSSTokenType.DOT, ref, "."));
      tokens.add(new WCSSToken(CSSTokenType.IDENTITY, refIS, identitySequence));
      this.offset += len + 1;
    } else {
      consumeDelimiterToken(".");
    }
  }

  private void consumeHashToken() {
    if (isHashCharacter(offset + 1)) {
      val hashSequence = getHashSequence(offset + 1);
      int len = hashSequence.length();
      val ref = new WCReferenceRange(path, offset, 1, getRowCol(offset), getRowCol(offset + 1));
      val refHS =
          new WCReferenceRange(
              path, offset, len, getRowCol(offset + 1), getRowCol(offset + 1 + len));
      tokens.add(new WCSSToken(CSSTokenType.HASH, ref, "#"));
      tokens.add(new WCSSToken(CSSTokenType.HEX_COLOR, refHS, hashSequence));
      this.offset += len + 1;
    } else if (isIdentStartSequence(offset + 1)) {
      val identitySequence = getIdentitySequence(offset + 1);
      int len = identitySequence.length();
      val ref = new WCReferenceRange(path, offset, 1, getRowCol(offset), getRowCol(offset + 1));
      val refIS =
          new WCReferenceRange(
              path, offset, len, getRowCol(offset + 1), getRowCol(offset + 1 + len));
      tokens.add(new WCSSToken(CSSTokenType.HASH, ref, "#"));
      tokens.add(new WCSSToken(CSSTokenType.IDENTITY, refIS, identitySequence));
      this.offset += len + 1;
    } else {
      consumeDelimiterToken("#");
    }
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
    val ref = new WCReferenceRange(path, offset, len, start, end);
    tokens.add(new WCSSToken(CSSTokenType.IDENTITY, ref, value));
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
    val ref = new WCReferenceRange(path, offset, len, start, end);
    WCSSStringToken token = new WCSSStringToken(ref, contents, casing, bad);
    tokens.add(token);
    this.offset += len;
  }

  private void consumeDelimiterToken(@NotNull String delimiter) {
    consumeToken(CSSTokenType.DELIMITER, delimiter);
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

  private int[] getRowCol(int offset) {
    return getRowCol(rowIndexes, offset);
  }

  private boolean isNumericChar(char c) {
    return Character.isDigit(c);
  }

  private boolean isNumericStartChar(char c) {
    return c == '.' || Character.isDigit(c);
  }

  private int skipIgnoredIndex(int index) {
    for (final int[] range : this.rangesIgnored) {
      if (range[0] <= index && index <= range[1]) {
        return range[1] + 1;
      }
    }
    return index;
  }

  private boolean isIgnoredIndex(int index) {
    for (final int[] range : this.rangesIgnored) {
      if (range[0] <= index && index <= range[1]) {
        return true;
      }
    }
    return false;
  }

  private boolean isWhitespaceChar(int index) {
    return switch (contents.charAt(index)) {
      case '\f', '\n', '\r', '\t', ' ' -> true;
      default -> false;
    };
  }

  private boolean isHashCharacter(int offset) {
    return REGEX_HEX_DIGIT.matcher("" + contents.charAt(offset)).matches();
  }

  private boolean isEOS() {
    return offset >= len;
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
