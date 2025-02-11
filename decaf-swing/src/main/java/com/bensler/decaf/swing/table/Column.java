package com.bensler.decaf.swing.table;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


/**
 */
class Column<E> extends TableColumn {

  private final TablePropertyView<E, ?>   view_;

  Column(TablePropertyView<E, ?> view, int newModelIndex) {
    super(newModelIndex);
    setHeaderValue(view.getName());
    view_ = view;
  }

  TablePropertyView<E, ?> getView() {
    return view_;
  }

  String getId() {
    return view_.getId();
  }

  @Override
  public TableCellRenderer getCellRenderer() {
    return view_;
  }

  public boolean isSortable() {
    return view_.isSortable();
  }

}
