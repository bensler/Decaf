package com.bensler.decaf.swing.action;

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.EntityComponent.FocusListener;
import com.bensler.decaf.swing.selection.EntitySelectionListener;
import com.bensler.decaf.swing.selection.SelectionHolder;

public class FocusedComponentActionController implements FocusListener, EntitySelectionListener<Object> {

  private final List<EntityComponent<?>> components_;
  private final ActionGroup actions_;

  private List<?> currentSelection_;
  private EntityComponent<?> focusedComp_;

  public FocusedComponentActionController(ActionGroup actions, Collection<EntityComponent<?>> components) {
    components_ = List.copyOf(components);
    actions_ = actions;
    components_.forEach(comp -> comp.addFocusListener(this));
    components_.forEach(comp -> ((EntityComponent<Object>)comp).addSelectionListener(this));
    focusGained(components_.iterator().next());
  }

  public void triggerPrimaryAction() {
    final ActionStateMap states = new ActionStateMap();

    actions_.computeState(currentSelection_, states);
    states.getPrimaryAction().ifPresent(action -> action.doAction(focusedComp_, currentSelection_));
  }

  public void showPopupMenu(MouseEvent evt) {
    final ActionStateMap states = new ActionStateMap();

    actions_.computeState(currentSelection_, states);

    if (states.getState(actions_) != ActionState.HIDDEN) {
      final JPopupMenu menu = new JPopupMenu();
      actions_.createPopupmenuItem(menu::add, focusedComp_, currentSelection_, states);
      menu.show(focusedComp_.getComponent(), evt.getX(), evt.getY());
    }
  }

  @Override
  public void selectionChanged(SelectionHolder<Object> selectionSource, List<Object> selection) {
    if (selectionSource == focusedComp_) {
      reevaluate(selection);
    }
  }

  @Override
  public void focusGained(EntityComponent<?> component) {
    if (components_.contains(component)) {
      reevaluate((focusedComp_ = component).getSelection());
    }
  }

  void reevaluate(List<?> newSelection) {
    if (!newSelection.equals(currentSelection_)) {
      currentSelection_ = List.copyOf(newSelection);

      System.out.println("##### %s".formatted(currentSelection_));
// TODO
    }
  }

  public JComponent createToolbar() {
    return actions_.createToolbarComponent(() -> focusedComp_, () -> currentSelection_);
  }

  public <E extends EntityComponent<?>> void attachTo(E target, Consumer<E> initializer, Consumer<MouseEvent> onCtxMenuOpen) {
    final JComponent comp = target.getComponent();

    comp.addMouseListener(new ContextMenuMouseAdapter(evt -> triggerContextMenu(evt, onCtxMenuOpen)));
    comp.addMouseListener(new DoubleClickMouseAdapter(evt -> triggerPrimaryAction()));
    initializer.accept(target);
  }

  void triggerContextMenu(MouseEvent evt, Consumer<MouseEvent> onCtxMenuOpen) {
    onCtxMenuOpen.accept(evt);
    showPopupMenu(evt);
  }

}
