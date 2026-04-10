package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.NonNull;

public class WCSSNumericTokenImpl extends WCSSTokenImpl implements WCSSNumericToken {
  public WCSSNumericTokenImpl(@NonNull WCReferenceRangeImpl ref, @NonNull String value) {
    super(CSSTokenType.NUMERIC, ref, value, false, false);
  }
}
