package com.asledgehammer.webcalc.css.io;

import com.asledgehammer.webcalc.css.io.lexer.WCStyleSheetLexer;
import com.asledgehammer.webcalc.css.sheet.WCStyleSheet;
import lombok.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WCStyleSheetParser {

    public WCStyleSheet parse(String contents) {
        return null;
    }

    static void main(String[] args) {
        final File file = new File("./src/test/resources/test/blank.css");
        WCStyleSheetLexer.lex(file);
    }
}
