package com.bensler.decaf.swing.action;

import java.util.List;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.selection.EntitySelectionListener;
import com.bensler.decaf.swing.selection.SelectionHolder;

public class ActionBinding<E> implements EntitySelectionListener<E> {

  private final SelectionHolder<E> comp_;
  private final EntityAction<E> action_;

  public ActionBinding (SelectionHolder<E> comp, EntityAction<E> action) {
    (comp_ = comp).setSelectionListener(this);
    action_ = action;
  }

  @Override
  public void selectionChanged(EntityComponent<E> source, List<E> selection) {
    // TODO     vvvvvvvvvvv---- what to do with it
    ActionState actionState = action_.getActionState(selection);
  }

}
