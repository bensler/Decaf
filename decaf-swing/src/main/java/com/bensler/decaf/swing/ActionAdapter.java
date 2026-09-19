package com.bensler.decaf.swing;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import javax.swing.AbstractAction;

public class ActionAdapter extends AbstractAction {

  private final Consumer<ActionEvent> action_;

  public ActionAdapter(Consumer<ActionEvent> action) {
    action_ = action;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    action_.accept(e);
  }

}
