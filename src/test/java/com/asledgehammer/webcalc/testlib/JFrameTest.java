package com.asledgehammer.webcalc.testlib;

import com.asledgehammer.webcalc.element.Window;
import com.asledgehammer.webcalc.layout.Bounds;
import com.asledgehammer.webcalc.render.AWTRenderer;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;

public class JFrameTest extends JFrame {

  private final Window window;

  JFrameTest(int width, int height) {

    super("WebCalc Test");

    this.setSize(width, height);

    this.window = new Window(new Bounds(0, 0, width, height));
    this.window.setSize(width, height);

    add(new RenderPanel(window));
  }

  static class RenderPanel extends JPanel {

    private final @NonNull Window window;
    private final AWTRenderer renderer;

    RenderPanel(@NonNull Window window) {
      this.window = window;
      this.renderer = new AWTRenderer();
      this.setSize(window.getBounds().toDimension());
    }

    @Override
    protected void paintComponent(Graphics _g) {
      super.paintComponent(_g);
      Graphics2D g = (Graphics2D) _g;

      renderer.setGraphics(g);
      window.render(renderer);
    }
  }

  public static void main(String[] args) {
    JFrameTest frame = new JFrameTest(1024, 768);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
