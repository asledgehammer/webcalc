package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.NonNull;

public class WCSSRightSquareBracketTokenImpl extends WCSSTokenImpl implements WCSSToken {
  public WCSSRightSquareBracketTokenImpl(@NonNull WCReferenceRangeImpl ref) {
    super(CSSTokenType.RIGHT_SQUAREBRACKET, ref, "]", false, false);
  }
}
