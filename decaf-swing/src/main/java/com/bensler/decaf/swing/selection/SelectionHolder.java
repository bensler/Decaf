package com.bensler.decaf.swing.selection;

import java.util.List;

public interface SelectionHolder<E> {

  public List<E> getSelection();

  public E getSingleSelection();

  public void setSelectionListener(EntitySelectionListener<E> listener);

  public void clearSelection();

}
