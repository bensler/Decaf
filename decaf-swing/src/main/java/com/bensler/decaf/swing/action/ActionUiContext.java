package com.bensler.decaf.swing.action;

import java.awt.Component;
import java.awt.Window;
import java.util.Optional;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

public class ActionUiContext {

  private static final ThreadLocal<Optional<Window>> CONTEXT = new ThreadLocal<>();

  public static final ActionUiContext ActionUiContext = new ActionUiContext();

  private ActionUiContext() {}

  /** Fixed version of SwingUtilities.getWindowAncestor() to work with {@link JMenu} and {@link JPopupMenu}. */
  public Optional<Window> getWindowAncestor(Component c) {
    while (c != null) {
      c = getParent(c);
      if (c instanceof Window window) {
        return Optional.of(window);
      }
    }
    return Optional.empty();
  }

  public Component getParent(Component comp) {
    return ((comp instanceof JPopupMenu popupMenu) ? popupMenu.getInvoker() : comp.getParent());
  }

  public void wrap(JComponent context, Runnable runnable) {
    CONTEXT.set(getWindowAncestor(context));
    try {
      runnable.run();
    } finally {
      CONTEXT.set(null);
    }
  }

  public boolean sameContext(JComponent component) {
    final Optional<Window> context = CONTEXT.get();

    return ((context != null) && context.equals(getWindowAncestor(component)));
  }

}
