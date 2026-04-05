package com.asledgehammer.webcalc.io;

import lombok.NonNull;

public class WCParseException extends RuntimeException {
  public WCParseException(@NonNull WCParseError error) {
    super(error.toString());
  }
}
