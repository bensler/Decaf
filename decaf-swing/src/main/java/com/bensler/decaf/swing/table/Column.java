package com.bensler.decaf.swing.table;

import javax.swing.table.TableColumn;


class Column<E> extends TableColumn {

  private final TablePropertyView<E, ?>   view_;

  Column(TablePropertyView<E, ?> view, int newModelIndex) {
    super(newModelIndex);
    view_ = view;
  }

  TablePropertyView<E, ?> getView() {
    return view_;
  }

}
