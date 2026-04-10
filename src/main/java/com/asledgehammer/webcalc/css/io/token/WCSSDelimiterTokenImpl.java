package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.NonNull;

public class WCSSDelimiterTokenImpl extends WCSSTokenImpl implements WCSSDelimiterToken {

  public WCSSDelimiterTokenImpl(@NonNull WCReferenceRange reference, @NonNull String contents) {
    super(CSSTokenType.DELIMITER, reference, contents, false, false);
  }
}
