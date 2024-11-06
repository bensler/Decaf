package com.bensler.decaf.swing.action;

import java.util.List;
import java.util.function.Function;

public class SingleEntityFilter<E> implements Function<List<E>, ActionState>  {

  private final ActionState defaultState_;
  private final Function<E, ActionState> singleFilter_;

  public SingleEntityFilter(ActionState defaultState, Function<E, ActionState> singleFilter) {
    defaultState_ = defaultState;
    singleFilter_ = singleFilter;
  }

  @Override
  public ActionState apply(List<E> entities) {
    // TODO Auto-generated method stub
    return null;
  }

}
