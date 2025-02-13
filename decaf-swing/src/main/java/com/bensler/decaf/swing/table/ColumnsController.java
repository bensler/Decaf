package com.bensler.decaf.swing.table;

public class ColumnsController<E> {

  private final ColumnModel<E> columnModel_;

  ColumnsController(TableView<E> view) {
    columnModel_ = new ColumnModel<>(view);
  }

  ColumnModel<E> getColumnModel() {
    return columnModel_;
  }

}
