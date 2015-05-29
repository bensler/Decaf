package com.bensler.decaf.swing.table;

import java.awt.Component;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


/**
 */
class HeaderRenderer extends JButton implements TableCellRenderer {

  private   final         ColumnModel<?>          columnModel_;

  private   final         Map<Sorting, JButton>   sortingButtonMap_;

  HeaderRenderer(ColumnModel<?> columnModel) {
    final Insets              insets  = new Insets(0, 0, 0, 0);
    final int                 size    = getFont().getSize();

    columnModel_ = columnModel;
    sortingButtonMap_ = new HashMap<Sorting, JButton>(1);

    createButton(Sorting.NONE, size, insets);
    createButton(Sorting.ASCENDING, size, insets);
    createButton(Sorting.DESCENDING, size, insets);
  }

  private void createButton(
    Sorting sorting, int size, Insets insets
  ) {
    final JButton   button = new JButton();

    button.setMargin(insets);
    button.setHorizontalTextPosition(LEFT);
    button.setIcon(new BevelArrowIcon(size, sorting, false));
    button.setPressedIcon(new BevelArrowIcon(size, sorting, true));
    sortingButtonMap_.put(sorting, button);
  }

  @Override
  public Component getTableCellRendererComponent(
    JTable table, Object value, boolean isSelected, boolean hasFocus,
    int row, int column
  ) {
    final Sorting   sorting   = columnModel_.getSorting(column);
    final boolean   pressed   = columnModel_.isColumnPressed(column);
    final String    valueStr  = (value == null) ? "" : value.toString();
    final String    tooltip   = (valueStr.trim().length() < 1) ? null : value.toString();
    final JButton   button    = sortingButtonMap_.get(sorting);

    button.setText(valueStr);
    button.setToolTipText(tooltip);
    button.getModel().setPressed(pressed);
    button.getModel().setArmed(pressed);
    return button;
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    // iterate over the buttons
    for (JButton button : sortingButtonMap_.values()) {
      button.setEnabled(enabled);
    }
  }

}
