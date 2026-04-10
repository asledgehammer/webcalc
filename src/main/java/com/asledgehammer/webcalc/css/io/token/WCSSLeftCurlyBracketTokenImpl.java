package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.NonNull;

public class WCSSLeftCurlyBracketTokenImpl extends WCSSTokenImpl implements WCSSToken {
  public WCSSLeftCurlyBracketTokenImpl(@NonNull WCReferenceRangeImpl ref) {
    super(CSSTokenType.LEFT_SQUAREBRACKET, ref, "{", false, false);
  }
}
