package com.bensler.decaf.swing.tree;

import javax.swing.Icon;

public interface IconProvider {
  
  public Icon getOpenIcon();

  public Icon getClosedIcon();

  public Icon getLeafIcon();
  
}
