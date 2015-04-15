package com.bensler.decaf.swing.view;

import java.util.List;

import com.bensler.decaf.swing.Viewable;


/**
 */
public interface EntitySelectionListener {

  public void selectionChanged(EntityComponent source, List<? extends Viewable> selection);

}