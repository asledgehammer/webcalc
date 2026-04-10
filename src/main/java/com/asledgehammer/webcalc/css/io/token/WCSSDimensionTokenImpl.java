package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.NonNull;

public class WCSSDimensionTokenImpl extends WCSSTokenImpl implements WCSSToken {
  public WCSSDimensionTokenImpl(@NonNull WCReferenceRangeImpl ref, @NonNull String value) {
    super(CSSTokenType.DIMENSION, ref, value, false, false);
  }
}
