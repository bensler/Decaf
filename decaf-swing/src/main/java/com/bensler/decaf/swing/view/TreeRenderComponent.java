package com.bensler.decaf.swing.view;

import javax.swing.JLabel;
import javax.swing.JTree;

public interface TreeRenderComponent {

  public JLabel getComponent();

  void prepareForTree(
    JTree tree, boolean selected, boolean expanded,
    boolean leaf, int row, boolean hasFocus
  );

}
