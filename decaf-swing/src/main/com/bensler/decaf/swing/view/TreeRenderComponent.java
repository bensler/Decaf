package com.bensler.decaf.swing.view;

import javax.swing.JTree;

public interface TreeRenderComponent extends RenderComponent {

  void prepareForTree(
    JTree tree, boolean selected, boolean expanded, 
    boolean leaf, int row, boolean hasFocus
  );

}
