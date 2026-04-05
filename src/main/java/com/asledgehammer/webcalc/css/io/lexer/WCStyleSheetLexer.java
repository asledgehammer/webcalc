package com.asledgehammer.webcalc.css.io.lexer;

import com.asledgehammer.webcalc.css.io.token.WCSSCommentBlockToken;
import com.asledgehammer.webcalc.io.WCParseError;
import com.asledgehammer.webcalc.io.token.WCReferencedToken;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;

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
    List<WCParseError> errors = marcher.getErrors();
    List<WCSSCommentBlockToken> comments = marcher.getComments();
    List<WCReferencedToken> tokens = marcher.getTokens();
    List<int[]> rangesIgnored = marcher.getRangesIgnored();

    if (!errors.isEmpty()) {
      System.out.println("Errors (" + errors.size() + "): ");
      errors.forEach(System.out::println);
    }
    if (!comments.isEmpty()) {
      System.out.println("\n\nComment Block(s) (" + comments.size() + "): ");
      comments.forEach(System.out::println);
    }

    if (!rangesIgnored.isEmpty()) {
      System.out.println("\n\nIgnored Region(s) (" + rangesIgnored.size() + "): ");
      rangesIgnored.forEach(e -> System.out.println(e[0] + " -> " + e[1]));
    }

    if (!tokens.isEmpty()) {
      System.out.println("\n\nToken(s) (" + tokens.size() + "): ");
      tokens.forEach(System.out::println);
    }
  }
}
