package com.asledgehammer.webcalc.io.token;

import lombok.Getter;
import lombok.NonNull;

public class WCToken {

  @Getter private final @NonNull String contents;

  WCToken(@NonNull String contents) {
    this.contents = contents;
  }
}
