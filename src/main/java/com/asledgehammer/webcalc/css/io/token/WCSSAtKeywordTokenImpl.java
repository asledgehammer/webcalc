package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.Getter;
import lombok.NonNull;

public class WCSSAtKeywordTokenImpl extends WCSSTokenImpl implements WCSSAtKeywordToken {

  @Getter private final @NonNull String identitySequence;

  public WCSSAtKeywordTokenImpl(@NonNull WCReferenceRangeImpl ref, @NonNull String identitySequence) {
    super(CSSTokenType.AT_KEYWORD, ref, "@" + identitySequence, false, false);
    this.identitySequence = identitySequence;
  }
}
