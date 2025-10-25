package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.List;

public class SingleEntityFilter<E> implements EntitiesActionFilter<E>, EntityActionFilter<E> {

  private final ActionState defaultState_;
  private final EntityActionFilter<E> singleFilter_;

  public SingleEntityFilter(ActionState defaultState) {
    this(defaultState, e -> ActionState.ENABLED);
  }

  public SingleEntityFilter(ActionState defaultState, EntityActionFilter<E> singleFilter) {
    defaultState_ = requireNonNull(defaultState);
    singleFilter_ = requireNonNull(singleFilter);
  }

  @Override
  public ActionState getActionState(List<E> entities) {
    return ((entities.size() == 1) ? getActionState(entities.get(0)) : defaultState_);
  }

  @Override
  public ActionState getActionState(E entity) {
    return singleFilter_.getActionState(entity);
  }

}
