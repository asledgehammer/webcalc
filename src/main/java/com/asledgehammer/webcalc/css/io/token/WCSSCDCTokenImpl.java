package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.NonNull;

public class WCSSCDCTokenImpl extends WCSSTokenImpl implements WCSSToken {
  public WCSSCDCTokenImpl(@NonNull WCReferenceRangeImpl ref) {
    super(CSSTokenType.CDC, ref, "-->", false, false);
  }
}
