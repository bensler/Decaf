package com.bensler.decaf.swing.view;

import javax.swing.JComponent;

import com.bensler.decaf.swing.Viewable;

public interface RenderComponentFactory {

  public    final static  RenderComponentFactory    DEFAULT_INSTANCE = new DefaultRenderComponentFactory();

  public TreeRenderComponent getTreeComponent();

  public TableRenderComponent getTableComponent();

  public ListRenderComponent getListComponent();
  
  public void afterRendering(Target target, JComponent rendererComponent, Viewable viewable);

  public enum Target {
    
    TREE,
    TABLE,
    LIST,
    LABEL;
    
  }

}
