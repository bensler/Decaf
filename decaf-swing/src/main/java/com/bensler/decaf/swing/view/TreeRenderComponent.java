package com.bensler.decaf.swing.view;

import javax.swing.JComponent;
import javax.swing.JTree;

public interface TreeRenderComponent<C extends JComponent> {

  C prepareForTree(
    JTree tree, boolean selected, boolean expanded,
    boolean leaf, int row, boolean hasFocus
  );

}
