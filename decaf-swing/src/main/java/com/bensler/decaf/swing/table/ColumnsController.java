package com.bensler.decaf.swing.table;

import static java.util.function.Predicate.not;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

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

    columnModel_ = new TableColumnModel();
    headerRenderer_ = new HeaderRenderer<>(tableModel, this);
    pressedColumn_ = null;
    columnViewMap_ = IntStream.range(0, view.getColumnCount())
      .mapToObj(i -> createColumn(view, i))
      .collect(Collectors.toMap(Pair::getRight, Pair::getLeft));
    viewIdColumnMap_ = columnViewMap_.entrySet().stream()
      .collect(Collectors.toMap(entry -> entry.getValue().getId(), Entry::getKey));
  }

  void applyColumnWidthPrefs(LinkedHashMap<String, Integer> idWidths) {
    final List<String> validColIds = idWidths.keySet().stream()
      .filter(viewIdColumnMap_::containsKey).toList();

    if (!validColIds.isEmpty()) {
      viewIdColumnMap_.keySet().stream()
        .filter(not(validColIds::contains))
        .map(viewIdColumnMap_::get)
        .forEach(columnModel_::removeColumn);
      validColIds.stream()
      .forEach(colId -> setColWidth(viewIdColumnMap_.get(colId), idWidths.get(colId)));
    }
  }

  private void setColWidth(TableColumn column, int width) {
    column.setPreferredWidth(width);
    column.setWidth(width);
  }

  private Pair<TablePropertyView<E, ?>, TableColumn> createColumn(TableView<E> view, int index) {
    final TablePropertyView<E, ?> columnView = view.getColumnView(index);
    final TableColumn column = new TableColumn(index);

    columnModel_.addColumn(column);
    setColWidth(column, headerRenderer_.getTableCellRendererComponent(
      null, columnView.getName(), false, false, -1, index
    ).getPreferredSize().width * 10);
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
      if ((evt.getButton() == MouseEvent.BUTTON1)) {
        final TableColumn column = columnModel_.getColumn(tableHeader_.columnAtPoint(evt.getPoint()));

        if (columnViewMap_.get(column).isSortable()) {
          setPressedColumn(column, tableHeader_);
        }
      }
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
      if ((evt.getButton() == MouseEvent.BUTTON1)) {
        setPressedColumn(null, tableHeader_);
      }
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
      if ((evt.getButton() == MouseEvent.BUTTON1)) {
        sortByColumn(columnModel_.getColumn(tableHeader_.columnAtPoint(evt.getPoint())));
      }
    }

  }

  /** making protected tableColumns property accessible ... */
  private static class TableColumnModel extends DefaultTableColumnModel {

    boolean containsColumn(TableColumn column) {
      return tableColumns.contains(column);
    }

  }

}
