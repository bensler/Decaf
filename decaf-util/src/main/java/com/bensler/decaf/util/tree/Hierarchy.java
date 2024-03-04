package com.bensler.decaf.util.tree;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import com.bensler.decaf.util.tree.ChildrenCollectionMaintainer.DefaultCollectionMaintainer;

/**
 * A Hierarchy forms a tree out of a collection of {@link Hierarchical}s. A synthetic root is used if there are more than one
 * nodes with an unknown or null parent ref. This is to make sure that there is always exactly one root.
 */
public class Hierarchy<H extends Hierarchical<H>> extends AbstractHierarchy<H, Set<H>> {

  /**
   * Creates a new empty hierarchy.
   */
  public Hierarchy(Function<H, H> parentRefProvider) {
    super(new DefaultCollectionMaintainer<>(), parentRefProvider);
  }

  /**
   * Creates a new hierarchy using the given members.
   */
  public Hierarchy(final Collection<? extends H> members, Function<H, H> parentRefProvider) {
    this(parentRefProvider);
    addAll(members);
  }

}
