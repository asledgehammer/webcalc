package com.asledgehammer.webcalc.render;

import com.asledgehammer.webcalc.element.Element;
import com.asledgehammer.webcalc.element.Window;
import lombok.NonNull;

public interface ElementRenderable {

  void renderElement(@NonNull Element element);

  void renderWindow(@NonNull Window window);
}
