package com.bensler.decaf.swing.action;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.action.FocusedComponentActionController.ToolbarComponentCollector;
import com.bensler.decaf.util.Pair;

public class ActionGroup implements Action {

  public static final Set<ActionState> VISIBLE_STATES = Set.of(ActionState.ENABLED, ActionState.DISABLED);

  private final ActionAppearance appearance_;
  private final List<Action> actions_;

  public ActionGroup(Action... actions) {
    this(null, actions);
  }

  public ActionGroup(ActionAppearance appearance, Action... actions) {
    appearance_ = appearance;
    actions_= Arrays.asList(actions);
  }

  public boolean isEmpty() {
    return actions_.isEmpty();
  }

  @Override
  public void createToolbarComponent(FocusedComponentActionController ctrl, ToolbarComponentCollector collector) {
    if (appearance_ != null) {
      final JButton button = appearance_.createToolbarButton();

      button.addActionListener(evt -> {
        final MenuItemCollector menuCollector = new MenuItemCollector();

        createToolbarPopupmenuItems(ctrl, menuCollector, ctrl.computeStates(this));
        if (!menuCollector.isEmpty()) {
          final JPopupMenu popup = new JPopupMenu();

          menuCollector.populateMenu(popup);
          popup.show(button, 20, 20);
        }
      });
      collector.add(new Pair<>(button, this));
    } else {
      actions_.forEach(action -> action.createToolbarComponent(ctrl, collector));
    }
    collector.add(new Pair<>(null, this));
  }

  private void createToolbarPopupmenuItems(
      FocusedComponentActionController ctrl, MenuItemCollector collector, ActionStateMap states
  ) {
    // TODO hacky
    collector.add(Optional.empty());
    actions_.forEach(action -> action.createPopupmenuItem(collector, ctrl::getFocusedComp, ctrl::getCurrentSelection, states));
    collector.add(Optional.empty());
  }

  @Override
  public void computeState(List<?> entities, ActionStateMap target) {
    actions_.forEach(action -> action.computeState(entities, target));
    target.put(this, actions_.stream().map(target::getState).filter(VISIBLE_STATES::contains).findFirst().isPresent()
      ? ActionState.ENABLED : ActionState.HIDDEN
    );
  }

  public Optional<JPopupMenu> createPopupmenu(EntityComponent<?> comp, List<?> selection, ActionStateMap states) {
    if (states.getState(this) != ActionState.HIDDEN) {
      final MenuItemCollector collector = new MenuItemCollector();

      createPopupmenuItem(collector, () -> comp, () -> selection, states);
      if (!collector.isEmpty()) {
        final JPopupMenu popup = new JPopupMenu();

        collector.populateMenu(popup);
        return Optional.of(popup);
      }
    }
    return Optional.empty();
  }

  @Override
  public void createPopupmenuItem(
    MenuItemCollector collector, Supplier<EntityComponent<?>> compSupplier, Supplier<List<?>> selectionSupplier, ActionStateMap states
  ) {
    if (states.getState(this) != ActionState.HIDDEN) {
      if (appearance_ != null) {
        final JMenu menu = appearance_.createMenu();
        final MenuItemCollector lCollector = new MenuItemCollector();

        actions_.forEach(action -> action.createPopupmenuItem(lCollector, compSupplier, selectionSupplier, states));
        lCollector.populateMenu(menu);
        collector.add(Optional.of(menu));
      } else {
        collector.add(Optional.empty());
        actions_.forEach(action -> action.createPopupmenuItem(collector, compSupplier, selectionSupplier, states));
        collector.add(Optional.empty());
      }
    }
  }

  @Override
  public Optional<EntityAction<?>> isEntityAction() {
    return Optional.empty();
  }

}
