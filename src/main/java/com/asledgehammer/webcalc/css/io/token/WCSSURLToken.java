package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.NonNull;

public class WCSSURLToken extends WCSSToken {
  private final String url;

  public WCSSURLToken(
      @NonNull WCReferenceRange reference, final @NonNull String url, final boolean bad) {
    super(CSSTokenType.URL, reference, "url(" + url + ")", bad);
    this.url = url;
  }

  @NonNull
  public String getURL() {
    return url;
  }
}
