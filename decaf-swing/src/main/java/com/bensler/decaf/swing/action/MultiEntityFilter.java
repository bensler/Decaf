package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.List;

public class MultiEntityFilter<E> implements EntitiesActionFilter<E>  {

  private final ActionState defaultState_;
  private final EntitiesActionFilter<E> filterDelegate_;

  public MultiEntityFilter(ActionState defaultState, EntitiesActionFilter<E> filterDelegate) {
    defaultState_ = requireNonNull(defaultState);
    filterDelegate_ = requireNonNull(filterDelegate);
  }

  @Override
  public ActionState getActionState(List<E> entities) {
    return (entities.isEmpty()  ? defaultState_ : filterDelegate_.getActionState(entities));
  }

}
