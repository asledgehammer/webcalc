package com.asledgehammer.webcalc.css;

import java.util.HexFormat;
import java.util.regex.Pattern;

public class CSSUtils {

  public static final Pattern PATTERN_NEWLINE = Pattern.compile("(\n|\r\n|\r|\f)");
  public static final Pattern PATTERN_HEX_DIGITS =
      Pattern.compile("[A-Fa-f0-9]", Pattern.CASE_INSENSITIVE);

  public static boolean isHexDigit(char c) {
    return HexFormat.isHexDigit(c);
  }

  public static boolean isHexDigits(String c) {
    return PATTERN_HEX_DIGITS.matcher(c).find();
  }

  static void main() {
    System.out.println(isHexDigits("FFF"));
    System.out.println(isHexDigit('G'));
  }
}
