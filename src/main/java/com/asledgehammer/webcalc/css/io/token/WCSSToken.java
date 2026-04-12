package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import com.asledgehammer.webcalc.io.token.WCReferencedToken;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public class WCSSToken extends WCReferencedToken {

  @Getter private final @NotNull CSSTokenType type;
  @Getter private final boolean bad;

  public WCSSToken(
      @NonNull CSSTokenType type, @NonNull WCReferenceRange reference, @NonNull String contents) {
    this(type, reference, contents, false);
  }

  public WCSSToken(
      @NonNull CSSTokenType type,
      @NonNull WCReferenceRange reference,
      @NonNull String contents,
      boolean bad) {
    super(reference, contents);
    this.type = type;
    this.bad = bad;
  }

  @Override
  public String toString() {
    return getReference()
        + "["
        + getType().name()
        + "] :: \""
        + getContents()
            .replaceAll("\r\n", "\\\\r\\\\n")
            .replaceAll("\r", "\\\\r")
            .replaceAll("\n", "\\\\n")
            .replaceAll("\t", "\\\\t")
        + "\"";
  }
}
