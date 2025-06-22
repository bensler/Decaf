package com.bensler.decaf.swing;

import java.util.List;
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

  public void addFocusListener(FocusListener<E> listener);

  @FunctionalInterface
  public interface FocusListener<E> {

    public void focusGained(EntityComponent<E> component, List<E> selection);

  }

}
