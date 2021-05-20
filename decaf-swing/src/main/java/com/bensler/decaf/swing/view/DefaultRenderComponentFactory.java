package com.bensler.decaf.swing.view;

import com.bensler.decaf.swing.tree.DefaultTreeCellRenderComponent;



public class DefaultRenderComponentFactory implements RenderComponentFactory {

  private   final         RenderComponent   listComp_;

  private   final         TreeRenderComponent   treeComp_;

  public DefaultRenderComponentFactory() {
    listComp_ = new DefaultCellRenderComponent();
    treeComp_ = new DefaultTreeCellRenderComponent();
  }

  @Override
  public RenderComponent getListTableComponent() {
    return listComp_;
  }

  @Override
  public TreeRenderComponent getTreeComponent() {
    return treeComp_;
  }

}
