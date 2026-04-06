package com.asledgehammer.webcalc.css.io.token;

import org.jetbrains.annotations.NotNull;

public interface WCSSStringToken extends WCSSToken {
  boolean isBad();

  @NotNull
  String getValue();
}
