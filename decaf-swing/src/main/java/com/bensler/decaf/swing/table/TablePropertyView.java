package com.bensler.decaf.swing.table;

import java.awt.Component;
import java.util.Comparator;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.bensler.decaf.swing.view.PropertyView;
import com.bensler.decaf.util.Named;


public final class TablePropertyView<E, P> extends Object
implements TableCellRenderer, Named, Comparator<E> {

  private   final           String            id_;
  private   final           String            name_;

  private   final           boolean           defaultVisible_;

  private   final           PropertyView<? super E, P>  propertyView_;

  public TablePropertyView(
    String key, String name, PropertyView<? super E, P> propertyView
  ) {
    this(key, name, true, propertyView);
  }

  public TablePropertyView(
    String key, String name, boolean defaultVisible, PropertyView<? super E, P> propertyView
  ) {
    id_ = key;
    name_ = name;
    defaultVisible_ = defaultVisible;
    propertyView_ = propertyView;
  }

  public String getId() {
    return id_;
  }

  public boolean isDefaultVisible() {
    return defaultVisible_;
  }

  public P getPropertyValue(E viewable) {
    return propertyView_.getProperty(viewable);
  }

  @Override
  public Component getTableCellRendererComponent(
    JTable table, Object value,
    boolean selected, boolean hasFocus, int row, int column
  ) {
    throw new RuntimeException("Should not be called");
  }

  public Component getCellRendererComponent(
    JTable table, E viewable, Object cellValue,
    int row, int column, boolean selected
  ) {
    return propertyView_.getCellRendererComponent(
      table, viewable, cellValue, selected, false, row, column
    );
  }

  @Override
  public String getName() {
    return name_;
  }

  @Override
  public int compare(E o1, E o2) {
    return propertyView_.compare(o1, o2);
  }

  public boolean isSortable() {
    return true;//propertyView_.isSortable();
  }

  public PropertyView<? super E, P> getPropertyView() {
    return propertyView_;
  }

}
