package com.bensler.decaf.swing.table;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;


/**
 */
public class ColumnModel<E> extends DefaultTableColumnModel {

  private final Map<TablePropertyView<E, ?>, Column<E>> allPropertiesColumnMap_;

  private final Map<TablePropertyView<E, ?>, Column<E>> propertyColumnMap_;

  private Column<E> pressedColumn_;

  private int[] prefSizes_;

  ColumnModel(TableView<E> view) {
    allPropertiesColumnMap_ = new HashMap<>();
    propertyColumnMap_ = new HashMap<>();
    pressedColumn_ = null;
    prefSizes_ = null;
    for (int i = 0; i < view.getColumnCount(); i++) {
      addColumn(new Column<>(view.getColumnView(i), i));
    }
    allPropertiesColumnMap_.putAll(propertyColumnMap_);
  }

  Map<String, Column<E>> getColumnsById() {
    return propertyColumnMap_.entrySet().stream()
        .collect(Collectors.toMap(entry -> entry.getKey().getId(), Entry::getValue));
  }

  String getSizes() {
    return IntStream.range(0, getColumnCount())
      .mapToObj(this::getColumn)
      .map(Column.class::cast)
      .map(column -> column.getId() + ":" + column.getWidth())
      .collect(Collectors.joining(","));
  }

  void setPrefSizes(int[] sizes, int sum) {
    prefSizes_ = sizes;
    final int   prefWidth = getPrefWidth();

    if ((sum > 0) && (prefWidth > 0)) {
      final float ratio = ((float)sum) / prefWidth;

      if (prefSizes_.length == getColumnCount()) {
        for (int i = 0; i < prefSizes_.length; i++) {
          getColumn(i).setPreferredWidth(Math.round(prefSizes_[i] * ratio));
        }
      }
    }

  }

  int getPrefWidth() {
    int   prefSizeSum = 0;

    for (int i = 0; i < prefSizes_.length; i++) {
      prefSizeSum += prefSizes_[i];
    }
    return prefSizeSum;
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

  boolean setPressedColumn(Column column) {
    final boolean change = (column != pressedColumn_) && ((column == null) || (tableColumns.contains(column)));

    if (change) {
      pressedColumn_ = column;
    }
    return change;
  }

  boolean isColumnPressed(int col) {
    return (pressedColumn_ == tableColumns.get(col));
  }

  @Override
  public Column<E> getColumn(int columnIndex) {
    return (Column<E>)super.getColumn(columnIndex);
  }

//  void setPrefSize(int size, int columnIndex) {
//    prefSizes_[columnIndex] = size;
//  }
//
//  int[] getPrefSizes() {
//    if (prefSizes_ == null) {
//      updatePrefSizes();
//    }
//    return (int[])ArrayUtil.createCopy(prefSizes_);
//  }

//  void setShownProperties(Collection<TablePropertyView> newProperties) {
//    final Set<TablePropertyView<E, ?>>   shownProperties   = getShownProperties();
//    final Set<TablePropertyView>   toRemove          = new HashSet<>(shownProperties);
//    final Set<TablePropertyView>   toAdd             = new HashSet<>(newProperties);
//
//    toRemove.removeAll(newProperties);
//    toAdd.removeAll(shownProperties);
//    for (TablePropertyView view : toRemove) {
//      hideColumn(view);
//    }
//    updatePrefSizes();
//    if (!toAdd.isEmpty()) {
//            int   i             = 0;
//            int   prefSizeSum   = 0;
//      final int[] newPrefSizes  = new int[prefSizes_.length + toAdd.size()];
//      final int   avgSize;
//
//      for (i = 0; i < prefSizes_.length; i++) {
//        prefSizeSum += prefSizes_[i];
//        newPrefSizes[i] = prefSizes_[i];
//      }
//      avgSize = Math.round(((float)prefSizeSum) / newPrefSizes.length);
//      for (TablePropertyView view : toAdd) {
//        showColumn(view);
//        newPrefSizes[i] = avgSize;
//      }
//      prefSizes_ = newPrefSizes;
//    }
//  }
//
//  private void showColumn(TablePropertyView<?, ?> view) {
//    final Column<E> column = allPropertiesColumnMap_.get(view);
//
//    if (column != null) {
////      final int viewIndex = getColumnCount();
////
////      column.setViewIndex(viewIndex);
//      addColumn(column);
//    }
//  }
//
//  private void hideColumn(TablePropertyView<?, ?> view) {
//    final Column<E> column = allPropertiesColumnMap_.get(view);
//
//    if (column != null) {
////      column.setViewIndex(tableColumns.indexOf(column));
//      removeColumn(column);
//    }
//  }

}
