package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Function;

public class SingleEntityFilter<E> implements EntityActionFilter<E> {

  private final ActionState defaultState_;
  private final Function<E, ActionState> singleFilter_;

  public SingleEntityFilter(ActionState defaultState) {
    this(defaultState, E -> ActionState.ENABLED);
  }

  public SingleEntityFilter(ActionState defaultState, Function<E, ActionState> singleFilter) {
    defaultState_ = requireNonNull(defaultState);
    singleFilter_ = requireNonNull(singleFilter);
  }

  @Override
  public ActionState getActionState(List<E> entities) {
    return ((entities.size() == 1) ? singleFilter_.apply(entities.get(0)) : defaultState_);
  }

}
