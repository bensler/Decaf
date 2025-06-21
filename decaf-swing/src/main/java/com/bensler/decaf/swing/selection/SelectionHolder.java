package com.bensler.decaf.swing.selection;

import java.util.Collection;
import java.util.List;

public interface SelectionHolder<E> {

  public List<E> getSelection();

  public E getSingleSelection();

  public void addSelectionListener(EntitySelectionListener<E> listener);

  public void clearSelection();

  public void select(Collection<E> entities);

  public void select(E entity);

}
