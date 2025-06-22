package com.bensler.decaf.swing;

import java.util.List;
import java.util.Optional;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import com.bensler.decaf.swing.selection.SelectionHolder;

/**
 */
public interface EntityComponent<E> extends SelectionHolder<E> {

  public Class<E> getEntityClass();

  public JComponent getComponent();

  public JScrollPane getScrollPane();

  public Optional<E> contains(Object entity);

  public void addFocusListener(FocusListener listener);

  @FunctionalInterface
  public interface FocusListener {

    public void focusGained(EntityComponent<?> component, List<?> selection);

  }

}
