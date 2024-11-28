package com.bensler.decaf.util.tree;

/**
 * Visitor for visiting members of a {@link Hierarchy}.
 */
public interface Visitor<E> {

  /**
   * Called for every member of an {@link Hierarchy}.
   */
    void visit(E member);

}