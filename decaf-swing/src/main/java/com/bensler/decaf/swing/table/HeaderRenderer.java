package com.bensler.decaf.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.bensler.decaf.util.Pair;


/**
 */
class HeaderRenderer<E> extends JButton implements TableCellRenderer {

  private final TableModel<?> model_;
  private final ColumnModel<?> columnModel_;
  private final JButton button_;
  private final ArrowIcon arrowIcon_;

  HeaderRenderer(TableModel<E> model, ColumnModel<?> columnModel) {
    model_ = model;
    columnModel_ = columnModel;
    button_ = new JButton();
    button_.setMargin(new Insets(0, 0, 0, 0));
    button_.setHorizontalTextPosition(LEFT);
    button_.setIcon(arrowIcon_ = new ArrowIcon(getFont().getSize(), button_));
  }

  @Override
  public Component getTableCellRendererComponent(
    JTable table, Object value, boolean isSelected, boolean hasFocus,
    int row, int colIndex
  ) {
    final Column<?> column = columnModel_.getColumn(colIndex);
    final Optional<Pair<Sorting, Integer>> sorting = model_.getSorting(column);

    // System.out.println("HeaderRenderer:%s:%s".formatted(value, sorting));

    final boolean pressed = columnModel_.isColumnPressed(colIndex);
    final String valueStr = (value == null) ? "" : value.toString();
    final String tooltip = (valueStr.trim().length() < 1) ? null : valueStr;
    final JButton button = button_;
    final ButtonModel buttonModel = button.getModel();

    button.setText(valueStr);
    button.setToolTipText(tooltip);
    buttonModel.setPressed(pressed);
    buttonModel.setArmed(pressed);
    arrowIcon_.setSorting(sorting);
    return button;
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    button_.setEnabled(enabled);
  }

  static class ArrowIcon implements Icon {

    private final Color color_;
    private final int size_;
    private Optional<Pair<Sorting, Integer>> sorting_;

    ArrowIcon(int size, JButton parent) {
      final Color fg = parent.getForeground();
      final Color bg = parent.getBackground();

      color_ = new Color(
        ((1 * fg.getRed())   + bg.getRed()  ) / 2,
        ((1 * fg.getGreen()) + bg.getGreen()) / 2,
        ((1 * fg.getBlue())  + bg.getBlue() ) / 2
      );
      size_ = size;
      sorting_ = Optional.empty();
    }

    void setSorting(Optional<Pair<Sorting, Integer>> sorting) {
      sorting_ = sorting;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      if (sorting_.isPresent()) {
        final Pair<Sorting, Integer> pair = sorting_.get();
        final Integer prio = pair.getRight();

        if (prio < 3) {
          final Sorting sorting = pair.getLeft();
          final Graphics2D g2d = (Graphics2D) g.create(x, y, size_, size_);

          try {
            final int size = size_ - 1;

            if (sorting == Sorting.DESCENDING) {
              g2d.scale(1, -1);
              g2d.translate(0, -size_ + 1);
            }
            g2d.setColor(color_);
            g2d.fillPolygon(
              new int[] {0       , 0 + size, 0 + (size / 2)},
              new int[] {0 + size, 0 + size, 0            },
              3
            );
          } finally {
            g2d.dispose();
          }
        }
      }
    }

    @Override
    public int getIconWidth() {
      return size_;
    }

    @Override
    public int getIconHeight() {
      return size_;
    }

  }
}
