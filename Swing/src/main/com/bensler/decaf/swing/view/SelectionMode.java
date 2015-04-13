package com.bensler.decaf.swing.view;

import javax.swing.ListSelectionModel;
import javax.swing.tree.TreeSelectionModel;

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
   * gets the constant to be used in a JTable
   */
  public int getTableConstant() {
  	return getListConstant();
  }

  /**
   * gets the constant to be used in a JList
   */
  public int getListConstant() {
    return listTableConstant_;
  }

  /**
   * gets the constant to be used in a JTree
   */
  public int getTreeConstant() {
    return treeConstant_;
  }

  }