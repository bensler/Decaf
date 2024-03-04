package com.bensler.decaf.swing.view;

import java.util.function.Function;

import com.bensler.decaf.util.tree.Hierarchical;

public class TreePropertyView<E extends Hierarchical<E>, P> {

  private final PropertyView<E, P> propertyView_;
  private final Function<E, E> parentRefProvider_;

  public TreePropertyView(PropertyView<E, P> aPropertyView, Function<E, E> aParentRefProvider) {
    propertyView_ = aPropertyView;
    parentRefProvider_ = aParentRefProvider;
  }

  public PropertyView<E, P> getPropertyView() {
    return propertyView_;
  }

  public Function<E, E> getParentRefProvider() {
    return parentRefProvider_;
  }

}
