package com.asledgehammer.webcalc.math;

import lombok.Getter;

public class Unit {

  @Getter private final double value;
  @Getter private final Type type;

  public Unit(final double value, final Type type) {
    this.value = value;
    this.type = type;
  }

  public static Unit pixels(final double value) {
    return new Unit(value, Type.PX);
  }

  public static Unit percent(final double value) {
    return new Unit(value, Type.PERCENT);
  }

  public enum Type {
    PX(false),
    PERCENT(true);

    /** If true, the value depends on the inherited parent value. */
    @Getter private final boolean dynamic;

    Type(boolean dynamic) {
      this.dynamic = dynamic;
    }
  }
}
