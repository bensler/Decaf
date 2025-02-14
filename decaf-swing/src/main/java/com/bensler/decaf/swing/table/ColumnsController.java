package com.bensler.decaf.swing.table;

import static com.bensler.decaf.util.function.ForEachMapperAdapter.forEachMapper;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.bensler.decaf.util.Pair;

public class ColumnsController<E> {

  private final TableModel<E> tableModel_;
  private final TableColumnModel columnModel_;
  private final HeaderRenderer<E> headerRenderer_;

  private final Map<String, TableColumn> viewIdColumnMap_;
  private final Map<TableColumn, TablePropertyView<E, ?>> columnViewMap_;
  private TableColumn pressedColumn_;
  private int[] prefSizes_;

  ColumnsController(TableModel<E> tableModel) {
    final TableView<E> view = (tableModel_ = tableModel).getView();

    columnModel_ = new DefaultTableColumnModel();
    headerRenderer_ = new HeaderRenderer<>(tableModel, this);
    pressedColumn_ = null;
    columnViewMap_ = IntStream.range(0, view.getColumnCount())
      .mapToObj(i -> createColumn(view, i))
      .map(forEachMapper(pair -> columnModel_.addColumn(pair.getRight())))
      .collect(Collectors.toMap(Pair::getRight, Pair::getLeft));
    viewIdColumnMap_ = columnViewMap_.entrySet().stream()
      .collect(Collectors.toMap(entry -> entry.getValue().getId(), Entry::getKey));

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

  private static <E> Pair<TablePropertyView<E, ?>, TableColumn> createColumn(TableView<E> view, int index) {
    final TablePropertyView<E, ?> columnView = view.getColumnView(index);
    final TableColumn column = new TableColumn(index);

    column.setHeaderValue(columnView.getName());
    column.setCellRenderer(columnView);
    return new Pair<>(columnView, column);
  }

  TablePropertyView<E, ?> getView(TableColumn column) {
    return columnViewMap_.get(column);
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

  Optional<TableColumn> getColumn(String colId) {
    return Optional.ofNullable(viewIdColumnMap_.get(colId));
  }

  boolean isColumnPressed(int col) {
    return (pressedColumn_ == columnModel_.getColumn(col));
  }

  void setPressedColumn(TableColumn column, JTableHeader tableHeader) {
    final boolean change = (column != pressedColumn_) && (
      (column == null) || (columnViewMap_.containsKey(column))
    );

    if (change) {
      pressedColumn_ = column;
      tableHeader.repaint();
    }
  }

  TableColumnModel getColumnModel() {
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

    private void sortByColumn(TableColumn column) {
      try (var s = selectionCtrl_.createSelectionKeeper()) {
        final TablePropertyView<E, ?> propertyView = columnViewMap_.get(column);

        if (propertyView.isSortable()) {
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
        final TableColumn column = columnModel_.getColumn(tableHeader_.columnAtPoint(evt.getPoint()));

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
