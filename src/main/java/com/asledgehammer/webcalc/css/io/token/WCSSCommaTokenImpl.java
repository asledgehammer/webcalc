package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.NonNull;

public class WCSSCommaTokenImpl extends WCSSTokenImpl implements WCSSToken {
  public WCSSCommaTokenImpl(@NonNull WCReferenceRangeImpl ref) {
    super(CSSTokenType.COMMA, ref, ",", false, false);
  }
}
