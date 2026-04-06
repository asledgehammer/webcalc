package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import com.asledgehammer.webcalc.io.token.WCReferencedTokenImpl;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public class WCSSTokenImpl extends WCReferencedTokenImpl implements WCSSToken {

  @Getter private final @NotNull CSSTokenType type;
  private final boolean whitespace;

  protected WCSSTokenImpl(
      @NonNull CSSTokenType type,
      @NonNull WCReferenceRange reference,
      @NonNull String contents,
      boolean generic,
      boolean whitespace) {
    super(reference, contents, generic);
    this.type = type;
    this.whitespace = whitespace;
  }

  @Override
  public boolean isWhiteSpace() {
    return whitespace;
  }
}
