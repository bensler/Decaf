package com.bensler.decaf.swing.view;

import com.bensler.decaf.swing.tree.DefaultTreeCellRenderComponent;



public class DefaultRenderComponentFactory implements RenderComponentFactory {

  private   final         ListRenderComponent   listComp_;

  private   final         TableRenderComponent  tableComp_;

  private   final         TreeRenderComponent   treeComp_;

  public DefaultRenderComponentFactory() {
    final DefaultCellRenderComponent tableListComponent = new DefaultCellRenderComponent();

    listComp_ = tableListComponent;
    tableComp_ = tableListComponent;
    treeComp_ = new DefaultTreeCellRenderComponent();
  }

  @Override
  public ListRenderComponent getListComponent() {
    return listComp_;
  }

  @Override
  public TableRenderComponent getTableComponent() {
    return tableComp_;
  }

  @Override
  public TreeRenderComponent getTreeComponent() {
    return treeComp_;
  }

}
