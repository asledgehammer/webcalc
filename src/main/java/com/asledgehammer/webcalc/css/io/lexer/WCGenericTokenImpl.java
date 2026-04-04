package com.asledgehammer.webcalc.css.io.lexer;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import com.asledgehammer.webcalc.css.io.lexer.token.WCReferencedToken;
import com.asledgehammer.webcalc.css.io.lexer.token.WCReferencedTokenImpl;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

class WCGenericTokenImpl extends WCReferencedTokenImpl implements WCReferencedToken {
  WCGenericTokenImpl(@NotNull WCReferenceRange reference, @NonNull String contents) {
    super(reference, contents, true);
  }

  @Override
  public String toString() {
    return "\"" + getContents() + "\"";
  }

}
