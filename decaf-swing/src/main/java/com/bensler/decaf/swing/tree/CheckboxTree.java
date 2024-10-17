package com.bensler.decaf.swing.tree;

import com.bensler.decaf.swing.view.PropertyView;
import com.bensler.decaf.util.tree.Hierarchical;

/**
 * This is a tree that displays a Hierarchy.
 */
public class CheckboxTree<H extends Hierarchical<H>> extends EntityTree<H> {

  public CheckboxTree(PropertyView<H, ?> view) {
    super(view);
  }

  @Override
  protected TreeComponent<H> createCompoent(TreeModel<H> model, PropertyView<H, ?> view) {
    return new CheckboxTreeComponent<>(model, view);
  }

}
