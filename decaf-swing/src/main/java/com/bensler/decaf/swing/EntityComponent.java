package com.bensler.decaf.swing;

import java.util.Optional;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import com.bensler.decaf.swing.selection.SelectionHolder;

/**
 */
public interface EntityComponent<E> extends SelectionHolder<E> {

  public JComponent getComponent();

  public JScrollPane getScrollPane();

  public Optional<E> contains(Object entity);

  public boolean isEnabled();

  @FunctionalInterface
  public interface FocusListener {

    public void focusGained(EntityComponent<?> component);

  }

}
