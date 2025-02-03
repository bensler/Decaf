package com.bensler.decaf.swing.table;

import java.awt.Component;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.bensler.decaf.util.Pair;


/**
 */
class HeaderRenderer<E> extends JButton implements TableCellRenderer {

  private final TableModel<?> model_;
  private final ColumnModel<?> columnModel_;
  private final Map<Sorting, JButton> sortingButtonMap_;

  HeaderRenderer(TableModel<E> model, ColumnModel<?> columnModel) {
    final Insets insets = new Insets(0, 0, 0, 0);
    final int size = getFont().getSize();

    model_ = model;
    columnModel_ = columnModel;
    sortingButtonMap_ = new HashMap<>(1);

    createButton(Sorting.ASCENDING, size, insets);
    createButton(Sorting.DESCENDING, size, insets);
    createButton(null, size, insets);
  }

  private void createButton(
    Sorting sorting, int size, Insets insets
  ) {
    final JButton button = new JButton();

    button.setMargin(insets);
    button.setHorizontalTextPosition(LEFT);
    button.setIcon(new BevelArrowIcon(size, sorting, false));
    button.setPressedIcon(new BevelArrowIcon(size, sorting, true));
    sortingButtonMap_.put(sorting, button);
  }

  @Override
  public Component getTableCellRendererComponent(
    JTable table, Object value, boolean isSelected, boolean hasFocus,
    int row, int colIndex
  ) {
    final Column column = columnModel_.getColumn(colIndex);
    final Optional<Pair<Sorting, Integer>> sorting = model_.getSorting(column);
    final boolean pressed = columnModel_.isColumnPressed(colIndex);
    final String valueStr = (value == null) ? "" : value.toString();
    final String tooltip = (valueStr.trim().length() < 1) ? null : value.toString();
    final JButton button = sortingButtonMap_.get(sorting.map(Pair::getLeft).orElse(null));

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
