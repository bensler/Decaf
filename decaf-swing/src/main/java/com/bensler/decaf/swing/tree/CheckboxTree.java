package com.bensler.decaf.swing.tree;

import static com.bensler.decaf.swing.selection.SelectionMode.MULTIPLE_INTERVAL;

import java.util.Collection;
import java.util.Set;

import com.bensler.decaf.swing.view.PropertyView;
import com.bensler.decaf.util.tree.Hierarchical;

/**
 * An {@link EntityTree} displaying a checkbox at every node.
 */
public class CheckboxTree<H extends Hierarchical<H>> extends EntityTree<H> {

  public CheckboxTree(PropertyView<H, ?> view, Class<H> entityClass) {
    super(view, entityClass);
    setSelectionMode(MULTIPLE_INTERVAL);
  }

  @Override
  protected TreeComponent<H> createComponent(EntityTreeModel<H> model, PropertyView<H, ?> view) {
    return new CheckboxTreeComponent<>(model, view);
  }

  public void setCheckedNodes(Collection<? extends H> nodesToBeChecked) {
    ((CheckboxTreeComponent<H>)tree_).setCheckedNodes(nodesToBeChecked.stream().filter(h -> contains(h).isPresent()).toList());
  }

  public Set<H> getCheckedNodes() {
    return ((CheckboxTreeComponent<H>)tree_).getCheckedNodes();
  }

  public void addCheckedListener(CheckedListener<H> listener) {
    ((CheckboxTreeComponent<H>)tree_).addCheckedListener(listener);
  }

  public interface CheckedListener<H extends Hierarchical<H>> {

    void nodesChecked(Set<H> checkedNodes);

  }

}
