package com.bensler.decaf.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
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
  private final MyIcon buttonIcon_;

  HeaderRenderer(TableModel<E> model, ColumnModel<?> columnModel) {
    final int size = getFont().getSize();

    model_ = model;
    columnModel_ = columnModel;
    button_ = new JButton();
    button_.setMargin(new Insets(0, 0, 0, 0));
    button_.setHorizontalTextPosition(LEFT);
    button_.setIcon(buttonIcon_ = new MyIcon(size));
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
    buttonIcon_.setSorting(sorting);
    return button;
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    button_.setEnabled(enabled);
  }

  static class MyIcon implements Icon {

    private final int size_;
    private Optional<Pair<Sorting, Integer>> sorting_;

    MyIcon(int size) {
      size_ = size;
      sorting_ = Optional.empty();
    }

    void setSorting(Optional<Pair<Sorting, Integer>> sorting) {
      sorting_ = sorting;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      g.setColor(Color.RED);
      g.drawRect(x, y, size_, size_);

      if (sorting_.isEmpty()) {

      } else {
        final Pair<Sorting, Integer> pair = sorting_.get();
        final Sorting sorting = pair.getLeft();
        final Integer prio = pair.getRight();

        g.setColor(Color.GREEN);
        if (sorting == Sorting.ASCENDING) {
          g.drawPolygon(
            new int[] {x        , x + size_, x + (size_ / 2)},
            new int[] {y + size_, y + size_, y              },
            3
          );
        } else {
          // (sorting == Sorting.DESCENDING)
          g.drawPolygon(
            new int[] {x, x + size_, x + (size_ / 2)},
            new int[] {y, y        , y + size_      },
            3
          );
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
