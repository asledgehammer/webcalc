package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.Getter;
import lombok.NonNull;

public class WCSSAtKeywordToken extends WCSSToken {

  @Getter private final @NonNull String identitySequence;

  public WCSSAtKeywordToken(
          @NonNull WCReferenceRange ref, @NonNull String identitySequence) {
    super(CSSTokenType.AT_KEYWORD, ref, "@" + identitySequence);
    this.identitySequence = identitySequence;
  }
}
