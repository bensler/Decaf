package com.bensler.decaf.util.tree;

import com.bensler.decaf.util.Named;

public class Folder extends Object implements Hierarchical, Named {

    private final Folder parent_;
    private final String name_;

    public Folder(final Folder parent, final String name) {
        parent_ = parent;
        name_ = name;
    }

    @Override
    public Object getParent() {
        return parent_;
    }

    @Override
    public String getName() {
        return name_;
    }

}
