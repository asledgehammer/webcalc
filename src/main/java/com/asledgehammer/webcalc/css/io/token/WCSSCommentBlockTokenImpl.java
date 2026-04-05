package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.NonNull;

public class WCSSCommentBlockTokenImpl extends WCSSTokenImpl
    implements WCSSCommentBlockToken {

  public WCSSCommentBlockTokenImpl(
      @NonNull WCReferenceRange reference, @NonNull String contents) {
    super(reference, contents, false);
  }

  @Override
  public String toString() {
    return getReference() + " :: " + getContents();
  }
}
