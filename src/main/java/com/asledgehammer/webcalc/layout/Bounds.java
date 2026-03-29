package com.asledgehammer.webcalc.layout;

import com.asledgehammer.webcalc.util.DirtySupported;
import com.asledgehammer.webcalc.util.LocationSupported;
import com.asledgehammer.webcalc.util.SizeSupported;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

import java.awt.*;

@Getter(value = AccessLevel.PUBLIC)
public class Bounds implements DirtySupported, LocationSupported, SizeSupported {

  private double x;
  private double y;
  private double width;
  private double height;

  private boolean dirty;

  public Bounds(@NonNull Bounds other) {
    this(other.x, other.y, other.width, other.height);
  }

  public Bounds(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.dirty = true;
  }

  public void setX(double x) {
    if (x == this.x) return;
    this.x = x;
    this.dirty = true;
  }

  public void setY(double y) {
    if (y == this.y) return;
    this.y = y;
    this.dirty = true;
  }

  public void setSize(double width, double height) {
    this.setWidth(width);
    this.setHeight(height);
  }

  public void setWidth(double width) {
    if (width == this.width) return;
    this.width = width;
    this.dirty = true;
  }

  public void setHeight(double height) {
    if (height == this.height) return;
    this.height = height;
    this.dirty = true;
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }

  @Override
  public void setDirty(boolean flag) {
    this.dirty = flag;
  }

  @NonNull
  public Bounds copy() {
    return new Bounds(x, y, width, height);
  }

  @NonNull
  public Dimension toDimension() {
    return new Dimension((int) Math.floor(width), (int) Math.floor(height));
  }
}
