package com.bensler.decaf.swing.table;

/**
 */
public class ColumnModel { // extends DefaultTableColumnModel {

//  ColumnModel() {
//  }
//
//  @Override
//  public void addColumn(TableColumn aColumn) {
//    final Column              column    = (Column)aColumn;
//    final TablePropertyView   property  = column.getView();
//
//    if (!propertyColumnMap_.containsKey(property)) {
//      propertyColumnMap_.put(property, column);
//      super.addColumn(column);
//    }
//  }
//
//  @Override
//  public Column<E> getColumn(int columnIndex) {
//    return (Column<E>)super.getColumn(columnIndex);
//  }

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
