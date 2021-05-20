package com.bensler.decaf.swing.table;

import javax.swing.JComponent;

public interface TableRowView<E> {

  public JComponent prepareRenderer(
    TableComponent<E> table, JComponent component, E viewable, int row, boolean selected
  );

  public static final class Nop<E> extends Object implements TableRowView<E> {

    public Nop() {}

    @Override
    public JComponent prepareRenderer(
      TableComponent<E> table, JComponent component, E viewable, int row, boolean selected
    ) {
      return component;
    }

  }

}
