package com.bensler.decaf.swing.view;

import java.util.function.Function;

import com.bensler.decaf.util.tree.Hierarchical;

public class TreePropertyView<E extends Hierarchical<?>, P> {

  private final PropertyView<E, P> propertyView_;
  private final Function<E, ?> parentRefProvider_;

  public TreePropertyView(PropertyView<E, P> aPropertyView, Function<E, ?> aParentRefProvider) {
    propertyView_ = aPropertyView;
    parentRefProvider_ = aParentRefProvider;
  }

  public PropertyView<E, P> getPropertyView() {
    return propertyView_;
  }

  public Function<E, ?> getParentRefProvider() {
    return parentRefProvider_;
  }

}
