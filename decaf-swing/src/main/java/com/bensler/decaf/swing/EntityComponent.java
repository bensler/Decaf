package com.bensler.decaf.swing;

import java.awt.Color;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import com.bensler.decaf.swing.selection.SelectionHolder;

/**
 */
public interface EntityComponent<E> extends SelectionHolder<E> {

  public JComponent getComponent();

  public JScrollPane getScrollPane();

  public boolean contains(E entity);

  public void select(Collection<E> entities);

  public void select(E entity);

  public void setBackground(Color c);
  public void setToolTipText(String hint);
  public boolean isEnabled();

  @FunctionalInterface
  public interface FocusListener {

    public void focusGained(EntityComponent<?> component);

  }

}
