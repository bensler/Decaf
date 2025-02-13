package com.bensler.decaf.swing.table;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.table.JTableHeader;

import com.bensler.decaf.util.function.ForEachMapperAdapter;

public class ColumnsController<E> {

  private final TableModel<E> tableModel_;
  private final ColumnModel<E> columnModel_;
  private final HeaderRenderer<E> headerRenderer_;
  private final Map<TablePropertyView<E, ?>, Column<E>> viewColumnMap_;

  ColumnsController(TableModel<E> tableModel) {
    final TableView<E> view = (tableModel_ = tableModel).getView();

    columnModel_ = new ColumnModel<>(view);
    headerRenderer_ = new HeaderRenderer<>(tableModel, columnModel_);
    viewColumnMap_ = IntStream.range(0, view.getColumnCount())
      .mapToObj(i -> new Column<>(view.getColumnView(i), i))
      .map(ForEachMapperAdapter.forEachMapper(columnModel_::addColumn))
      .collect(Collectors.toMap(Column::getView, Function.identity()));

    final int[] sizes = new int[view.getColumnCount()];
          int   sum   = 0;

    for (int i = 0; i < columnModel_.getColumnCount(); i++) {
      final String name = columnModel_.getColumn(i).getView().getName();

      sizes[i] = headerRenderer_.getTableCellRendererComponent(
        null, name, false, false, -1, i
      ).getPreferredSize().width * 10;
      sum += sizes[i];
    }
    columnModel_.setPrefSizes(sizes);
    columnModel_.updateColPrefSizes(sum);
  }

  ColumnModel<E> getColumnModel() {
    return columnModel_;
  }

  void configure(JTableHeader tableHeader, TableSelectionController<E> selectionCtrl) {
    final HeaderMouseListener mouseListener = new HeaderMouseListener(tableHeader, selectionCtrl);

    tableHeader.setDefaultRenderer(headerRenderer_);
    tableHeader.addMouseListener(mouseListener);
    tableHeader.addMouseMotionListener(mouseListener);
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
        if (column.isSortable()) {
          tableModel_.sortByColumn(column, tableModel_.getNewSorting(column));
        }
      }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      if (columnModel_.setPressedColumn(null)) {
        tableHeader_.repaint();
      }
    }

    @Override
    public void mousePressed(MouseEvent evt) {
      if (
        (evt.getButton() == MouseEvent.BUTTON1)
        && (getResizeColumnIndex(evt.getPoint()) < 0)
      ) {
        final Column<E> column = columnModel_.getColumn(tableHeader_.columnAtPoint(evt.getPoint()));

        if (column.isSortable() && columnModel_.setPressedColumn(column)) {
          tableHeader_.repaint();
        }
      }
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
      if (
        (evt.getButton() == MouseEvent.BUTTON1)
        && (getResizeColumnIndex(evt.getPoint()) < 0)
        && columnModel_.setPressedColumn(null)
      ) {
        tableHeader_.repaint();
      }
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
      final Point point             = evt.getPoint();
      final int   resizeColumnIndex = getResizeColumnIndex(point);

      if ((resizeColumnIndex < 0) && (evt.getButton() == MouseEvent.BUTTON1)) {
        sortByColumn(columnModel_.getColumn(tableHeader_.columnAtPoint(evt.getPoint())));
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
