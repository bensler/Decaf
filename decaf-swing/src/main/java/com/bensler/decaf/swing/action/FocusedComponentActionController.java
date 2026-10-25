package com.bensler.decaf.swing.action;

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.WeakHashMap;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.awt.SimpleMouseAdapter;
import com.bensler.decaf.swing.selection.SelectionHolder;
import com.jgoodies.forms.layout.FormLayout;

public class FocusedComponentActionController {

  private final List<EntityComponent<?>> entityComponents_;
  private final ActionGroup actions_;
  private final WeakHashMap<JComponent, Action> actionComponents_;
  private final boolean primaryActionEnabled_;

  private List<?> currentSelection_;
  private EntityComponent<?> focusedComp_;

  public FocusedComponentActionController(ActionGroup actions, Collection<EntityComponent<?>> components) {
    this(actions, components, true);
  }

  public FocusedComponentActionController(ActionGroup actions, Collection<EntityComponent<?>> components, boolean primaryActionEnabled) {
    entityComponents_ = List.copyOf(components);
    actions_ = actions;
    primaryActionEnabled_ = primaryActionEnabled;
    actionComponents_ = new WeakHashMap<>();
    entityComponents_.forEach(comp -> comp.addFocusListener(this::focusGained));
    entityComponents_.forEach(comp -> comp.addSelectionListener(this::selectionChanged));
    focusGained(entityComponents_.iterator().next());
  }

  private void selectionChanged(SelectionHolder<? extends Object> selectionSource, List<? extends Object> selection) {
    if (selectionSource == focusedComp_) {
      setSelection(selection);
    }
  }

  List<?> getCurrentSelection() {
    return currentSelection_;
  }

  void triggerPrimaryAction(JComponent uiContext) {
    computeStates().getPrimaryAction().ifPresent(action -> action.doAction(() -> currentSelection_, uiContext));
  }

  public void showPopupMenu(MouseEvent evt) {
    final ActionStateMap states = computeStates();

    if (states.isEnabled(actions_)) {
      actions_.createPopupmenu(currentSelection_, states)
      .ifPresent(popup -> popup.show(focusedComp_.getComponent(), evt.getX(), evt.getY()));
    }
  }

  ActionStateMap computeStates() {
    return computeStates(actions_);
  }

  ActionStateMap computeStates(Action action) {
    final ActionStateMap states = new ActionStateMap();

    action.computeState(new Selection(focusedComp_.getEntityClass(), currentSelection_), states);
    if (!primaryActionEnabled_) {
      states.resetPrimaryAction();
    }
    return states;
  }

  private void focusGained(EntityComponent<?> component) {
    if (entityComponents_.contains(component)) {
      setSelection((focusedComp_ = component).getSelection());
    }
  }

  private void setSelection(List<?> newSelection) {
    currentSelection_ = List.copyOf(newSelection);
    final ActionStateMap states = computeStates();

    actionComponents_.entrySet().forEach(entry -> states.getState(entry.getValue()).applyTo(entry.getKey()));
  }

  public JComponent createToolbar() {
    final ToolbarComponentCollector toolbarComponents = new ToolbarComponentCollector(actionComponents_::put);

    actions_.createToolbarComponent(this, toolbarComponents);
    return toolbarComponents.addToToolbar(new JPanel(new FormLayout(toolbarComponents.getColumnSpec(), "f:p")));
  }

  public JMenuBar createMenuBar() {
    return actions_.createPopupmenuItem(new MenuItemCollector(actionComponents_::put), () -> getCurrentSelection(), computeStates()).populateMenubar();
  }

  static class Selection {

    final Class<?> entityClass_;
    final List<?> entities_;

    private Selection(Class<?> entityClass, List<?> entities) {
      entityClass_ = entityClass;
      entities_ = entities;
    }

  }

  public <E extends EntityComponent<?>> void attachTo(E target, Consumer<E> initializer, Consumer<MouseEvent> onCtxMenuOpen) {
    final JComponent comp = target.getComponent();

    comp.addMouseListener(new ContextMenuMouseAdapter(evt -> triggerContextMenu(evt, onCtxMenuOpen)));
    comp.addMouseListener(SimpleMouseAdapter.doubleClicked(_ -> triggerPrimaryAction(comp)));
    initializer.accept(target);
  }

  void triggerContextMenu(MouseEvent evt, Consumer<MouseEvent> onCtxMenuOpen) {
    onCtxMenuOpen.accept(evt);
    showPopupMenu(evt);
  }

}
