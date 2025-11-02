package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.bensler.decaf.swing.action.FilteredAction.FilterMany;
import com.bensler.decaf.swing.action.FilteredAction.FilterOne;

public class SingleEntityFilter<E> implements FilterMany<E>, FilterOne<E> {

  private final FilterOne<E> singleFilter_;

  public SingleEntityFilter() {
    this(e -> true);
  }

  public SingleEntityFilter(FilterOne<E> singleFilter) {
    singleFilter_ = requireNonNull(singleFilter);
  }

  @Override
  public boolean match(List<E> entities) {
    return (entities.size() == 1) && matches(entities.get(0));
  }

  @Override
  public boolean matches(E entity) {
    return singleFilter_.matches(entity);
  }

}
