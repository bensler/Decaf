package com.bensler.decaf.swing.view;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;

import com.bensler.decaf.util.Named;
import com.bensler.decaf.util.cmp.CollatorComparator;


public class PropertyViewImpl<E, P> extends Object implements PropertyView<E, P> {

  public static class ToStringGetter<E> extends PropertyGetter<E, String> {

    public ToStringGetter() {
      super(CollatorComparator.COLLATOR_COMPARATOR);
    }

    @Override
    public String getProperty(E viewable) {
      return viewable.toString();
    }

  }

  public    final static  PropertyViewImpl<Object, String>  OBJECT    = new PropertyViewImpl<>(
    new ToStringGetter<Object>()
  );

  public    final static  PropertyViewImpl<Named, String>  NAMED     = new PropertyViewImpl<>(
    new PropertyGetter<Named, String>(CollatorComparator.COLLATOR_COMPARATOR) {
      @Override
      public String getProperty(Named viewable) {
        return viewable.getName();
      }
    }
  );

  private   final         RenderComponentFactory  compFactory_;

  private   final         CellRenderer            renderer_;

  private   final         PropertyGetter<E, P>    getter_;

  private   final         NullPolicy<E>           nullPolicy_;

  public PropertyViewImpl(
    PropertyGetter<E, P> getter
  ) {
    this(new SimpleCellRenderer(), getter);
  }

  public PropertyViewImpl(
    Icon icon, PropertyGetter<E, P> getter
  ) {
    this(new SimpleCellRenderer(icon), getter);
  }

  public PropertyViewImpl(
    CellRenderer cellRenderer, PropertyGetter<E, P> propertyGetter
  ) {
    this(cellRenderer, propertyGetter, RenderComponentFactory.DEFAULT_INSTANCE);
  }

  public PropertyViewImpl(
    CellRenderer cellRenderer,
    PropertyGetter<E, P> propertyGetter, RenderComponentFactory componentFactory
  ) {
    renderer_ = cellRenderer;
    getter_ = propertyGetter;
    compFactory_ = componentFactory;
    nullPolicy_ = new DefaultNullPolicy<E>();
  }
//
//  public PropertyViewImpl(String propertyName, PropertyView propertyView) {
//    this(propertyView.getRenderer(), new QueueGetter(propertyName, propertyView.getGetter()), propertyView.getRenderComponentFactory());
//  }
//
//  public PropertyViewImpl(PropertyGetter getter, PropertyView propertyView) {
//    this(propertyView.getRenderer(), new QueueGetter(getter, propertyView.getGetter()), propertyView.getRenderComponentFactory());
//  }

  @SuppressWarnings("unchecked")
  @Override
  public Component getTreeCellRendererComponent(
    JTree tree, Object value, boolean selected,
    boolean expanded, boolean leaf, int row, boolean hasFocus
  ) {
    final TreeRenderComponent   treeComponent = compFactory_.getTreeComponent();
    final JLabel                label;

    treeComponent.prepareForTree(tree, selected, expanded, leaf, row, hasFocus);
    label = treeComponent.getComponent();
    nullPolicy_.render((E)value, label, renderer_, getter_);
    return label;
  }

  @Override
  public Component getCellRendererComponent(
    JTable table, E viewable, Object cellValue, boolean selected,
    boolean hasFocus, int row, int column
  ) {
    final RenderComponent  tableComponent  = compFactory_.getListTableComponent();
    final JLabel           label;

    tableComponent.prepareForTable(table, selected, row, column, hasFocus);
    label = tableComponent.getComponent();
    renderer_.render(viewable, cellValue, label);
    return label;
  }

  @Override
  public Component getListCellRendererComponent(
    JList<? extends E> list, E value, int index,
    boolean selected, boolean hasFocus
  ) {
    final RenderComponent   listComponent   = compFactory_.getListTableComponent();
    final JLabel            label;

    listComponent.prepareForList(list, selected, index, hasFocus);
    label = listComponent.getComponent();
    nullPolicy_.render(value, label, renderer_, getter_);
    return label;
  }

  @Override
  public JLabel renderLabel(JLabel label, E viewable) {
    nullPolicy_.render(viewable, label, renderer_, getter_);
    return label;
  }

  @Override
  public P getProperty(E viewable) {
    return getter_.getProperty(viewable);
  }

  @Override
  public String getPropertyString(E viewable) {
    final P propertyValue = getProperty(viewable);

    return nullPolicy_.getPropertyString(propertyValue);
  }

  @Override
  public int compare(E v1, E v2) {
    return getter_.getEntityComparator().compare(v1, v2);
  }

  @Override
  public RenderComponentFactory getRenderComponentFactory() {
    return compFactory_;
  }

  @Override
  public PropertyGetter<E, P> getGetter() {
    return getter_;
  }

}
