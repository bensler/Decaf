package com.bensler.decaf.swing.view;


public interface RenderComponentFactory {

  public    final static  RenderComponentFactory    DEFAULT_INSTANCE = new DefaultRenderComponentFactory();

  public TreeRenderComponent createTreeComponent();

  public RenderComponent getListTableComponent();

}
