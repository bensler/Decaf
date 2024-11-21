package com.bensler.decaf.swing.tree;

import com.bensler.decaf.util.tree.Hierarchical;

public final class SynthRoot<H> extends Object implements Hierarchical<H> {

  SynthRoot() {}

  @Override
  public H getParent() {
    return null;
  }

    @Override
  public String toString() {
    return "<SynthRoot>";
  }

}