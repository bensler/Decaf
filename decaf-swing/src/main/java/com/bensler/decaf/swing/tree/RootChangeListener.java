package com.bensler.decaf.swing.tree;

import java.util.EventListener;

/** Provides a method called when a TreeModel stops or starts using a synthetic
 * root. A EntityTree should listen on these events to switch its tree components
 * root visible flag. */
public interface RootChangeListener extends EventListener {

  /** Called when a TreeModel stops or starts using a synthetic root. */
  public void rootChanged(RootProvider rootProvider);

  public interface RootProvider {

    public boolean hasSyntheticRoot();

  }

}