package com.bensler.decaf.util.tree;

import com.bensler.decaf.util.Named;

/**
 * Sample of an entity or business class having hierarchical nature.
 */
public class Folder extends Object implements Hierarchical<Folder>, Named {

    private final Folder parent_;
    private final String name_;

    public Folder(final Folder parent, final String name) {
        parent_ = parent;
        name_ = name;
    }

    @Override
    public Folder getParent() {
        return parent_;
    }

    @Override
    public String getName() {
        return name_;
    }

    @Override
    public String toString() {
      return name_;
    }

}
