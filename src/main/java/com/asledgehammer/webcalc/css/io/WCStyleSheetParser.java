package com.asledgehammer.webcalc.css.io;

import com.asledgehammer.webcalc.css.io.lexer.Marcher;
import com.asledgehammer.webcalc.css.io.lexer.WCStyleSheetLexer;
import com.asledgehammer.webcalc.css.sheet.WCStyleSheet;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WCStyleSheetParser {

    private static final Pattern PATTERN_NEWLINE = Pattern.compile("(\n|\r\n|\r|\f)");

    public static int[] getRowIndexesFromString(@NonNull String contents) {
        List<Integer> list = new ArrayList<>();

        Matcher matcher = PATTERN_NEWLINE.matcher(contents);
        while (matcher.find()) {
            System.out.println("Found at index: " + matcher.start() + " to " + matcher.end());
            list.add(matcher.end());
        }

        int[] indexes = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            indexes[i] = list.get(i);
        }
        return indexes;
    }

    public WCStyleSheet parse(String contents) {
        return null;
    }

    static void main(String[] args) {
        final File file = new File("./src/test/resources/test/blank.css");
        WCStyleSheetLexer.lex(file);
    }
}
