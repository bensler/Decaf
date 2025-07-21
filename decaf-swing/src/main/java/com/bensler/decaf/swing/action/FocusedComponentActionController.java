package com.bensler.decaf.swing.action;

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    reevaluate(List.of());
  }

  public void triggerPrimaryAction() {
    actions_.triggerPrimaryAction(focusedComp_, currentSelection_);
  }

  public void showPopupMenu(MouseEvent evt) {
    final Map<Action, ActionState> states = new HashMap<>();
    final ActionState state = actions_.computeState(currentSelection_, states);

    if (state != ActionState.HIDDEN) {
      addItems(new JPopupMenu(), states).show(focusedComp_.getComponent(), evt.getX(), evt.getY());
    }
  }

  private JPopupMenu addItems(JPopupMenu menu, Map<Action, ActionState> states) {
    // TODO primaryAction ---------------------------------------------------vvvv
    actions_.createPopupmenuItem(menu::add, focusedComp_, currentSelection_, null);
//
//    List<Pair<Action, ActionState>> actions;
//
//    actions.stream().forEach(pair -> {
//      menu.add(pair.getRight().applyTo(pair.getLeft().createPopupmenuItem(
//        focusedComp_, currentSelection_, false //primaryAction_.filter(entityAction -> entityAction.equals(pair.getLeft())).isPresent()
//      )));
//    });
    return menu;
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
    } else {
      focusedComp_ = null;
      reevaluate(List.of());
    }
  }

  @Override
  public void focusLost(EntityComponent<?> component) { }

  void reevaluate(List<?> newSelection) {
    if (!newSelection.equals(currentSelection_)) {
      currentSelection_ = List.copyOf(newSelection);

      ActionState state = actions_.computeState(currentSelection_, new HashMap<>());
      System.out.println("##### %s".formatted(currentSelection_));
// TODO
    }
  }

  public JComponent createToolbar() {
    return actions_.createToolbarComponent(() -> focusedComp_, () -> currentSelection_);
  }

}
