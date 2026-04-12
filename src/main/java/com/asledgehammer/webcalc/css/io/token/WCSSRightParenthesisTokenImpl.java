package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;

public class WCSSRightParenthesisTokenImpl extends WCSSTokenImpl implements WCSSToken {
  public WCSSRightParenthesisTokenImpl(WCReferenceRangeImpl ref) {
    super(CSSTokenType.RIGHT_PARENTHESIS, ref, ")", false, false);
  }
}
