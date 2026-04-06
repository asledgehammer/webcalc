package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.NonNull;

public class WCSSWhiteSpaceTokenImpl extends WCSSTokenImpl implements WCSSWhiteSpaceToken {
  public WCSSWhiteSpaceTokenImpl(@NonNull WCReferenceRange reference, @NonNull String contents) {
    super(CSSTokenType.WHITESPACE, reference, contents, false, true);
  }
}
