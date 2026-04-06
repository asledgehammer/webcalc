package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import com.asledgehammer.webcalc.io.token.WCReferencedToken;
import com.asledgehammer.webcalc.io.token.WCReferencedTokenImpl;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public class WCSSGenericTokenImpl extends WCSSTokenImpl {
  public WCSSGenericTokenImpl(@NotNull WCReferenceRange reference, @NonNull String contents) {
    super(CSSTokenType.GENERIC, reference, contents, true, false);
  }
}
