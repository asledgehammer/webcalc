package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public class WCSSCommentBlockToken extends WCSSToken {

  @Getter private final @NotNull String comment;

  public WCSSCommentBlockToken(@NonNull WCReferenceRange reference, @NonNull String contents) {
    super(CSSTokenType.COMMENT, reference, contents);

    // Remove the '/*' and '*/' casing.
    String comment = contents.substring(2);
    if (comment.endsWith("*/")) {
      comment = comment.substring(0, comment.length() - 2);
    }
    this.comment = comment.trim();
  }

  @Override
  public String toString() {
    return getReference()
        + "[BLOCK_COMMENT] :: \""
        + getContents()
            .replaceAll("\r\n", "\\\\r\\\\n")
            .replaceAll("\r", "\\\\r")
            .replaceAll("\n", "\\\\n")
            .replaceAll("\t", "\\\\t")
        + "\"";
  }
}
