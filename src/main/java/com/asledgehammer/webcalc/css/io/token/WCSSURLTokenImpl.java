package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.NonNull;

public class WCSSURLTokenImpl extends WCSSTokenImpl implements WCSSUrlToken {
  private final String url;
  private final boolean bad;

  public WCSSURLTokenImpl(
      @NonNull WCReferenceRange reference, final @NonNull String url, final boolean bad) {
    super(CSSTokenType.URL, reference, "url(" + url + ")", false, false);
    this.url = url;
    this.bad = bad;
  }

  @NonNull
  @Override
  public String getURL() {
    return url;
  }

  @Override
  public boolean isBad() {
    return bad;
  }
}
