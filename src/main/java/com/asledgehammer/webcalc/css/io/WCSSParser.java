package com.asledgehammer.webcalc.css.io;

import com.asledgehammer.webcalc.css.sheet.WCStyleSheet;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class WCSSParser {

  public WCStyleSheet parse(String contents) {
    return null;
  }

  public static void parse(@NonNull File file) {
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
    parse(path, contents);
  }

  private static void parse(@Nullable URL path, @NonNull String contents) {
    WCSSLexer lexer = new WCSSLexer(path, contents);
    lexer.run();
    lexer.debug();
  }

  static void main(String[] args) {
    final File file = new File("./src/test/resources/test/blank.css");
    parse(file);
  }
}
