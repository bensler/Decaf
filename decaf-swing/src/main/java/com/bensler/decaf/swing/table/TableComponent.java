package com.bensler.decaf.swing.table;

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
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.bensler.decaf.swing.awt.ColorHelper;


/**
 */
public class TableComponent<E> extends JTable {

  private   final         Color               backgroundSelectionColorUnfocused_;

  private   final         EntityTable         entityTable_;

  private   final         TableModel<E>       sortableTableModel_;

  private   final         ColumnModel         columnModel_;

  private   final         TableView           view_;

  /** Causes a gap when right and left aligned columns are beside each other */
  private   final         Border              gapBorder_;

  /** for calc the preferred viewports size when residing in
   * a scrollpane.<p>
   * [min,max], ([min]>=1)<=<=[max], default: [10,10] */
  private   final         int[]               visibleRows_;

  private   final         HeaderRenderer      headerRenderer_;

  private                 TableRowView        rowView_;

  private                 ColumnResizeState   columnResizeState_;

  TableComponent(
    EntityTable boTable, TableModel model, TableView view
  ) {
    super(model);

    backgroundSelectionColorUnfocused_ = ColorHelper.mix(getSelectionBackground(), 2, UIManager.getColor("Table.background"), 1);
    entityTable_ = boTable;
    columnResizeState_ = ColumnResizeState.NONE;
    rowView_ = new TableRowView.Nop();
    visibleRows_ = new int[]{10, 10};
    view_ = view;
    sortableTableModel_ = model;
    columnModel_ = (ColumnModel)columnModel;
    createDefaultColumnsFromModel();
    headerRenderer_ = new HeaderRenderer((ColumnModel)getColumnModel());
    tableHeader.setDefaultRenderer(headerRenderer_);
    gapBorder_ = BorderFactory.createEmptyBorder(0, 3, 0, 3);
    tableHeader.addMouseListener(new HeaderListener());
    tableHeader.addMouseMotionListener(new HeaderDragListener());
  }

  @Override
  public void doLayout() {
    try {
      if (columnResizeState_ == ColumnResizeState.SIZE) {
        columnModel_.updateColPrefSizes(getSize().width);
      }
      super.doLayout();
      if (columnResizeState_ == ColumnResizeState.COL) {
        columnModel_.updatePrefSizes();
        entityTable_.saveColumnState();
      }
    } finally {
      columnResizeState_ = ColumnResizeState.NONE;
    }
  }

  void setSizesFromHeaderLabel() {
    final int[] sizes = new int[view_.getColumnCount()];
          int   sum   = 0;

    for (int i = 0; i < columnModel_.getColumnCount(); i++) {
      final TablePropertyView view = columnModel_.getColumn(i).getView();

      sizes[i] = headerRenderer_.getTableCellRendererComponent(
        this, view.getName(), false, false, -1, i
      ).getPreferredSize().width;
      sum += sizes[i];
    }
    columnModel_.setPrefSizes(sizes);
    columnModel_.updateColPrefSizes(sum);
  }

  @Override
  public void createDefaultColumnsFromModel() {
    if (columnModel.getColumnCount() < 1) {
      for (int i = 0; i < sortableTableModel_.getColumnCount(); i++) {
        addColumn(new Column(view_.getColumnView(i), i));
      }
    }
  }

  /** @see javax.swing.JTable#getAutoCreateColumnsFromModel()
   */
  @Override
  public boolean getAutoCreateColumnsFromModel() {
    return false;
  }

  /** @see javax.swing.JTable#createDefaultColumnModel()
   */
  @Override
  protected TableColumnModel createDefaultColumnModel() {
    return new ColumnModel();
  }

  private void sortByColumn(Column column) {
    sortByColumn(column, sortableTableModel_.getSorting(column));
  }

  void sortByColumn(int column, boolean ascending) {
    sortByColumn(columnModel_.getColumn(column), (ascending ? Sorting.ASCENDING : Sorting.DESCENDING));
  }

  private void sortByColumn(Column column, Sorting sorting) {
    if (column.isSortable()) {
      sortableTableModel_.sortByColumn(column, sorting);
      columnModel_.setSorting(column, sorting);
      entityTable_.saveSortState();
      tableHeader.repaint();
    }
  }

