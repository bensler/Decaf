package com.bensler.decaf.swing.view;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**  */
public class NoSelectionModel extends Object {

  public    static  ListSelectionModel  createTableListModel() { return new TableList(); }
  
  public    static  TreeSelectionModel  createTreeModel() { return new Tree(); }
  
  private final static class TableList extends DefaultListSelectionModel {
  
    private TableList() {
      clearSelection();
    }
    
    public void addSelectionInterval(int index0, int index1) {}
    
    public void insertIndexInterval(int index, int length, boolean before)  {}
    
    public void setSelectionInterval(int index0, int index1) {}
  
    public void setAnchorSelectionIndex(int index) {}
    
    public void setLeadSelectionIndex(int index) {}
  
  }

  private final static class Tree extends DefaultTreeSelectionModel {

    public void setSelectionPaths(TreePath[] pPaths) {}
    
    public void addSelectionPaths(TreePath[] paths) {}

    public void removeSelectionPaths(TreePath[] paths) {}

  }
  
}
