package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.NonNull;

public class WCSSColonTokenImpl extends WCSSTokenImpl implements WCSSToken {
  public WCSSColonTokenImpl(@NonNull WCReferenceRangeImpl ref) {
    super(CSSTokenType.COLON, ref, ":", false, false);
  }
}
