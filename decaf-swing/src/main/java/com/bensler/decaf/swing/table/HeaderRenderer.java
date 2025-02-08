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

    private final Color colorFill_;
    private final Color colorBg_;
    private final Color colorFg_;
    private final int maxSize_;
    private Optional<Pair<Sorting, Integer>> sorting_;
    private int size_;

    ArrowIcon(int size, JButton parent) {
      colorFg_ = parent.getForeground();
      colorBg_ = parent.getBackground();
      colorFill_ = new Color(
        (colorFg_.getRed()   + (2 * colorBg_.getRed())  ) / 3,
        (colorFg_.getGreen() + (2 * colorBg_.getGreen())) / 3,
        (colorFg_.getBlue()  + (2 * colorBg_.getBlue()) ) / 3
      );
      size_ = maxSize_ = size;
      sorting_ = Optional.empty();
    }

    void setSorting(Optional<Pair<Sorting, Integer>> sorting) {
      sorting_ = sorting;
      size_ = 0;
      if (sorting_.isPresent()) {
        switch (sorting_.get().getRight().intValue()) {
          case 0  : size_ = maxSize_; break;
          case 1  : size_ = Math.round((maxSize_ / 4.0f) * 3); break;
          case 2  : size_ = Math.round( maxSize_ / 2.0f     ); break;
        }
      }
//      System.out.println(size_);
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

            final int[] xCoords = new int[] {0       , 0 + size, 0 + (size / 2)};
            final int[] yCoords = new int[] {0 + size, 0 + size, 0             };

            g2d.setColor(colorFill_);
            g2d.fillPolygon(xCoords, yCoords, xCoords.length);
            g2d.setColor(colorFg_);
            g2d.drawPolygon(xCoords, yCoords, xCoords.length);
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
