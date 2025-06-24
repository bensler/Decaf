package com.bensler.decaf.swing.action;

import java.util.List;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.EntityComponent.FocusListener;
import com.bensler.decaf.swing.selection.EntitySelectionListener;
import com.bensler.decaf.swing.selection.SelectionHolder;

public class FocusedComponentActionController implements FocusListener, EntitySelectionListener<Object> {

  private final List<EntityComponent<?>> components_;

  private List<?> currentSelection_;
  private EntityComponent<?> focusedComp_;

  public FocusedComponentActionController(EntityComponent<?>... components) {
    components_ = List.of(components);
    components_.forEach(comp -> comp.addFocusListener(this));
    components_.forEach(comp -> ((EntityComponent<Object>)comp).addSelectionListener(this));
    reevaluate(null);
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
      reevaluate(null);
    }
  }

  @Override
  public void focusLost(EntityComponent<?> component) {
    if (focusedComp_ == component) {
      focusedComp_ = null;
      reevaluate(null);
    }
  }

  void reevaluate(List<?> newSelection) {
    if (newSelection == null) {
      newSelection = List.of();
    }
    if (!newSelection.equals(currentSelection_)) {
      currentSelection_ = List.copyOf(newSelection);

      System.out.println("##### %s".formatted(currentSelection_));
// TODO
    }
  }

}
