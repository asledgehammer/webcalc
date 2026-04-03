package com.asledgehammer.webcalc.css.io.lexer;

import com.asledgehammer.webcalc.css.io.lexer.token.WCStyleSheetToken;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WCStyleSheetLexer {

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

    public static int getRow(int[] indexes, int offset) {
        if (indexes == null) {
            throw new IllegalArgumentException("indexes cannot be null", new NullPointerException());
        } else if (offset < 0) {
            throw new IllegalArgumentException("The index out of bounds!");
        } else if (indexes.length == 0) {
            throw new IllegalArgumentException("No indexes!");
        }
        for (int row = 0; row < indexes.length - 1; row++) {
            int rowIndexCurr = indexes[row];
            int rowIndexNext = indexes[row + 1];
            if (rowIndexCurr <= offset && rowIndexNext > offset) return row;
        }
        throw new IllegalArgumentException("The index out of bounds!");
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
