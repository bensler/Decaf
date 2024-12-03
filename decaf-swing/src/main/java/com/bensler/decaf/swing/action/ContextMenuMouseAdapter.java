package com.bensler.decaf.swing.action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ContextMenuMouseAdapter extends MouseAdapter {

  private final Consumer<MouseEvent> openMenuAction_;

  public ContextMenuMouseAdapter(Consumer<MouseEvent> openMenuAction) {
    openMenuAction_ = openMenuAction;
  }

  @Override
  public void mouseClicked(MouseEvent evt) {
    triggerContextMenu(evt);
  }

  @Override
  public void mousePressed(MouseEvent evt) {
    triggerContextMenu(evt);
  }

  @Override
  public void mouseReleased(MouseEvent evt) {
    triggerContextMenu(evt);
  }

  private void triggerContextMenu(MouseEvent evt) {
    if (evt.isPopupTrigger()) {
      openMenuAction_.accept(evt);
    }
  }

}
