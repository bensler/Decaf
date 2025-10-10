package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.action.FocusedComponentActionController.ToolbarComponentCollector;
import com.bensler.decaf.util.Pair;

/** A bundle of
 * <ul>
 *   <li>an {@link ActionAppearance}, representing this action to the user</li>
 *   <li>a filter, deciding if this action applicable to given entities or is even visible</li>
 *   <li>and the action itself performing an operation on the given entities.</li>
 * </ul>
 */
public class EntityAction<E> implements Action {

  public static <X> EntitiesActionFilter<X> allwaysOnFilter() {
    return (x -> ActionState.ENABLED);
  }

  public static <X> EntitiesActionFilter<X> atLeastOneFilter(ActionState zeroElementsState) {
    return (x -> ((x.size() < 1) ? zeroElementsState : ActionState.ENABLED));
  }

  private final ActionAppearance appearance_;
  private final Class<E> entityClass_;
  private final EntitiesActionFilter<E> filter_;
  private final EntityActionListener<E> action_;

  /** @param filter <code>null</code> means always on */
  public EntityAction(
    ActionAppearance appearance, Class<E> entityClass, EntitiesActionFilter<E> filter, EntityActionListener<E> action
  ) {
    appearance_ = requireNonNull(appearance);
    entityClass_ = entityClass;
    filter_ = Optional.ofNullable(filter).orElseGet(EntityAction::allwaysOnFilter);
    action_ = requireNonNull(action);
  }

  @Override
  public Optional<EntityAction<?>> isEntityAction() {
    return Optional.of(this);
  }

  public ActionState getActionState(List<?> entities) {
    return filter_.getActionState(filterTypeFittingEntities(entities));
  }

  private List<E> filterTypeFittingEntities(List<?> entities) {
    return entities.stream()
      .filter(entity -> entityClass_.isAssignableFrom(entity.getClass()))
      .map(entity -> entityClass_.cast(entity))
      .collect(Collectors.toList());
  }

  public void doAction(Supplier<EntityComponent<?>> compSupplier, Supplier<List<?>> selectionSupplier) {
    final EntityComponent<?> comp = compSupplier.get();

    if (entityClass_.isAssignableFrom(comp.getEntityClass())) {
      action_.doAction((EntityComponent<E>)comp, filterTypeFittingEntities(selectionSupplier.get()));
    }
  }

  @Override
  public void createPopupmenuItem(
    MenuItemCollector collector, Supplier<EntityComponent<?>> comp, Supplier<List<?>> selection, ActionStateMap states
  ) {
    if (states.getState(this) != ActionState.HIDDEN) {
      final JMenuItem menuItem = appearance_.createPopupmenuItem(states.isPrimaryAction(this));

      states.getState(this).applyTo(menuItem);
      menuItem.addActionListener(evt -> doAction(comp, selection));
      collector.add(Optional.of(menuItem));
    }
  }

  @Override
  public void createToolbarComponent(FocusedComponentActionController ctrl, ToolbarComponentCollector collector) {
    final JButton button = appearance_.createToolbarButton();

    button.addActionListener(evt -> action_.doAction((EntityComponent<E>)ctrl.getFocusedComp(), (List<E>)ctrl.getCurrentSelection()));
    collector.add(new Pair<>(button, this));
  }

  @Override
  public void computeState(List<?> entities, ActionStateMap target){
    target.put(this, filter_.getActionState(entities.stream()
      .filter(entity -> entityClass_.isAssignableFrom(entity.getClass()))
      .map(entity -> entityClass_.cast(entity)).toList()
    ));
  }

}
