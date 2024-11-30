package com.bensler.decaf.swing;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import com.bensler.decaf.swing.selection.SelectionHolder;

/**
 */
public interface EntityComponent<E> extends SelectionHolder<E> {

  public JComponent getComponent();

  public JScrollPane getScrollPane();

  public boolean contains(E entity);

  public boolean isEnabled();

  @FunctionalInterface
  public interface FocusListener {

    public void focusGained(EntityComponent<?> component);

  }

}
