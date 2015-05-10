package com.bensler.decaf.util.tree;

import com.bensler.decaf.util.CanceledException;

/** 
 * Visitor for visiting members of a {@link Hierarchy}
 *
 * @param <E>
 */
public interface Visitor<E> {

    void visit(E member) throws CanceledException;

}