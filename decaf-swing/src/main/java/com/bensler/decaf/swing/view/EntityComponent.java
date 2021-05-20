package com.bensler.decaf.swing.view;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

/**
 */
public interface EntityComponent<E> {

  public JComponent getComponent();

  public JScrollPane getScrollPane();

  public List<E> getSelection();

  public E getSingleSelection();

  public void setSelectionListener(EntitySelectionListener<? super E> listener);

  public boolean contains(E entity);

  public void select(Collection<E> entities);

  public void select(E entity);

  public void clearSelection();

  public void setBackground(Color c);
  public void setToolTipText(String hint);
  public boolean isEnabled();

  public interface FocusListener {

    public void focusGained(EntityComponent<?> component);

  }

}
