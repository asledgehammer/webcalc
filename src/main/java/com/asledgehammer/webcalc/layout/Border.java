package com.asledgehammer.webcalc.layout;

import com.asledgehammer.webcalc.math.Unit;

public class Border extends Layer {

  private Radius radius;
  private Inset inset;

  public Border(
      final Unit top,
      final Unit right,
      final Unit bottom,
      final Unit left,
      final Radius radius,
      final Inset inset) {
    super(top, right, bottom, left);
    this.radius = radius;
    this.inset = inset;
  }

  public static class Radius {
    final Unit tl;
    final Unit tr;
    final Unit br;
    final Unit bl;

    Radius(final Unit tl, final Unit tr, final Unit br, final Unit bl) {
      this.tl = tl;
      this.tr = tr;
      this.br = br;
      this.bl = bl;
    }
  }

  public static class Inset {
    final Unit top;
    final Unit right;
    final Unit bottom;
    final Unit left;

    Inset(final Unit top, final Unit right, final Unit bottom, final Unit left) {
      this.top = top;
      this.right = right;
      this.bottom = bottom;
      this.left = left;
    }
  }
}
