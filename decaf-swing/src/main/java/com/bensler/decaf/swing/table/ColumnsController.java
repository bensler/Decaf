package com.bensler.decaf.swing.table;

import static java.util.function.Function.identity;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.bensler.decaf.util.function.ForEachMapperAdapter;

public class ColumnsController<E> {

  private final TableModel<E> tableModel_;
  private final TableColumnModel columnModel_;
  private final HeaderRenderer<E> headerRenderer_;

  private final Map<TablePropertyView<E, ?>, Column<E>> viewColumnMap_;
  private final Map<Column<E>, TablePropertyView<E, ?>> columnViewMap_;
  private final Map<Column<E>, Column<E>> columnMap_;
  private Column<E> pressedColumn_;
  private int[] prefSizes_;

  ColumnsController(TableModel<E> tableModel) {
    final TableView<E> view = (tableModel_ = tableModel).getView();

    columnModel_ = new DefaultTableColumnModel();
    headerRenderer_ = new HeaderRenderer<>(tableModel, this);
    pressedColumn_ = null;
    viewColumnMap_ = IntStream.range(0, view.getColumnCount())
      .mapToObj(i -> createColumn(view, i))
      .map(ForEachMapperAdapter.forEachMapper(columnModel_::addColumn))
      .collect(Collectors.toMap(Column::getView, Function.identity()));
    columnViewMap_ = viewColumnMap_.entrySet().stream()
      .collect(Collectors.toMap(Entry::getValue, Entry::getKey));
    columnMap_ = columnViewMap_.keySet().stream()
      .collect(Collectors.toMap(identity(), identity()));

    final int[] sizes = new int[view.getColumnCount()];
          int   sum   = 0;

    for (int i = 0; i < view.getColumnCount(); i++) {
      final String name = view.getColumnView(i).getName();

      sizes[i] = headerRenderer_.getTableCellRendererComponent(
        null, name, false, false, -1, i
      ).getPreferredSize().width * 10;
      sum += sizes[i];
    }
    setPrefSizes(sizes, sum);
  }

  private static <E> Column<E> createColumn(TableView<E> view, int index) {
    final TablePropertyView<E, ?> columnView = view.getColumnView(index);
    final Column<E> column = new Column<>(columnView, index);

    column.setHeaderValue(columnView.getName());
    column.setCellRenderer(columnView);
    return column;
  }

  String getSizes() {
    return IntStream.range(0, columnModel_.getColumnCount())
      .mapToObj(this::getColumn)
      .map(column -> columnViewMap_.get(column).getId() + ":" + column.getWidth())
      .collect(Collectors.joining(","));
  }

  void setPrefSizes(int[] sizes, int sum) {
    prefSizes_ = sizes;
    final int   prefWidth = getPrefWidth();

    if ((sum > 0) && (prefWidth > 0)) {
      final float ratio = ((float)sum) / prefWidth;

      if (prefSizes_.length == columnModel_.getColumnCount()) {
        for (int i = 0; i < prefSizes_.length; i++) {
          getColumn(i).setPreferredWidth(Math.round(prefSizes_[i] * ratio));
        }
      }
    }

  }

  private int getPrefWidth() {
    int   prefSizeSum = 0;

    for (int i = 0; i < prefSizes_.length; i++) {
      prefSizeSum += prefSizes_[i];
    }
    return prefSizeSum;
  }

  TableColumn getColumn(int col) {
    return columnModel_.getColumn(col);
  }

  boolean isColumnPressed(int col) {
    return (pressedColumn_ == columnModel_.getColumn(col));
  }

  void setPressedColumn(Column<E> column, JTableHeader tableHeader) {
    final boolean change = (column != pressedColumn_) && (
      (column == null) || (viewColumnMap_.containsValue(column))
    );

    if (change) {
      pressedColumn_ = column;
      tableHeader.repaint();
    }
  }

  TableColumnModel getColumnModel() {
    return columnModel_;
  }

  Column<E> getEColumn(int colIndex) {
    return columnMap_.get(columnModel_.getColumn(colIndex));
  }

  void configure(JTableHeader tableHeader, TableSelectionController<E> selectionCtrl) {
    final HeaderMouseListener mouseListener = new HeaderMouseListener(tableHeader, selectionCtrl);

    tableHeader.setDefaultRenderer(headerRenderer_);
    tableHeader.addMouseListener(mouseListener);
    tableHeader.addMouseMotionListener(mouseListener);
  }

  Map<String, Column<E>> getColumnsById() {
    return viewColumnMap_.entrySet().stream()
      .collect(Collectors.toMap(entry -> entry.getKey().getId(), Entry::getValue));
  }

  private class HeaderMouseListener extends MouseAdapter {

    private final JTableHeader tableHeader_;
    private final TableSelectionController<E> selectionCtrl_;

    HeaderMouseListener(JTableHeader tableHeader, TableSelectionController<E> selectionCtrl) {
      tableHeader_ = tableHeader;
      selectionCtrl_ = selectionCtrl;
    }

    private void sortByColumn(Column<E> column) {
      try (var s = selectionCtrl_.createSelectionKeeper()) {
        if (columnViewMap_.get(column).isSortable()) {
          tableModel_.sortByColumn(column, tableModel_.getNewSorting(column));
        }
      }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      setPressedColumn(null, tableHeader_);
    }

    @Override
    public void mousePressed(MouseEvent evt) {
      if (
        (evt.getButton() == MouseEvent.BUTTON1)
        && (getResizeColumnIndex(evt.getPoint()) < 0)
      ) {
        final Column<E> column = getEColumn(tableHeader_.columnAtPoint(evt.getPoint()));

        if (columnViewMap_.get(column).isSortable()) {
          setPressedColumn(column, tableHeader_);
        }
      }
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
      if (
        (evt.getButton() == MouseEvent.BUTTON1)
        && (getResizeColumnIndex(evt.getPoint()) < 0)
      ) {
        setPressedColumn(null, tableHeader_);
      }
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
      final Point point             = evt.getPoint();
      final int   resizeColumnIndex = getResizeColumnIndex(point);

      if ((resizeColumnIndex < 0) && (evt.getButton() == MouseEvent.BUTTON1)) {
        sortByColumn(getEColumn(tableHeader_.columnAtPoint(evt.getPoint())));
      }
    }

    private int getResizeColumnIndex(Point point) {
      int columnIndex = tableHeader_.columnAtPoint(point);

      if (columnIndex >= 0) {
        final Rectangle   rect = tableHeader_.getHeaderRect(columnIndex);

        rect.grow(-3, 0);
        if (rect.contains(point)) {
          columnIndex = -1;
        } else {
          final int center = rect.x + (rect.width / 2);

          columnIndex += (
            (tableHeader_.getComponentOrientation().isLeftToRight() ^ (point.x < center)) ? 0 : -1
          );
        }
      }
      return columnIndex;
    }

  }

}
