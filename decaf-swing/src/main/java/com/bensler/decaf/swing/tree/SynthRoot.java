package com.bensler.decaf.swing.tree;

import com.bensler.decaf.util.tree.Hierarchical;

public final class SynthRoot extends Object implements Hierarchical<Object> {

    @Override
    public Object getParent() {
        return null;
    }

    @Override
    public String toString() {
      return "<SynthRoot>";
    }

}