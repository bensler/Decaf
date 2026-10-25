package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

import com.bensler.decaf.swing.action.FocusedComponentActionController.Selection;
import com.bensler.decaf.util.Pair;

/** A bundle of
 * <ul>
 *   <li>an {@link ActionAppearance}, representing this action to the user</li>
 *   <li>a filter, deciding if this action applicable to given entities or is even visible</li>
 *   <li>and the action itself performing an operation on the given entities.</li>
 * </ul>
 */
public class UiAction implements Action {

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

  void doAction(Supplier<List<?>> selectionSupplier, JComponent uiContext) {
    filteredAction_.doAction(selectionSupplier.get(), uiContext);
  }

  @Override
  public MenuItemCollector createPopupmenuItem(MenuItemCollector collector, Supplier<List<?>> selection, ActionStateMap states) {
    final JMenuItem menuItem = appearance_.createMenuItem(states.isPrimaryAction(this));

    states.getState(this).applyTo(menuItem);
    menuItem.addActionListener(_ -> doAction(selection, menuItem));
    collector.add(this, Optional.of(menuItem));
    return collector;
  }

  @Override
  public void createToolbarComponent(FocusedComponentActionController ctrl, ToolbarComponentCollector collector) {
    final JButton button = appearance_.createToolbarButton();

    button.addActionListener(_ -> filteredAction_.doAction(ctrl.getCurrentSelection(), button));
    collector.add(new Pair<>(button, this));
  }

  @Override
  public void computeState(Selection entities, ActionStateMap target) {
    target.put(this, filteredAction_.matches(entities));
  }

}
