package com.asledgehammer.webcalc.css.io.lexer;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class WCStyleSheetLexer {

  private static final Pattern PATTERN_NEWLINE = Pattern.compile("(\n|\r\n|\r|\f)");

  public static int[] getRowIndexesFromString(@NonNull String contents) {
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

  public static void lex(@NonNull File file) {
    String contents = "";
    URL path = null;
    try {
      path = file.toURI().toURL();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      contents = reader.readAllAsString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    lex(path, contents);
  }

  private static void lex(@Nullable URL path, String contents) {
    Marcher marcher = new Marcher(path, contents);
    marcher.march();
    System.out.println(marcher.tokens);
  }
}
