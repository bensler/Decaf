package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Function;

public class MultiEntityFilter<E> implements Function<List<E>, ActionState>  {

  private final ActionState defaultState_;
  private final Function<List<E>, ActionState> filterDelegate_;

  public MultiEntityFilter(ActionState defaultState, Function<List<E>, ActionState> filterDelegate) {
    defaultState_ = requireNonNull(defaultState);
    filterDelegate_ = requireNonNull(filterDelegate);
  }

  @Override
  public ActionState apply(List<E> entities) {
    return (entities.isEmpty()  ? defaultState_:filterDelegate_.apply(entities));
  }

}
