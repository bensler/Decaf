package com.bensler.decaf.swing.view;

import com.bensler.decaf.swing.tree.DefaultTreeCellRenderComponent;



public class DefaultRenderComponentFactory implements RenderComponentFactory {

  private   final         ListRenderComponent   listComp_;

  private   final         TableRenderComponent  tableComp_;

  private   final         TreeRenderComponent   treeComp_;

  public DefaultRenderComponentFactory() {
    this(new DefaultCellRenderComponent(), new DefaultTreeCellRenderComponent());
  }

  public DefaultRenderComponentFactory(
    RendererLabel tableListComponent, TreeRenderComponent treeComponent
  ) {
    this((ListRenderComponent)tableListComponent, (TableRenderComponent)tableListComponent, treeComponent);
  }

  public DefaultRenderComponentFactory(
    ListRenderComponent listComponent, TableRenderComponent tableComponent,
    TreeRenderComponent treeComponent
  ) {
    listComp_ = listComponent;
    tableComp_ = tableComponent;
    treeComp_ = treeComponent;
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
