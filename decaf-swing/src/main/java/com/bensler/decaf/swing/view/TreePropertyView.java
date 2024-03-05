package com.bensler.decaf.swing.view;

import com.bensler.decaf.util.tree.Hierarchical;

public class TreePropertyView<E extends Hierarchical<E>, P> {

  private final PropertyView<E, P> propertyView_;

  public TreePropertyView(PropertyView<E, P> aPropertyView) {
    propertyView_ = aPropertyView;
  }

  public PropertyView<E, P> getPropertyView() {
    return propertyView_;
  }

}
