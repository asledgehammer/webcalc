package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.NonNull;

public class WCSSDotTokenImpl extends WCSSTokenImpl implements WCSSToken {

  public WCSSDotTokenImpl(@NonNull WCReferenceRange reference) {
    super(CSSTokenType.DOT, reference, ".", false, false);
  }
}
