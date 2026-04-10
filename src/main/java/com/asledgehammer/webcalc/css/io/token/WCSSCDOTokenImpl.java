package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.NonNull;

public class WCSSCDOTokenImpl extends WCSSTokenImpl implements WCSSToken {
  public WCSSCDOTokenImpl(@NonNull WCReferenceRangeImpl ref) {
    super(CSSTokenType.CDO, ref, "<!--", false, false);
  }
}
