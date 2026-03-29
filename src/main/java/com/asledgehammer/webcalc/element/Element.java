package com.asledgehammer.webcalc.element;

import com.asledgehammer.webcalc.node.TreeNode;
import com.asledgehammer.webcalc.render.ElementRenderable;
import lombok.Getter;
import lombok.NonNull;

public class Element extends TreeNode<Element> {

  @Getter private final String tag;

  public Element(@NonNull final String tag) {
    if (tag.isEmpty()) throw new IllegalArgumentException("tag must not be empty");
    this.tag = tag;
  }

  public void render(@NonNull ElementRenderable renderable) {
    renderable.renderElement(this);
    if (hasChildren()) {
      for (Element child : getChildren()) {
        render(renderable);
      }
    }
  }
}
