package com.asledgehammer.webcalc.io.token;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public class WCGenericTokenImpl extends WCReferencedTokenImpl implements WCReferencedToken {
  public WCGenericTokenImpl(@NotNull WCReferenceRange reference, @NonNull String contents) {
    super(reference, contents, true);
  }
}
