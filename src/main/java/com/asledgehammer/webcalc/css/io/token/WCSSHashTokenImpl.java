package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class WCSSHashTokenImpl extends WCSSTokenImpl implements WCSSHashToken {

  public WCSSHashTokenImpl(@NonNull WCReferenceRange reference) {
    super(CSSTokenType.HASH, reference, "#", false, false);
  }

}
