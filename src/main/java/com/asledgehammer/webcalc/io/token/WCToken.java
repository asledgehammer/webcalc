package com.asledgehammer.webcalc.io.token;

import org.jetbrains.annotations.NotNull;

public interface WCToken {
  String getContents();
  boolean isGeneric();
}
