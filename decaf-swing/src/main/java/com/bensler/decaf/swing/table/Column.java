package com.bensler.decaf.swing.table;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


/**
 */
class Column<E> extends TableColumn {

  private final TablePropertyView<E, ?>   view_;

  private int viewIndex_;

  Column(TablePropertyView<E, ?> view, int newModelIndex) {
    super(newModelIndex);
    setHeaderValue(view.getName());
    viewIndex_ = -1;
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

  int getViewIndex() {
    return viewIndex_;
  }

  void setViewIndex(int viewIndex) {
    viewIndex_ = viewIndex;
  }

  public boolean isSortable() {
    return view_.isSortable();
  }

}
