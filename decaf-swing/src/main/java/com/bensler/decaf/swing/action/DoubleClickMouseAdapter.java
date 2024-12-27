package com.bensler.decaf.swing.action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class DoubleClickMouseAdapter extends MouseAdapter {

  private final Consumer<MouseEvent> doubleClickAction_;

  public DoubleClickMouseAdapter(Consumer<MouseEvent> doubleClickAction) {
    doubleClickAction_ = doubleClickAction;
  }

  @Override
  public void mouseClicked(MouseEvent evt) {
    if (
      (evt.getButton() == MouseEvent.BUTTON1)
      && (evt.getClickCount() == 2)
    ) {
      doubleClickAction_.accept(evt);
    }
  }

}
