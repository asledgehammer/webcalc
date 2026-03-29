package com.asledgehammer.webcalc.node;

import com.asledgehammer.webcalc.layout.Bounds;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class NodeContainer<E extends Node<E>> {
  private final List<E> roots = new ArrayList<>();
  @Getter private Bounds bounds;

  public NodeContainer(@NonNull Bounds bounds) {
    this.bounds = new Bounds(bounds);
  }

  public double getWidth() {
    return bounds.getWidth();
  }

  public double getHeight() {
    return bounds.getHeight();
  }

}
