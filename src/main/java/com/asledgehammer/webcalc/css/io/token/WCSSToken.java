package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferencedToken;
import org.jetbrains.annotations.NotNull;

public interface WCSSToken extends WCReferencedToken {
  @NotNull
  CSSTokenType getType();

  boolean isWhiteSpace();
}
