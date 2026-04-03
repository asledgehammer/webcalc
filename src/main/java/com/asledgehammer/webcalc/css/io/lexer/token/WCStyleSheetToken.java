package com.asledgehammer.webcalc.css.io.lexer.token;

import com.asledgehammer.webcalc.css.io.WCFileReferenceBlock;
import lombok.NonNull;

public interface WCStyleSheetToken {
    @NonNull
    WCFileReferenceBlock getFileReference();

    @NonNull
    String getRaw();
}
