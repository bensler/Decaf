package com.bensler.decaf.swing.tree;

import static com.bensler.decaf.swing.view.SelectionMode.MULTIPLE_INTERVAL;

import com.bensler.decaf.swing.view.PropertyView;
import com.bensler.decaf.util.tree.Hierarchical;

/**
 * An {@link EntityTree} displaying a checkbox at every node.
 */
public class CheckboxTree<H extends Hierarchical<H>> extends EntityTree<H> {

  public CheckboxTree(PropertyView<H, ?> view) {
    super(view);
    setSelectionMode(MULTIPLE_INTERVAL);
  }

  @Override
  protected TreeComponent<H> createCompoent(TreeModel<H> model, PropertyView<H, ?> view) {
    return new CheckboxTreeComponent<>(model, view);
  }

}
