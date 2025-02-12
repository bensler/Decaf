package com.bensler.decaf.swing.table;

import static com.bensler.decaf.util.function.ForEachMapperAdapter.forEachMapper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableCellRenderer;

import com.bensler.decaf.swing.awt.ColorHelper;
import com.bensler.decaf.util.Pair;


/**
 */
public class TableComponent<E> extends JTable {

  private   final         Color               backgroundSelectionColorUnfocused_;

  private   final         TableModel<E>       sortableTableModel_;

  private   final         ColumnModel<E>      columnModel_;

  private   final         TableView<E>        view_;

  final TableSelectionController<E> selectionCtrl_;

  /** Causes a gap when right and left aligned columns are beside each other */
  private   final         Border              gapBorder_;

  /** for calc the preferred viewports size when residing in
   * a scrollpane.<p>
   * [min,max], ([min]>=1)<=<=[max], default: [10,10] */
  private   final         int[]               visibleRows_;

  private   final         HeaderRenderer<E>   headerRenderer_;

  private                 TableRowView<E>     rowView_;

  TableComponent(EntityTable<E> entityTable, TableModel<E> model, TableView<E> view) {
    super(model, new ColumnModel<>(view));
    selectionCtrl_ = new TableSelectionController<>(entityTable, this);
    backgroundSelectionColorUnfocused_ = ColorHelper.mix(getSelectionBackground(), 2, UIManager.getColor("Table.background"), 1);
    rowView_ = new TableRowView.Nop<>();
    visibleRows_ = new int[]{10, 10};
    view_ = view;
    sortableTableModel_ = model;
    columnModel_ = (ColumnModel)columnModel;
    headerRenderer_ = new HeaderRenderer<>(sortableTableModel_, (ColumnModel)getColumnModel());
    tableHeader.setDefaultRenderer(headerRenderer_);
    gapBorder_ = BorderFactory.createEmptyBorder(0, 3, 0, 3);
    tableHeader.addMouseListener(new HeaderListener());
    tableHeader.addMouseMotionListener(new HeaderDragListener());
  }

  void setSizesFromHeaderLabel() {
    final int[] sizes = new int[view_.getColumnCount()];
          int   sum   = 0;

    for (int i = 0; i < columnModel_.getColumnCount(); i++) {
      final String name = columnModel_.getColumn(i).getView().getName();

      sizes[i] = headerRenderer_.getTableCellRendererComponent(
        this, name, false, false, -1, i
      ).getPreferredSize().width * 10;
      sum += sizes[i];
    }
    columnModel_.setPrefSizes(sizes);
    columnModel_.updateColPrefSizes(sum);
  }

  private void sortByColumn(Column<E> column) {
    try (var s = selectionCtrl_.new SelectionKeeper()) {
      if (column.isSortable()) {
        sortableTableModel_.sortByColumn(column, sortableTableModel_.getNewSorting(column));
      }
    }
  }

  List<E> getSelectedValues() {
    return sortableTableModel_.getValues(getSelectedRows());
  }

  List<E> setSelectedValues(Collection<E> newSelection) {
    clearSelection();
    return newSelection.stream()
      .map(entity -> new Pair<>(entity, sortableTableModel_.indexOf(entity)))
      .filter(pair -> pair.getRight() >= 0)
      .map(forEachMapper(pair -> addRowSelectionInterval(pair.getRight(), pair.getRight())))
      .flatMap(pair ->  sortableTableModel_.contains(pair.getLeft()).stream())
      .toList();
  }

  private class HeaderDragListener extends MouseMotionAdapter {

    @Override
    public void mouseDragged(MouseEvent e) {
      if (columnModel_.setPressedColumn(null)) {
        tableHeader.repaint();
      }
    }

  }

  private class HeaderListener extends MouseAdapter {

    @Override
    public void mousePressed(MouseEvent evt) {
    	if (isEditing()) {
        getCellEditor().stopCellEditing();
      }
      if (
        (evt.getButton() == MouseEvent.BUTTON1)
        && (getResizeColumnIndex(evt.getPoint()) < 0)
      ) {
        final Column<E> column = columnModel_.getColumn(tableHeader.columnAtPoint(evt.getPoint()));

        if (column.isSortable() && columnModel_.setPressedColumn(column)) {
          tableHeader.repaint();
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
        tableHeader.repaint();
      }
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
      final Point point             = evt.getPoint();
      final int   resizeColumnIndex = getResizeColumnIndex(point);

      if ((resizeColumnIndex < 0) && (evt.getButton() == MouseEvent.BUTTON1)) {
        sortByColumn(columnModel_.getColumn(tableHeader.columnAtPoint(evt.getPoint())));
      }
    }

    private int getResizeColumnIndex(Point point) {
      int columnIndex = tableHeader.columnAtPoint(point);

      if (columnIndex >= 0) {
        final Rectangle   rect = tableHeader.getHeaderRect(columnIndex);

        rect.grow(-3, 0);
        if (rect.contains(point)) {
          columnIndex = -1;
        } else {
          final int center = rect.x + (rect.width / 2);

          columnIndex += (
            (tableHeader.getComponentOrientation().isLeftToRight() ^ (point.x < center)) ? 0 : -1
          );
        }
      }
      return columnIndex;
    }

  }

