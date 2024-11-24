package com.bensler.decaf.swing.tree;

import com.bensler.decaf.util.tree.Hierarchical;
import com.bensler.decaf.util.tree.Hierarchy;

/** Root object which is nerver visible but exists to have a common root node above potentially many
 * root nodes of a {@link Hierarchy}  */
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