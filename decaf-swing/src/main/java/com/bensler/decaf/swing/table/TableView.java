package com.bensler.decaf.swing.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */
public class TableView<E> extends Object {

  protected final         List<TablePropertyView<E, ?>>   views_;

  public TableView(TablePropertyView<E, ?>... views) {
    this(Arrays.asList(views));
  }

  public TableView(List<TablePropertyView<E, ?>> views) {
    super();
    views_ = new ArrayList<>(views);
  }

  public String getName(int column) {
    return views_.get(column).getName();
  }

  public int getColumnCount() {
    return views_.size();
  }

  public Object getCellValue(int column, E viewable) {
    return views_.get(column).getPropertyValue(viewable);
  }

  public TablePropertyView<E, ?> getColumnView(int i) {
    return views_.get(i);
  }

  public List<TablePropertyView<E, ?>> getViews() {
    return new ArrayList<>(views_);
  }

  public boolean contains(TablePropertyView<?, ?> view) {
    return views_.contains(view);
  }

  public List<String> getDefaultVisibleViews() {
    final List<String> result = new ArrayList<String>(views_.size());

    for (TablePropertyView<E, ?> view : views_) {
      if (view.isDefaultVisible()) {
        result.add(view.getId());
      }
    }
    return result;
  }
}
