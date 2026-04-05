package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import com.asledgehammer.webcalc.io.token.WCReferencedTokenImpl;
import lombok.NonNull;

public class WCSSTokenImpl extends WCReferencedTokenImpl implements WCSSToken {
  protected WCSSTokenImpl(
      @NonNull WCReferenceRange reference, @NonNull String contents, boolean generic) {
    super(reference, contents, generic);
  }
}
