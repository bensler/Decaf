package com.bensler.decaf.swing.tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class NewTreeModel implements TreeModel {

  private final List<TreeModelListener> listeners_;

  public NewTreeModel() {
    listeners_ = new ArrayList<>();
  }

  @Override
  public Object getRoot() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object getChild(Object parent, int index) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getChildCount(Object parent) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isLeaf(Object node) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {
    // TODO Auto-generated method stub

  }

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void addTreeModelListener(TreeModelListener listnernerToAdd) {
    if (!listeners_.contains(listnernerToAdd)) {
      listeners_.add(listnernerToAdd);
    }
  }

  @Override
  public void removeTreeModelListener(TreeModelListener listenerToRemove) {
    listeners_.remove(listenerToRemove);
  }

}
