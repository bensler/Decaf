package com.bensler.decaf.swing.table;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


/**
 */
class Column extends TableColumn {

  private   final         TablePropertyView   view_;

  private                 int                 viewIndex_;

  Column(TablePropertyView view, int newModelIndex) {
    super(newModelIndex);
    viewIndex_ = -1;
    view_ = view;
  }

  TablePropertyView getView() {
    return view_;
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
