package com.asledgehammer.webcalc.render;

import com.asledgehammer.webcalc.element.Element;
import com.asledgehammer.webcalc.element.Window;
import com.asledgehammer.webcalc.layout.Bounds;
import lombok.NonNull;

import java.awt.Graphics2D;
import java.awt.Color;
import com.asledgehammer.webcalc.element.Window;

public class AWTRenderer implements ElementRenderable {

  private Graphics2D g;

  public void setGraphics(@NonNull Graphics2D g) {
    this.g = g;
  }

  @Override
  public void renderWindow(@NonNull Window window) {
    if (g == null) return;

    Bounds bounds = window.getBounds();

    g.setColor(Color.BLACK);
    g.fillRect(
        (int) Math.floor(bounds.getX()),
        (int) Math.floor(bounds.getY()),
        (int) Math.floor(bounds.getWidth()),
        (int) Math.floor(bounds.getHeight()));

    g.setColor(Color.RED);
    g.drawString("Hello World", 20, 20);
  }

  @Override
  public void renderElement(@NonNull Element element) {
    if (g == null) return;
  }
}
