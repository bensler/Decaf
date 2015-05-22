package com.bensler.decaf.swing.table;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;


/**
 */
public class ColumnModel extends DefaultTableColumnModel {

  private   final         Map<TablePropertyView, Column>  allPropertiesColumnMap_;

  private   final         Map<TablePropertyView, Column>  propertyColumnMap_;

  private                 Column                          pressedColumn_;

  private                 Column                          sortedColumn_;

  private                 Sorting                         sorting_;

  private                 int[]                           prefSizes_;

  ColumnModel() {
    super();
    allPropertiesColumnMap_ = new HashMap<TablePropertyView, Column>();
    propertyColumnMap_ = new HashMap<TablePropertyView, Column>();
    pressedColumn_ = null;
    sortedColumn_ = null;
    sorting_ = Sorting.NONE;
    prefSizes_ = null;
  }

  void init() {
    allPropertiesColumnMap_.putAll(propertyColumnMap_);
  }

  Set<TablePropertyView> getShownProperties() {
    return new HashSet<TablePropertyView>(propertyColumnMap_.keySet());
  }

  List<String> getPropertyKeyList() {
    final List<String> returnValue    = new ArrayList<String>(tableColumns.size());

    for (int i = 0; i < tableColumns.size(); i++) {
      final TablePropertyView view = ((Column)tableColumns.get(i)).getView();

      returnValue.add(view.getId());
    }
    return returnValue;
  }

  List<String> getSizeList() {
    final List<String> returnValue    = new ArrayList<String>(tableColumns.size());

    for (int i = 0; i < tableColumns.size(); i++) {
      returnValue.add(Integer.toString(((Column)tableColumns.get(i)).getWidth()));
    }
    return returnValue;
  }

  void setPrefSizes(int[] sizes) {
    prefSizes_ = sizes;
  }

  void setPrefSize(int size, int columnIndex) {
    prefSizes_[columnIndex] = size;
  }

  int getPrefWidth() {
    int   prefSizeSum = 0;

    for (int i = 0; i < prefSizes_.length; i++) {
      prefSizeSum += prefSizes_[i];
    }
    return prefSizeSum;
  }

  void updatePrefSizes() {
    prefSizes_ = new int[getColumnCount()];
    for (int i = 0; i < prefSizes_.length; i++) {
      prefSizes_[i] = getColumn(i).getWidth();
    }
  }

  void updateColPrefSizes(int size) {
    if (prefSizes_ == null) {
      updatePrefSizes();
    } else {
      final int   prefWidth = getPrefWidth();

      if ((size > 0) && (prefWidth > 0)) {
        final float ratio = ((float)size) / prefWidth;

        if (prefSizes_.length == getColumnCount()) {
          for (int i = 0; i < prefSizes_.length; i++) {
            final Column col = getColumn(i);

            col.setPreferredWidth(Math.round(prefSizes_[i] * ratio));
          }
        }
      }
    }
  }

//  int[] getPrefSizes() {
//    if (prefSizes_ == null) {
//      updatePrefSizes();
//    }
//    return (int[])ArrayUtil.createCopy(prefSizes_);
//  }
//
//  Column getSortedColumn() {
//    return sortedColumn_;
//  }

  void setProperties(List<TablePropertyView> properties) {
    if (!properties.isEmpty()) {
      setShownProperties(new HashSet<TablePropertyView>(0));
      for (int i = 0; i < properties.size(); i++) {
        addColumn(allPropertiesColumnMap_.get(properties.get(i)));
      }
    }
  }

  void setSorting(Column column, Sorting sorting) {
    for (int i = 0; i < getColumnCount(); i++) {
      if (getColumn(i) == column) {
        sortedColumn_ = column;
        sorting_ = sorting;
        break;
      }
    }
  }

  void setShownProperties(Collection<TablePropertyView> newProperties) {
    final Set<TablePropertyView>   shownProperties   = getShownProperties();
    final Set<TablePropertyView>   toRemove          = new HashSet<TablePropertyView>(shownProperties);
    final Set<TablePropertyView>   toAdd             = new HashSet<TablePropertyView>(newProperties);

    toRemove.removeAll(newProperties);
    toAdd.removeAll(shownProperties);
    for (TablePropertyView view : toRemove) {
      hideColumn(view);
    }
    updatePrefSizes();
    if (!toAdd.isEmpty()) {
            int   i             = 0;
            int   prefSizeSum   = 0;
      final int[] newPrefSizes  = new int[prefSizes_.length + toAdd.size()];
      final int   avgSize;

      for (i = 0; i < prefSizes_.length; i++) {
        prefSizeSum += prefSizes_[i];
        newPrefSizes[i] = prefSizes_[i];
      }
      avgSize = Math.round(((float)prefSizeSum) / newPrefSizes.length);
      for (TablePropertyView view : toAdd) {
        showColumn(view);
        newPrefSizes[i] = avgSize;
      }
      prefSizes_ = newPrefSizes;
    }
  }

  private void showColumn(TablePropertyView view) {
    final Column column = allPropertiesColumnMap_.get(view);

    if (column != null) {
      final int viewIndex = getColumnCount();

      column.setViewIndex(viewIndex);
      addColumn(column);
    }
  }

  void hideColumn(TablePropertyView view) {
    final Column column = allPropertiesColumnMap_.get(view);

    if (column != null) {
      column.setViewIndex(tableColumns.indexOf(column));
      removeColumn(column);
    }
  }

  /** @see javax.swing.table.TableColumnModel#addColumn(javax.swing.table.TableColumn)
   */
  @Override
  public void addColumn(TableColumn aColumn) {
    final Column              column    = (Column)aColumn;
    final TablePropertyView   property  = column.getView();

    if (!propertyColumnMap_.containsKey(property)) {
      propertyColumnMap_.put(property, column);
      super.addColumn(column);
    }
  }

  /** @see javax.swing.table.TableColumnModel#removeColumn(javax.swing.table.TableColumn)
   */
  @Override
  public void removeColumn(TableColumn column) {
    propertyColumnMap_.remove(((Column)column).getView());
    if (column == pressedColumn_) {
      pressedColumn_ = null;
    }
    if (column == sortedColumn_) {
      sortedColumn_ = null;
    }
    super.removeColumn(column);
  }

  void setPressedColumn(Column column) {
    if (tableColumns.contains(column)) {
      pressedColumn_ = column;
    }
  }

  void resetPressedColumn() {
    pressedColumn_ = null;
  }

  boolean isColumnPressed(int col) {
    return (pressedColumn_ == tableColumns.get(col));
  }

  @Override
  public Column getColumn(int columnIndex) {
    return (Column)super.getColumn(columnIndex);
  }

  Sorting getSorting(int columnIndex) {
    return (
      (tableColumns.get(columnIndex) == sortedColumn_)
      ? sorting_ : Sorting.NONE
    );
  }

  public Column resolveColumn(Ref tablePropertyViewRef) {
    for (TableColumn column : tableColumns) {
      if (((Column)column).getView().equals(tablePropertyViewRef)) {
        return (Column)column;
      }
    }
    return null;
  }

}
