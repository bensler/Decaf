package com.bensler.decaf.swing.dialog;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class WindowClosingTrigger extends WindowAdapter {

  private final Consumer<WindowEvent> action_;

  public WindowClosingTrigger(Window window, Consumer<WindowEvent> action) {
    action_ = action;
    window.addWindowListener(this);
  }

  @Override
  public void windowClosing(WindowEvent evt) {
    action_.accept(evt);
  }

}
