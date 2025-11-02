package com.bensler.decaf.swing.action;

import static com.bensler.decaf.swing.action.ActionState.DISABLED;
import static com.bensler.decaf.swing.action.ActionState.ENABLED;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class ActionStateMap {

  private final Set<Action> enabledActions_;
  private UiAction primaryAction_;

  ActionStateMap() {
    enabledActions_ = new HashSet<>();
  }

  void put(Action action, boolean enabled) {
    final Optional<UiAction> entityAction = action.isEntityAction();

    if (enabled) {
      enabledActions_.add(action);
      if ((primaryAction_ == null) && entityAction.isPresent()) {
        primaryAction_ = entityAction.get();
      }
    }
  }

  ActionState getState(Action action) {
    return isEnabled(action) ? ENABLED : DISABLED;
  }

  boolean isEnabled(Action action) {
    return enabledActions_.contains(action);
  }

  boolean isPrimaryAction(Action action) {
    return primaryAction_ == action;
  }

  Optional<UiAction> getPrimaryAction() {
    return Optional.ofNullable(primaryAction_);
  }

}
