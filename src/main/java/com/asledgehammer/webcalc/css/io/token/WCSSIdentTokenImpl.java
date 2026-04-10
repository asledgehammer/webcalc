package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.NonNull;

public class WCSSIdentTokenImpl extends WCSSTokenImpl implements WCSSIdentToken {

  public WCSSIdentTokenImpl(@NonNull WCReferenceRange reference, @NonNull String contents) {
    super(CSSTokenType.IDENTITY, reference, contents, false, false);
  }
}
