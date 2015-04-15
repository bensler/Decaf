package com.bensler.decaf.swing.view;

import java.util.Comparator;

import com.bensler.decaf.swing.Viewable;


public interface PropertyGetter extends Comparator<Viewable> {

  public Object getProperty(Viewable viewable);

  public boolean isSortable();
  
}
