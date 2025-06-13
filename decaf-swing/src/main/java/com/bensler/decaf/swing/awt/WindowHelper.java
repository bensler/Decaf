package com.bensler.decaf.swing.awt;

import java.awt.Rectangle;
import java.awt.Window;

public class WindowHelper {

  public static void centerOnParent(Window window) {
    final Rectangle dialogBounds = window.getBounds();
    final Rectangle parentBounds = window.getParent().getBounds();

    dialogBounds.x = (int)(parentBounds.getCenterX() - (dialogBounds.width / 2.0));
    dialogBounds.y = (int)(parentBounds.getCenterY() - (dialogBounds.height / 2.0));
    window.setBounds(dialogBounds);
  }

  private WindowHelper() { }

}
