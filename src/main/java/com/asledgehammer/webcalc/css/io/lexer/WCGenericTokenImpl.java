package com.asledgehammer.webcalc.css.io.lexer;

import com.asledgehammer.webcalc.css.io.WCFileReferenceBlock;
import com.asledgehammer.webcalc.css.io.lexer.token.WCStyleSheetToken;
import com.asledgehammer.webcalc.css.io.lexer.token.WCStyleSheetTokenImpl;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

class WCGenericTokenImpl extends WCStyleSheetTokenImpl implements WCStyleSheetToken {
  WCGenericTokenImpl(@NotNull WCFileReferenceBlock reference, @NonNull String contents) {
    super(reference, contents);
  }

  @Override
  public String toString() {
    return "\"" + getRaw() + "\"";
  }
}
