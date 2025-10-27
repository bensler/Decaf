package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.List;

public class SingleEntityFilter<E> implements EntitiesActionFilter<E>, EntityActionFilter<E> {

  private final EntityActionFilter<E> singleFilter_;

  public SingleEntityFilter() {
    this(e -> ActionState.ENABLED);
  }

  public SingleEntityFilter(EntityActionFilter<E> singleFilter) {
    singleFilter_ = requireNonNull(singleFilter);
  }

  @Override
  public ActionState getActionState(List<E> entities) {
    return ((entities.size() == 1) ? getActionState(entities.get(0)) : ActionState.DISABLED);
  }

  @Override
  public ActionState getActionState(E entity) {
    return singleFilter_.getActionState(entity);
  }

}
