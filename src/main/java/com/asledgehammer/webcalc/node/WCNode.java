package com.asledgehammer.webcalc.node;

import com.asledgehammer.webcalc.util.DirtySupported;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class WCNode<E extends WCNode<E>> implements DirtySupported {

  @Nullable List<E> children;
  @Nullable E parent;
  private volatile boolean dirty;

  @NonNull
  public List<E> getChildren() {
    synchronized (this) {
      if (children == null) throw new NullPointerException("children == null");
      return Collections.unmodifiableList(children);
    }
  }

  public boolean hasChildren() {
    synchronized (this) {
      return children != null && !children.isEmpty();
    }
  }

  public void addChild(@NonNull E child) {
    synchronized (this) {
      if (children == null) children = new ArrayList<>();
      children.add(child);
      child.parent = (E) this;
      setDirty(true, true);
    }
  }

  public void removeChild(@NonNull E child) {
    synchronized (this) {
      if (children != null) {
        children.remove(child);
        child.parent = null;
        setDirty(true, true);
        if (children.isEmpty()) children = null;
      }
    }
  }

  public boolean hasParent() {
    synchronized (this) {
      return parent != null;
    }
  }

  @NonNull
  public E getParent() {
    synchronized (this) {
      if (parent == null) throw new NullPointerException("parent == null");
      return this.parent;
    }
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }

  @Override
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  @Override
  public void setDirty(boolean flag, boolean cascadeDownstream) {
    this.dirty = flag;

    // Propagate the flag downstream.
    if (cascadeDownstream && children != null && !children.isEmpty()) {
      for (E child : children) {
        child.setDirty(flag, true);
      }
    }
  }
}
