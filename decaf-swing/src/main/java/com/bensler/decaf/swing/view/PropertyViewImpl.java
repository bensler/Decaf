package com.bensler.decaf.swing.view;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;

import com.bensler.decaf.swing.Viewable;
import com.bensler.decaf.swing.view.RenderComponentFactory.Target;



public class PropertyViewImpl<E> extends Object implements PropertyView<E> {

  public static class ToStringGetter<E> extends StringGetter<E> {

    @Override
    public String getProperty(E viewable) {
      return viewable.toString();
    }

  }

  public    final static  PropertyViewImpl<Object>  OBJECT    = new PropertyViewImpl<>(
    new ToStringGetter()
  );

  public    final static  PropertyViewImpl<Object>  KEY       = new PropertyViewImpl<>("key_");

  public    final static  PropertyViewImpl<Object>  NAME      = new PropertyViewImpl<>("name");

  private   final         RenderComponentFactory  compFactory_;

  private   final         CellRenderer            renderer_;

  private   final         PropertyGetter          getter_;

  private   final         NullPolicy              nullPolicy_;

  public PropertyViewImpl(
    PropertyGetter getter
  ) {
    this(new SimpleCellRenderer(), getter);
  }

  public PropertyViewImpl(
    String propertyName
  ) {
    this(new SimpleCellRenderer(), new NamePropertyGetter(propertyName));
  }

  public PropertyViewImpl(
    Icon icon, PropertyGetter getter
  ) {
    this(new SimpleCellRenderer(icon), getter);
  }

  public PropertyViewImpl(Icon icon, String propertyName) {
    this(new SimpleCellRenderer(icon), new NamePropertyGetter(propertyName));
  }

  public PropertyViewImpl(
    CellRenderer cellRenderer, String propertyName
  ) {
    this(cellRenderer, new NamePropertyGetter(propertyName));
  }

  public PropertyViewImpl(
    CellRenderer cellRenderer, PropertyGetter propertyGetter
  ) {
    this(cellRenderer, propertyGetter, RenderComponentFactory.DEFAULT_INSTANCE);
  }

  public PropertyViewImpl(
    CellRenderer cellRenderer,
    PropertyGetter propertyGetter, RenderComponentFactory componentFactory
  ) {
    renderer_ = cellRenderer;
    getter_ = propertyGetter;
    compFactory_ = componentFactory;
    nullPolicy_ = new DefaultNullPolicy();
  }
//
//  public PropertyViewImpl(String propertyName, PropertyView propertyView) {
//    this(propertyView.getRenderer(), new QueueGetter(propertyName, propertyView.getGetter()), propertyView.getRenderComponentFactory());
//  }
//
//  public PropertyViewImpl(PropertyGetter getter, PropertyView propertyView) {
//    this(propertyView.getRenderer(), new QueueGetter(getter, propertyView.getGetter()), propertyView.getRenderComponentFactory());
//  }

  protected PropertyViewImpl() {
    this(new SimpleCellRenderer(), (PropertyGetter)null);
  }

  @Override
  public Component getTreeCellRendererComponent(
    JTree tree, Object value, boolean selected,
    boolean expanded, boolean leaf, int row, boolean hasFocus
  ) {
    final TreeRenderComponent   treeComponent = compFactory_.getTreeComponent();
    final JLabel                label;

    treeComponent.prepareForTree(tree, selected, expanded, leaf, row, hasFocus);
    label = treeComponent.getComponent();
    nullPolicy_.render(value, label, getRenderer(), getter_);
    compFactory_.afterRendering(Target.TREE, label, (Viewable)value);
    return label;
  }

  @Override
  public Component getCellRendererComponent(
    JTable table, Viewable viewable, Object cellValue, boolean selected,
    boolean hasFocus, int row, int column
  ) {
    final TableRenderComponent  tableComponent  = compFactory_.getTableComponent();
    final JLabel                label;

    tableComponent.prepareForTable(table, selected, row, column, hasFocus);
    label = tableComponent.getComponent();
    getRenderer().render(viewable, cellValue, label);
    compFactory_.afterRendering(Target.TABLE, label, viewable);
    return label;
  }

  @Override
  public Component getListCellRendererComponent(
    JList list, Object value, int index,
    boolean selected, boolean hasFocus
  ) {
    final ListRenderComponent   listComponent   = compFactory_.getListComponent();
    final JLabel                label;

    listComponent.prepareForList(list, selected, index, hasFocus);
    label = listComponent.getComponent();
    nullPolicy_.render(value, label, getRenderer(), getter_);
    compFactory_.afterRendering(Target.LIST, label, (Viewable)value);
    return listComponent.getComponent();
  }

  @Override
  public JLabel renderLabel(JLabel label, Viewable viewable) {
    nullPolicy_.render(viewable, label, getRenderer(), getter_);
    compFactory_.afterRendering(Target.LABEL, label, viewable);
    return label;
  }

  @Override
  public Object getProperty(Viewable viewable) {
    return getter_.getProperty(viewable);
  }

  @Override
  public String getPropertyString(Viewable viewable) {
    final Object propertyValue = getProperty(viewable);

    return nullPolicy_.getPropertyString(propertyValue);
  }

  @Override
  public int compare(E v1, E v2) {
    return getter_.compare(v1, v2);
  }

  @Override
  public CellRenderer getRenderer() {
    return renderer_;
  }

  @Override
  public RenderComponentFactory getRenderComponentFactory() {
    return compFactory_;
  }

  @Override
  public PropertyGetter getGetter() {
    return getter_;
  }

}
