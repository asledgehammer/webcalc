package com.asledgehammer.webcalc.element;

import com.asledgehammer.webcalc.layout.Bounds;
import com.asledgehammer.webcalc.node.WCNodeContainer;
import com.asledgehammer.webcalc.render.ElementRenderable;
import com.asledgehammer.webcalc.util.DirtySupported;
import lombok.NonNull;

import java.util.Objects;

public class Document extends WCNodeContainer<Element> implements DirtySupported {

  private Element head;
  private Element body;
  private boolean dirty;

  public Document(@NonNull Bounds bounds) {
    super(bounds);
  }

  @NonNull
  public Element getHead() {
    return head;
  }

  public void setHead(@NonNull final Element head) {
    if (Objects.equals(head, this.head)) return;
    this.head = head;
    this.dirty = true;
  }

  @NonNull
  public Element getBody() {
    return body;
  }

  public void setBody(@NonNull final Element body) {
    if (Objects.equals(body, this.body)) return;
    this.body = body;
    this.dirty = true;
  }

  @Override
  public boolean isDirty() {
    return this.dirty;
  }

  @Override
  public void setDirty(boolean flag) {
    this.dirty = true;
  }

  @NonNull
  public static Document emptyDocument(@NonNull Bounds bounds) {
    return new Document(bounds);
  }

  public void render(@NonNull ElementRenderable renderable) {
    if (this.body != null) {
      this.body.render(renderable);
    }
  }
}
