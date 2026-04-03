package com.asledgehammer.webcalc.node;

import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WCTreeNode<E extends WCTreeNode<E>> extends WCNode<E> {

  /**
   * Reaches upward until the node has no parent.
   *
   * @return The top-most node.
   */
  @Nullable
  public E getRoot() {
    return parent == null ? (E) this : parent.getRoot();
  }

  @Nullable
  public List<E> getStems() {
    if (!hasChildren()) return null;
    val list = new ArrayList<E>();
    getStems(list);
    return list;
  }

  /**
   * Internal recursion
   *
   * @param list The list to add for all stems.
   */
  void getStems(List<E> list) {
    if (children != null && !children.isEmpty()) {
      for (val child : children) {
        child.getStems(list);
      }
    } else if (isStem()) {
      list.add((E) this);
    }
  }

  @Nullable
  public List<E> getLeaves() {
    if (!hasChildren()) return null;
    val list = new ArrayList<E>();
    getLeaves(list);
    return list;
  }

  /**
   * Internal recursion
   *
   * @param list The list to add for all leaves.
   */
  void getLeaves(List<E> list) {
    if (children != null && !children.isEmpty()) {
      for (val child : children) {
        child.getLeaves(list);
      }
    } else if (isLeaf()) {
      list.add((E) this);
    }
  }

  public boolean isLeaf() {
    return hasParent() && !hasChildren();
  }

  public boolean isStem() {
    return hasParent() && hasChildren();
  }

  public boolean isRoot() {
    return !hasParent() && hasChildren();
  }

  public boolean isOrphan() {
    return !hasParent() && !hasChildren();
  }
}
