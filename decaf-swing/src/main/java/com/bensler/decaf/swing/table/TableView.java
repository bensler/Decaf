package com.bensler.decaf.swing.table;

import java.util.Arrays;
import java.util.List;

/**
 */
public class TableView<E> extends Object {

  protected final List<TablePropertyView<E, ?>> views_;

  @SafeVarargs
  public TableView(TablePropertyView<E, ?>... views) {
    views_ = Arrays.asList(views);
  }

  public String getName(int colIndex) {
    return views_.get(colIndex).getName();
  }

  public int getColumnCount() {
    return views_.size();
  }

  public Object getCellValue(int colIndex, E entity) {
    return getColumnView(colIndex).getPropertyValue(entity);
  }

  public TablePropertyView<E, ?> getColumnView(int colIndex) {
    return views_.get(colIndex);
  }

}
