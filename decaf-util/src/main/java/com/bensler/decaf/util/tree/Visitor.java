package com.bensler.decaf.util.tree;

import com.bensler.decaf.util.CanceledException;

/** 
 * Visitor for visiting members of a {@link Hierarchy}. 
 */
public interface Visitor<E> {

  /**
   * Called for every member of an {@link Hierarchy}.
   *
   * @throws CanceledException if visiting should stop.
   */
    void visit(E member) throws CanceledException;

}