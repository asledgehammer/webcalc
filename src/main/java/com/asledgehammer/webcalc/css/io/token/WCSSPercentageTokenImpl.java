package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.NonNull;

public class WCSSPercentageTokenImpl extends WCSSTokenImpl implements WCSSToken {
  public WCSSPercentageTokenImpl(@NonNull WCReferenceRangeImpl ref) {
    super(CSSTokenType.PERCENTAGE, ref, "%", false, false);
  }
}
