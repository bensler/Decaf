package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.List;

public class MultiEntityFilter<E> implements EntityActionFilter<E>  {

  private final ActionState defaultState_;
  private final EntityActionFilter<E> filterDelegate_;

  public MultiEntityFilter(ActionState defaultState, EntityActionFilter<E> filterDelegate) {
    defaultState_ = requireNonNull(defaultState);
    filterDelegate_ = requireNonNull(filterDelegate);
  }

  @Override
  public ActionState getActionState(List<E> entities) {
    return (entities.isEmpty()  ? defaultState_ : filterDelegate_.getActionState(entities));
  }

}
