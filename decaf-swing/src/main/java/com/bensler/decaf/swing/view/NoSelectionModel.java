package com.bensler.decaf.swing.view;

import javax.swing.DefaultListSelectionModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

/**  */
public class NoSelectionModel extends Object {

  public static final DefaultListSelectionModel NOP_MODEL_TABLE_LIST = new TableList();

  public static final DefaultTreeSelectionModel NOP_MODEL_TREE = new Tree();

  private final static class TableList extends DefaultListSelectionModel {

    private TableList() {
      clearSelection();
    }

    @Override
    public void addSelectionInterval(int index0, int index1) {}

    @Override
    public void insertIndexInterval(int index, int length, boolean before)  {}

    @Override
    public void setSelectionInterval(int index0, int index1) {}

    @Override
    public void setAnchorSelectionIndex(int index) {}

    @Override
    public void setLeadSelectionIndex(int index) {}

  }

  private final static class Tree extends DefaultTreeSelectionModel {

    @Override
    public void setSelectionPaths(TreePath[] pPaths) {}

    @Override
    public void addSelectionPaths(TreePath[] paths) {}

    @Override
    public void removeSelectionPaths(TreePath[] paths) {}

  }

}
