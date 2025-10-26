package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bensler.decaf.swing.EntityComponent;

public class FilteredAction<E> {

  private final Class<E> entityClass_;
  private final EntitiesActionFilter<E> filter_;
  private final EntityActionListener<E> action_;

  /** @param filter <code>null</code> means always on */
  public FilteredAction(
    Class<E> entityClass, EntitiesActionFilter<E> filter, EntityActionListener<E> action
  ) {
    entityClass_ = entityClass;
    filter_ = Optional.ofNullable(filter).orElseGet(UiAction::allwaysOnFilter);
    action_ = requireNonNull(action);
  }

  private List<E> filterTypeFittingEntities(List<?> entities) {
    return entities.stream()
      .filter(entity -> entityClass_.isAssignableFrom(entity.getClass()))
      .map(entity -> entityClass_.cast(entity))
      .collect(Collectors.toList());
  }

  public void doAction(EntityComponent<?> comp, List<?> selection) {
    if (entityClass_.isAssignableFrom(comp.getEntityClass())) {
      action_.doAction((EntityComponent<E>)comp, filterTypeFittingEntities(selection));
    }
  }

  public ActionState computeState(List<?> entities){
    return filter_.getActionState(entities.stream()
      .filter(entity -> entityClass_.isAssignableFrom(entity.getClass()))
      .map(entity -> entityClass_.cast(entity)).toList()
    );
  }

}