  List<E> getSelectedValues() {
    return sortableTableModel_.getValues(getSelectedRows());
  }

  void setSelectedValues(Collection selection) {
    clearSelection();
    for (Iterator iter = selection.iterator(); iter.hasNext();) {
      final int index = sortableTableModel_.indexOf(iter.next());

      if (index >= 0) {
        addRowSelectionInterval(index, index);
      }
    }
  }

  private class HeaderDragListener extends MouseMotionAdapter {

    @Override
    public void mouseDragged(MouseEvent e) {
      TableColumn resizingColumn  = tableHeader.getResizingColumn();

      if (resizingColumn != null) {
        columnResizeState_ = ColumnResizeState.COL;
      }
    }

  }

  void setResizingState() {
    columnResizeState_ = ColumnResizeState.SIZE;
  }

  @Override
  public void setSize(Dimension d) {
    if (getSize().width != d.width) {
      setResizingState();
    }
    super.setSize(d);
  }

  private class HeaderListener extends MouseAdapter {

    @Override
    public void mousePressed(MouseEvent evt) {
    	if (isEditing()) {
        getCellEditor().stopCellEditing();
      }
      if (
        (evt.getButton() == MouseEvent.BUTTON1)
        && (entityTable_.isSortable())
        && (getResizeColumnIndex(evt.getPoint()) < 0)
      ) {
        final Column column = columnModel_.getColumn(tableHeader.columnAtPoint(evt.getPoint()));

        if (column.isSortable()) {
          columnModel_.setPressedColumn(column);
          tableHeader.repaint();
        }
      }
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
      if (
        (evt.getButton() == MouseEvent.BUTTON1)
        && (getResizeColumnIndex(evt.getPoint()) < 0)
      ) {
      	columnModel_.resetPressedColumn();
        tableHeader.repaint();
      }
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
      final Point point             = evt.getPoint();
      final int   resizeColumnIndex = getResizeColumnIndex(point);

      if (resizeColumnIndex < 0) {
        if (evt.getButton() == MouseEvent.BUTTON1) {
          sortByColumn(columnModel_.getColumn(tableHeader.columnAtPoint(evt.getPoint())));
        }
      } else {
        if (evt.getClickCount() == 2) {
          if (resizeColumnIndex >= 0) {
            // TODO
//            resizeToFit(resizeColumnIndex);
          }
        }
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
    final Column            column    = columnModel_.getColumn(columnIndex);
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
      this, viewable, cellValue, row, convertColumnIndexToModel(column), selected
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
      columnModel.getColumnCount() * 10 , super.getMinimumSize().height
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

  List<String> getSortPrefs() {
    return sortableTableModel_.getSortPrefs();
  }

//  void loadSortPrefs(String[] sortings) {
//    sortableTableModel_.loadSortPrefs(sortings, columnModel_);
//    headerRenderer_.repaint();
//  }

  public E getViewable(int row) {
    return sortableTableModel_.getValueAt(row);
  }

  void setVisibleRows(int min, int max) {
    //min >= 1
    visibleRows_[0] = Math.max(1, min);
    // max>=min
    visibleRows_[1] = Math.max(visibleRows_[0], max);
    invalidateIfNeeded();
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

  private enum ColumnResizeState {

    NONE("none"),
    COL("col"),
    SIZE("size");

    private final String key_;

    private ColumnResizeState(String key) {
      key_ = key;
    }

  }

  @Override
  public void columnMoved(TableColumnModelEvent e) {
    super.columnMoved(e);
    if (e.getFromIndex() != e.getToIndex()) {
      entityTable_.saveColumnState();
    }
  }

  public void setSizesFromData() {
    final int[] sizes = new int[columnModel_.getColumnCount()];
          int   sum   = 0;

    for (int i = 0; i < sizes.length; i++) {
      sizes[i] = getPrefColumnWidth(i) + 2;
      sum += sizes[i];
    }
    columnModel_.setPrefSizes(sizes);
    columnModel_.updateColPrefSizes(sum);
  }

  public void setRowView(TableRowView rowView) {
    rowView_ = rowView;
  }

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
