package com.bensler.decaf.swing.view;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

/**
 */
public interface EntityComponent <E> {

  public JComponent getComponent();

  public JScrollPane getScrollPane();

  public List<E> getSelection();

  public E getSingleSelection();

//  public void addSelectionListener(EntitySelectionListener listener);
//  public void removeSelectionListener(EntitySelectionListener listener);

  public boolean contains(Object entity);

  public void select(Collection<?> entities);

  public void select(Object entity);

  public void clearSelection();

  public void setBackground(Color c);
  public void setToolTipText(String hint);
  public boolean isEnabled();

  public interface FocusListener {

    public void focusGained(EntityComponent<?> component);

  }

}
