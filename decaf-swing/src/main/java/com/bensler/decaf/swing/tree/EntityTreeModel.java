package com.bensler.decaf.swing.tree;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.bensler.decaf.util.tree.Hierarchical;
import com.bensler.decaf.util.tree.Hierarchy;

public interface EntityTreeModel<H extends Hierarchical<H>> extends TreeModel {

  void setData(Hierarchy<H> data);

  boolean contains(H node);

  @Override
  public Hierarchical<H> getRoot();

  Hierarchy<H> getData();

  TreePath getTreePath(H node);

  void addNode(H node);

}
