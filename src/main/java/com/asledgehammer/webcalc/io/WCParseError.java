package com.asledgehammer.webcalc.io;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WCParseError {

  @Getter private final @NotNull WCReferenceRange range;
  @Getter private final @Nullable String message;

  public WCParseError(@NonNull WCReferenceRange range) {
    this.range = range;
    this.message = null;
  }

  public WCParseError(@NonNull WCReferenceRange range, @NonNull String message) {
    this.range = range;
    this.message = message;
  }

  public void throwException() {
    throw new WCParseException(this);
  }

  @NonNull
  public String toString() {
    return range + " :: " + (message != null && !message.isEmpty() ? message : "Parse Error");
  }
}
