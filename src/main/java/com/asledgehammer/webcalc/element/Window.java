package com.asledgehammer.webcalc.element;

import com.asledgehammer.webcalc.layout.Bounds;
import com.asledgehammer.webcalc.render.ElementRenderable;
import com.asledgehammer.webcalc.util.DirtySupported;
import lombok.Getter;
import lombok.NonNull;

public class Window implements DirtySupported {

  @Getter private Bounds bounds;
  @Getter private Bounds boundsInner;

  @Getter private double innerWidth;
  @Getter private double innerHeight;
  private volatile boolean dirty;

  @Getter private double scale = 1.0;

  @Getter private Document document;

  public Window(@NonNull Bounds bounds) {
    this.bounds = bounds;
    this.boundsInner = new Bounds(bounds);
    this.document = Document.emptyDocument(bounds);
  }

  public Window(@NonNull Document document) {
    this.document = document;
    this.bounds = new Bounds(document.getBounds());
    this.boundsInner = new Bounds(bounds);
  }

  public void calculate() {}

  public void render(@NonNull ElementRenderable renderable) {
    renderable.renderWindow(this);
    this.document.render(renderable);
  }

  public void setSize(int width, int height) {
    this.bounds.setSize(width, height);
  }

  public void setScale(double scale) {
    this.scale = scale;
    this.innerWidth = Math.floor(bounds.getWidth() * scale);
    this.innerHeight = Math.floor(bounds.getHeight() * scale);
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }

  @Override
  public void setDirty(boolean flag) {
    this.dirty = flag;
  }

  @Override
  public void setDirty(boolean flag, boolean cascadeDownstream) {
    this.dirty = flag;
    if (cascadeDownstream) {}
  }
}
