package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class WCSSStringTokenImpl extends WCSSTokenImpl implements WCSSStringToken {

  @Getter private final @NotNull String value;
  @Getter private final boolean bad;
  @Getter private final char enclosingCharacter;

  public WCSSStringTokenImpl(
      @NonNull WCReferenceRange reference,
      @NonNull String contents,
      char enclosingCharacter,
      boolean bad) {
    super(CSSTokenType.STRING, reference, contents, false, false);
    this.enclosingCharacter = enclosingCharacter;
    this.bad = bad;
    this.value = contents.substring(1, contents.length() - 1);
  }

  @Override
  public String toString() {
    val enclosingChar = enclosingCharacter;
    return getReference()
        + "[STRING]"
        + (bad ? "[BAD]" : "")
        + " :: "
        + enclosingChar
        + value
        + enclosingChar;
  }
}
