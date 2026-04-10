package com.asledgehammer.webcalc.css.io.token;

import lombok.NonNull;

public interface WCSSAtKeywordToken extends WCSSToken {
  @NonNull
  String getIdentitySequence();
}
