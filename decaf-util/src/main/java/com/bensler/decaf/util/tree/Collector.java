package com.bensler.decaf.util.tree;

import java.util.ArrayList;
import java.util.List;

/** 
 * {@link Visitor} simply collecting all members of a {@link Hierarchy}. It is meant to be a light weight 
 * throw-away object as it returns internal state from {@link #getList()}. 
 */
public class Collector<E> extends Object implements Visitor<E> {

    private final List<E> collector_;

    public Collector() {
        collector_ = new ArrayList<>();
    }

    @Override
    public void visit(final E member) {
        collector_.add(member);
    }

    /** 
     * Gives back all collected Objects.
     * <p>
     * Returns its internal list and not just a copy!
     */
    public List<E> getList() {
        return collector_;
    }

}