package com.bensler.decaf.swing.tree;

import javax.swing.Icon;

public class SimpleIconProvider implements IconProvider {

  /** Icon for unexpanded non-leaf nodes. */
  private   final         Icon closedIcon_;

  /** Icon for leaf nodes. */
  private   final         Icon leafIcon_;

  /** Icon for expanded non-leaf nodes. */
  private   final         Icon openIcon_;

  public SimpleIconProvider(
    Icon closedIcon, Icon leafIcon, Icon openIcon
  ) {
    closedIcon_ = closedIcon;
    leafIcon_ = leafIcon;
    openIcon_ = openIcon;
  }
  
  public Icon getOpenIcon() {
    return openIcon_;
  }

  public Icon getClosedIcon() {
    return closedIcon_;
  }

  public Icon getLeafIcon() {
    return leafIcon_;
  }

}
