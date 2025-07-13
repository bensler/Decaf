package com.bensler.decaf.swing.action;

import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.EntityComponent.FocusListener;
import com.bensler.decaf.swing.selection.EntitySelectionListener;
import com.bensler.decaf.swing.selection.SelectionHolder;

public class FocusedComponentActionController implements FocusListener, EntitySelectionListener<Object> {

  private final List<EntityComponent<?>> components_;
  private final Action actions_;

  private List<?> currentSelection_;
  private EntityComponent<?> focusedComp_;

  public FocusedComponentActionController(Action actions, Collection<EntityComponent<?>> components) {
    components_ = List.copyOf(components);
    actions_ = actions;
    components_.forEach(comp -> comp.addFocusListener(this));
    components_.forEach(comp -> ((EntityComponent<Object>)comp).addSelectionListener(this));



    reevaluate(List.of());
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

      ActionState state = actions_.computeState(currentSelection_);
      System.out.println("##### %s".formatted(currentSelection_));
// TODO
    }
  }

  public JComponent createToolbar() {
    return actions_.createToolbarComponent(() -> focusedComp_, () -> currentSelection_);
  }

}
