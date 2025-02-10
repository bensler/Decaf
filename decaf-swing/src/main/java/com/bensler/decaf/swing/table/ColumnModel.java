package com.bensler.decaf.swing.table;

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
public class ColumnModel<E> extends DefaultTableColumnModel {

  private final Map<TablePropertyView<E, ?>, Column<E>> allPropertiesColumnMap_;

  private final Map<TablePropertyView<E, ?>, Column<E>> propertyColumnMap_;

  private Column<E> pressedColumn_;

  private int[] prefSizes_;

  ColumnModel() {
    allPropertiesColumnMap_ = new HashMap<>();
    propertyColumnMap_ = new HashMap<>();
    pressedColumn_ = null;
    prefSizes_ = null;
  }

  void init() {
    allPropertiesColumnMap_.putAll(propertyColumnMap_);
  }

  Set<TablePropertyView<E, ?>> getShownProperties() {
    return Set.copyOf(propertyColumnMap_.keySet());
  }

  List<String> getSizeList() {
    final List<String> returnValue    = new ArrayList<>(tableColumns.size());

    for (int i = 0; i < tableColumns.size(); i++) {
      returnValue.add(Integer.toString(((Column<?>)tableColumns.get(i)).getWidth()));
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
            getColumn(i).setPreferredWidth(Math.round(prefSizes_[i] * ratio));
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
      setShownProperties(new HashSet<>(0));
      for (int i = 0; i < properties.size(); i++) {
        addColumn(allPropertiesColumnMap_.get(properties.get(i)));
      }
    }
  }

  void setShownProperties(Collection<TablePropertyView> newProperties) {
    final Set<TablePropertyView<E, ?>>   shownProperties   = getShownProperties();
    final Set<TablePropertyView>   toRemove          = new HashSet<>(shownProperties);
    final Set<TablePropertyView>   toAdd             = new HashSet<>(newProperties);

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

  @Override
  public void addColumn(TableColumn aColumn) {
    final Column              column    = (Column)aColumn;
    final TablePropertyView   property  = column.getView();

    if (!propertyColumnMap_.containsKey(property)) {
      propertyColumnMap_.put(property, column);
      super.addColumn(column);
    }
  }

  @Override
  public void removeColumn(TableColumn column) {
    propertyColumnMap_.remove(((Column)column).getView());
    if (column == pressedColumn_) {
      pressedColumn_ = null;
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

}
