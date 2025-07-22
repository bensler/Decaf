package com.bensler.decaf.swing.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class ActionStateMap {

  private final Map<Action, ActionState> states_;
  private EntityAction<?> primaryAction_;

  ActionStateMap() {
    states_ = new HashMap<>();
  }

  void put(Action action, ActionState state) {
    final Optional<EntityAction<?>> entityAction = action.isEntityAction();

    states_.put(action, state);
    if ((primaryAction_ == null) && entityAction.isPresent() && (state == ActionState.ENABLED)) {
      primaryAction_ = entityAction.get();
    }
  }

  ActionState getState(Action action) {
    return states_.get(action);
  }

  boolean isPrimaryAction(Action action) {
    return primaryAction_ == action;
  }

  Optional<EntityAction<?>> getPrimaryAction() {
    return Optional.ofNullable(primaryAction_);
  }

}
