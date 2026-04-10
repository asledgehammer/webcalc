package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.NonNull;

public class WCSSSemicolonTokenImpl extends WCSSTokenImpl implements WCSSToken {
  public WCSSSemicolonTokenImpl(@NonNull WCReferenceRangeImpl ref) {
    super(CSSTokenType.SEMICOLON, ref, ";", false, false);
  }
}
