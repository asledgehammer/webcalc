package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;

public class WCSSLeftParenthesisTokenImpl extends WCSSTokenImpl implements WCSSToken {
  public WCSSLeftParenthesisTokenImpl(WCReferenceRangeImpl ref) {
    super(CSSTokenType.LEFT_PARENTHESIS, ref, "(", false, false);
  }
}
