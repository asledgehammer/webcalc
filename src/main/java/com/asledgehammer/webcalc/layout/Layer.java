package com.asledgehammer.webcalc.layout;

import com.asledgehammer.webcalc.math.Unit;
import lombok.Getter;

public class Layer {
  @Getter final Unit top;
  @Getter final Unit right;
  @Getter final Unit bottom;
  @Getter final Unit left;

  public Layer(final Unit top, final Unit right, final Unit bottom, final Unit left) {
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
  }
}
