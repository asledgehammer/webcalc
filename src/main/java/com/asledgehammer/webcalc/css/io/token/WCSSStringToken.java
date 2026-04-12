package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class WCSSStringToken extends WCSSToken {

  @Getter private final @NotNull String value;
  @Getter private final boolean bad;
  @Getter private final char enclosingCharacter;

  public WCSSStringToken(
      @NonNull WCReferenceRange reference,
      @NonNull String contents,
      char enclosingCharacter,
      boolean bad) {
    super(CSSTokenType.STRING, reference, contents);
    this.enclosingCharacter = enclosingCharacter;
    this.bad = bad;
    this.value = contents.substring(1, contents.length() - 1);
  }
}
