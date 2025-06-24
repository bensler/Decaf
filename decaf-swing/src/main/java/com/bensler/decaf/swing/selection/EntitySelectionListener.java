package com.bensler.decaf.swing.selection;

import java.util.List;


/**
 */
public interface EntitySelectionListener<E> {

  public void selectionChanged(SelectionHolder<E> source, List<E> selection);

}