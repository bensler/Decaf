package com.bensler.decaf.swing.view;

import com.bensler.decaf.swing.tree.DefaultTreeCellRenderComponent;



public class RenderComponentFactory {

  public static final RenderComponentFactory INSTANCE = new RenderComponentFactory();

  private final RenderComponent listComp_;

  private RenderComponentFactory() {
    listComp_ = new DefaultCellRenderComponent();
  }

  public RenderComponent getListTableComponent() {
    return listComp_;
  }

  public TreeRenderComponent createTreeComponent() {
    return new DefaultTreeCellRenderComponent();
  }

}
