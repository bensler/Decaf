package com.bensler.decaf.util.tree;

import java.util.Collection;
import java.util.Set;

import com.bensler.decaf.util.tree.ChildrenCollectionMaintainer.DefaultCollectionMaintainer;

/**
 * A Hierarchy forms a tree out of a collection of {@link Hierarchical}s. A synthetic root is used if there are more than one
 * nodes with an unknown or null parent ref. This is to make sure that there is always exactly one root.
 */
public class Hierarchy<H extends Hierarchical<?>> extends AbstractHierarchy<H, Set<H>> {

  /**
   * Creates a new empty hierarchy.
   */
  public Hierarchy() {
    super(new DefaultCollectionMaintainer<H>());
  }

  /**
   * Creates a new hierarchy using the given members.
   */
  public Hierarchy(final Collection<? extends H> members) {
    this();
    addAll(members);
  }

}
