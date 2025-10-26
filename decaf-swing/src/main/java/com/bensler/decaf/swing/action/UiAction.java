package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import com.bensler.decaf.swing.action.FocusedComponentActionController.ToolbarComponentCollector;
import com.bensler.decaf.util.Pair;

/** A bundle of
 * <ul>
 *   <li>an {@link ActionAppearance}, representing this action to the user</li>
 *   <li>a filter, deciding if this action applicable to given entities or is even visible</li>
 *   <li>and the action itself performing an operation on the given entities.</li>
 * </ul>
 */
public class UiAction implements Action {

  public static <X> EntitiesActionFilter<X> allwaysOnFilter() {
    return (x -> ActionState.ENABLED);
  }

  public static <X> EntitiesActionFilter<X> atLeastOneFilter(ActionState zeroElementsState) {
    return (x -> ((x.size() < 1) ? zeroElementsState : ActionState.ENABLED));
  }

  private final ActionAppearance appearance_;
  private final FilteredAction<?> filteredAction_;

  public UiAction(ActionAppearance appearance, FilteredAction<?> filteredAction) {
    appearance_ = requireNonNull(appearance);
    filteredAction_ = filteredAction;
  }

  @Override
  public Optional<UiAction> isEntityAction() {
    return Optional.of(this);
  }

  public void doAction(Supplier<List<?>> selectionSupplier) {
    filteredAction_.doAction(selectionSupplier.get());
  }

  @Override
  public void createPopupmenuItem(MenuItemCollector collector, Supplier<List<?>> selection, ActionStateMap states) {
    if (states.getState(this) != ActionState.HIDDEN) {
      final JMenuItem menuItem = appearance_.createPopupmenuItem(states.isPrimaryAction(this));

      states.getState(this).applyTo(menuItem);
      menuItem.addActionListener(evt -> doAction(selection));
      collector.add(Optional.of(menuItem));
    }
  }

  @Override
  public void createToolbarComponent(FocusedComponentActionController ctrl, ToolbarComponentCollector collector) {
    final JButton button = appearance_.createToolbarButton();

    button.addActionListener(evt -> filteredAction_.doAction(ctrl.getCurrentSelection()));
    collector.add(new Pair<>(button, this));
  }

  @Override
  public void computeState(List<?> entities, ActionStateMap target){
    target.put(this, filteredAction_.computeState(entities));
  }

}
