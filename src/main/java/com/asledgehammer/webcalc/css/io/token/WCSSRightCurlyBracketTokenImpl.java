package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.NonNull;

public class WCSSRightCurlyBracketTokenImpl extends WCSSTokenImpl implements WCSSToken {
  public WCSSRightCurlyBracketTokenImpl(@NonNull WCReferenceRangeImpl ref) {
    super(CSSTokenType.RIGHT_CURLYBRACKET, ref, "}", false, false);
  }
}