  private int getPrefColumnWidth(int columnIndex) {
    final Column<E>         column    = columnModel_.getColumn(columnIndex);
    final TablePropertyView view      = column.getView();
          int               maxWidth  = headerRenderer_.getTableCellRendererComponent(
      this, view.getName(), false, false, -1, columnIndex
    ).getPreferredSize().width;

    for (int row = 0; row < getRowCount(); row++) {
      maxWidth = Math.max(maxWidth, prepareRenderer(view, row, columnIndex).getPreferredSize().width);
    }
//    columnModel_.setPrefSize(maxWidth, columnIndex);
//    columnModel_.updateColPrefSizes(columnModel_.getPrefWidth());
//    invalidateIfNeeded();
    return maxWidth;
  }

  boolean isSelected(Point point) {
    final int row         = rowAtPoint(point);

    return (
      (row > -1)
      && isRowSelected(row)
    );
  }

  @Override
  public void addMouseMotionListener(MouseMotionListener l) {
    if (!(l instanceof BasicTableUI.MouseInputHandler)) {
      super.addMouseMotionListener(l);
    }
  }
  /** Selects the row at the given position */
  void modifySelection(Point point) {
    final int row         = rowAtPoint(point);

    if ((row > -1) && (!isRowSelected(row))) {
      setRowSelectionInterval(row, row);
    }
  }

  @Override
  public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
    final boolean    selected     = isCellSelected(row, column);
    final E          viewable     = getViewable(row);
    final Object     cellValue    = getValueAt(row, column);
          JComponent component    = (JComponent)((TablePropertyView)renderer).getCellRendererComponent(
      this, viewable, cellValue, row, convertColumnIndexToModel(column), selected, isFocusOwner()
    );

    component = rowView_.prepareRenderer(this, component, viewable, row, selected);
    component.setBorder(gapBorder_);
    return component;
  }

  @Override
  public Dimension getPreferredScrollableViewportSize() {
    final Dimension   prefSize  = getMinimumSize();
          int         rowCount  = getRowCount();

    rowCount = Math.max(visibleRows_[0], rowCount);
    rowCount = Math.min(visibleRows_[1], rowCount);
    prefSize.height = rowCount * getRowHeight();
    return prefSize;
  }

  @Override
  public Dimension getMinimumSize() {
    return new Dimension(
      columnModel_.getColumnCount() * 10 , super.getMinimumSize().height
    );
  }

  @Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
    if (getParent() != null) {
      // set it for the viewport (for validation framework)
      getParent().setBackground(bg);
    }
	}

  @Override
  public void setToolTipText(String text) {
    super.setToolTipText(text);
    if (getParent() != null) {
      // set it for the viewport (for validation framework)
      ((JComponent)getParent()).setToolTipText(text);
    }
  }

  @Override
  public TableCellRenderer getCellRenderer(int row, int column) {
    return view_.getColumnView(convertColumnIndexToModel(column));
  }

  String getColumnWidthPrefs() {
    return columnModel_.getSizes();
  }

  String getSortPrefs() {
    return sortableTableModel_.getSortPrefs();
  }

  void applySortPrefs(String sortings) {
    sortableTableModel_.applySortPrefs(sortings, columnModel_.getColumnsById());
  }

  public E getViewable(int row) {
    return sortableTableModel_.getValueAt(row);
  }

  void invalidateIfNeeded() {
    if (isVisible()) {
      invalidate();
    }
    repaint();
  }

  @Override
  public String getToolTipText(MouseEvent event) {
    String  tt      = super.getToolTipText(event);

    if (tt == null) {
      final Point location  = event.getPoint();
      final int   row       = rowAtPoint(location);

      if ((row >= 0) && isCellSelected(row, 0)) {
//        tt = Client.getRes().getMessage(
//          GeneralKeys.TABLE_SEL_ROWS_TT, Integer.toString(getSelectedRowCount())
//        );
      }
    }
    return tt;
  }

//  public void setSizesFromData() {
//    final int[] sizes = new int[columnModel_.getColumnCount()];
//          int   sum   = 0;
//
//    for (int i = 0; i < sizes.length; i++) {
//      sizes[i] = getPrefColumnWidth(i) + 2;
//      sum += sizes[i];
//    }
//    columnModel_.setPrefSizes(sizes);
//    columnModel_.updateColPrefSizes(sum);
//  }

  public Color getBackgroundSelectionColorUnfocused() {
    return backgroundSelectionColorUnfocused_;
  }

  void repaintSelection() {
    final int[] selectedRows = getSelectedRows();

    if (selectedRows.length > 0) {
      final Rectangle rightestCell = getCellRect(0, getColumnCount() - 1, false);
      final int       rightEdge    = rightestCell.x + rightestCell.width;

      for (int row : selectedRows) {
        final Rectangle dirtyRect = getCellRect(row, 0, false);

        dirtyRect.width = rightEdge;
        repaint(dirtyRect);
      }
    }
  }

}
