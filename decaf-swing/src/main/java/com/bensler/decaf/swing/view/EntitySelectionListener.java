package com.bensler.decaf.swing.view;

import java.util.List;


/**
 */
public interface EntitySelectionListener<E> {

  public void selectionChanged(EntityComponent<E> source, List<E> selection);

}