package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class FilteredAction<E> {

  public static <X> EntitiesActionFilter<X> allwaysOnFilter() {
    return (x -> ActionState.ENABLED);
  }

  private final Class<E> entityClass_;
  private final EntitiesActionFilter<E> filter_;
  private final EntityActionListener<E> action_;

  /** @param filter <code>null</code> means always on */
  public FilteredAction(
    Class<E> entityClass, EntitiesActionFilter<E> filter, EntityActionListener<E> action
  ) {
    entityClass_ = entityClass;
    filter_ = Optional.ofNullable(filter).orElseGet(FilteredAction::allwaysOnFilter);
    action_ = requireNonNull(action);
  }

  public void doAction(List<?> selection) {
    action_.doAction(filterTypeFittingEntities(selection));
  }

  public List<E> filterTypeFittingEntities(Collection<?> source) {
    return source.stream()
    .filter(entity -> entityClass_.isAssignableFrom(entity.getClass()))
    .map(entity -> entityClass_.cast(entity))
    .toList();
  }

  public ActionState computeState(List<?> entities){
    return filter_.getActionState(filterTypeFittingEntities(entities));
  }

}
