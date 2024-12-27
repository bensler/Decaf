package com.bensler.decaf.swing.action;

import java.util.Arrays;
import java.util.List;

import com.bensler.decaf.swing.EntityComponent;

public class ActionGroup<E> {

  private final List<EntityAction<E>> actions_;

  public ActionGroup() {
    actions_ = List.of();
  }

  public ActionGroup(EntityAction<E>... actions) {
    actions_= Arrays.asList(actions);
  }

  public boolean isEmpty() {
    return actions_.isEmpty();
  }

  public ContextActions<E> createContextMenu(EntityComponent<E> comp) {
    return new ContextActions<>(actions_, comp);
  }

}
