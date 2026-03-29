package com.asledgehammer.webcalc.layout;

import com.asledgehammer.webcalc.math.Unit;
import lombok.Getter;
import org.joml.Matrix4f;

public class Box {

  @Getter Matrix4f transform;

  @Getter Border border;
  @Getter Layer margin;
  @Getter Layer padding;
  @Getter
  Unit width;
  @Getter
  Unit height;

  public Box(
      final Border border,
      final Layer margin,
      final Layer padding,
      final Unit width,
      final Unit height) {
    this.margin = margin;
    this.padding = padding;
    this.border = border;
    this.width = width;
    this.height = height;
  }
}
