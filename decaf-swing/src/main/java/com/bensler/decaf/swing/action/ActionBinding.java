package com.bensler.decaf.swing.action;

import java.util.List;

import com.bensler.decaf.swing.selection.EntitySelectionListener;
import com.bensler.decaf.swing.selection.SelectionHolder;

public class ActionBinding<E> implements EntitySelectionListener<E> {

  private final EntityAction<E> action_;
  private List<E> selection_;

  public ActionBinding(SelectionHolder<E> comp, EntityAction<E> action) {
    selection_ = List.of();
    comp.addSelectionListener(this);
    action_ = action;
  }

  @Override
  public void selectionChanged(SelectionHolder<E> source, List<E> selection) {
    // TODO     vvvvvvvvvvv---- what to do with it
    ActionState actionState = action_.getActionState(selection_ = List.copyOf(selection));
  }

}
