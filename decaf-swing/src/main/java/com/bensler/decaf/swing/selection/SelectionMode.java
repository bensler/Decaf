package com.bensler.decaf.swing.selection;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.TreeSelectionModel;

/** Just unifying constants from {@link ListSelectionModel} (used in {@link JList} and {@link JTable}) and
 * {@link TreeSelectionModel}. */
public enum SelectionMode {

  NONE(-1, -1),
  SINGLE(ListSelectionModel.SINGLE_SELECTION, TreeSelectionModel.SINGLE_TREE_SELECTION),
  SINGLE_INTERVAL(ListSelectionModel.SINGLE_INTERVAL_SELECTION, TreeSelectionModel.CONTIGUOUS_TREE_SELECTION),
  MULTIPLE_INTERVAL(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

  private final int listTableConstant_;
  private final int treeConstant_;

  private SelectionMode(int listTableConstant, int treeConstant) {
    listTableConstant_ = listTableConstant;
    treeConstant_ = treeConstant;
  }

  /**
   * gets the constant to be used in a {@link JTable}
   */
  public int getTableConstant() {
  	return getListConstant();
  }

  /**
   * gets the constant to be used in a {@link JList}
   */
  public int getListConstant() {
    return listTableConstant_;
  }

  /**
   * gets the constant to be used in a {@link JTree}
   */
  public int getTreeConstant() {
    return treeConstant_;
  }

}