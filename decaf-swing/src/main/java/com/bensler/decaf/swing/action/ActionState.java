package com.bensler.decaf.swing.action;

import javax.swing.JComponent;

/** Represents the visibility state of an Action. */
public enum ActionState {

  /** Ready to be invoked. */
  ENABLED(true),
  /** Visible but disabled. */
  DISABLED(false),
  /** Not even visible. */
  HIDDEN(false);

  private final boolean enabled_;

  ActionState(boolean enabled) {
    enabled_ = enabled;
  }

  <C extends JComponent> C applyTo(C comp) {
    comp.setEnabled(enabled_);
    return comp;
  }

}
