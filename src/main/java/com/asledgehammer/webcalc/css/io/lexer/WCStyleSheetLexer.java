package com.asledgehammer.webcalc.css.io.lexer;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class WCStyleSheetLexer {

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
    WCStyleSheetMarcher marcher = new WCStyleSheetMarcher(path, contents);
    marcher.march();
    System.out.println(marcher.tokens);
  }
}
