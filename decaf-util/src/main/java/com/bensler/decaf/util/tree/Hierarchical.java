package com.bensler.decaf.util.tree;

/**
 * Implemented by classes being in a hierarchical order, which means they have a parent. There is no assumption made
 * about children. This is possible only in the context of a Hierarchy. So one Hierarchical might be a member of
 * different hierarchies possibly performing some filtering.
 */
public interface Hierarchical {

    Object getParent();

}
