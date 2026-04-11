package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.NonNull;

public class WCSSHashColorTokenImpl extends WCSSTokenImpl implements WCSSToken {
  public WCSSHashColorTokenImpl(@NonNull WCReferenceRangeImpl ref, @NonNull String contents) {
    super(CSSTokenType.HEX_COLOR, ref, contents, false, false);
  }
}
