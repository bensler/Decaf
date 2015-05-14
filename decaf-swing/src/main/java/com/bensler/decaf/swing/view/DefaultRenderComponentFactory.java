package com.bensler.decaf.swing.view;

import javax.swing.JComponent;

import com.bensler.decaf.swing.tree.DefaultTreeCellRenderComponent;



public class DefaultRenderComponentFactory implements RenderComponentFactory {

  private   final         ListRenderComponent   listComp_;

  private   final         TableRenderComponent  tableComp_;

  private   final         TreeRenderComponent   treeComp_;

  private   final         PostProcessor         processor_;

  public DefaultRenderComponentFactory() {
    this(new DefaultCellRenderComponent(), new DefaultTreeCellRenderComponent());
  }

  public DefaultRenderComponentFactory(PostProcessor processor) {
    this(new DefaultCellRenderComponent(), new DefaultTreeCellRenderComponent(), processor);
  }

  public DefaultRenderComponentFactory(
    RendererBase tableListComponent, TreeRenderComponent treeComponent
  ) {
    this((ListRenderComponent)tableListComponent, (TableRenderComponent)tableListComponent, treeComponent, null);
  }

  public DefaultRenderComponentFactory(
    RendererBase tableListComponent, TreeRenderComponent treeComponent, PostProcessor processor
  ) {
    this((ListRenderComponent)tableListComponent, (TableRenderComponent)tableListComponent, treeComponent, processor);
  }

  public DefaultRenderComponentFactory(
    ListRenderComponent listComponent, TableRenderComponent tableComponent,
    TreeRenderComponent treeComponent, PostProcessor processor
  ) {
    listComp_ = listComponent;
    tableComp_ = tableComponent;
    treeComp_ = treeComponent;
    processor_ = processor;
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

  @Override
  public void afterRendering(Target target, JComponent rendererComponent, Object viewable) {
    if (processor_ != null) {
      processor_.afterRendering(target, rendererComponent, viewable);
    }
  }

  public static interface PostProcessor {

    public void afterRendering(Target target, JComponent rendererComponent, Object viewable);

  }

}
