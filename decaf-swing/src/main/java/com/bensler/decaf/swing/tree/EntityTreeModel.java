package com.bensler.decaf.swing.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.bensler.decaf.util.tree.Hierarchical;
import com.bensler.decaf.util.tree.Hierarchy;

public class EntityTreeModel<H extends Hierarchical<H>> implements TreeModel {

  private final List<TreeModelListener> modelListeners_;

  private final ListHierarchy<H> data_;
  private final SynthRoot<H> invisibleRoot_;

  public EntityTreeModel(Comparator<? super H> comparator) {
    modelListeners_ = new ArrayList<>();
    data_ = new ListHierarchy<H>(comparator);
    invisibleRoot_ = new SynthRoot<>();
  }

  @Override
  public Hierarchical<H> getRoot() {
    return invisibleRoot_;
  }

  @Override
  public Object getChild(Object parent, int index) {
    return data_.getChildren(data_.resolve(parent)).get(index);
  }

  @Override
  public int getChildCount(Object parent) {
    return data_.getChildren(data_.resolve(parent)).size();
  }

  @Override
  public boolean isLeaf(Object node) {
    return data_.getChildren(data_.resolve(node)).isEmpty();
  }

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    return data_.getChildren(data_.resolve(parent)).indexOf(child);
  }

  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {
    // (TODO) there is currently no in-place-editing of nodes ...
  }

  public boolean contains(H entity) {
    return data_.contains(entity);
  }

  public void setData(Hierarchy<H> data) {
    data_.clear();
    data_.addAll(data.getMembers());
    fireStructureChanged(new TreeModelEvent(this, new TreePath(getRoot()),  null, null));
  }

  protected List<H> getChildren(Hierarchical<?> parent) {
    return data_.getChildrenNoCopy((parent == invisibleRoot_) ? null : parent);
  }

  public void addNode(H node) {
    final H newParent = data_.resolve(node.getParent());

    if (data_.contains(node)) {
      int oldIndex  = getChildren(newParent).indexOf(node);

      data_.removeNode(node);
      fireNodeRemoved(new TreeModelEvent(
        this, getPathAsObjectArray(newParent),
        new int[] {oldIndex}, null
      ));
    }
    data_.add(node);
    fireNodeInserted(new TreeModelEvent(
      this, getPathAsObjectArray(newParent),
      new int[] {getChildren(newParent).indexOf(node)}, null
    ));
  }

  Object[] getPathAsObjectArray(H node) {
    final List<Object> path = new ArrayList<>(data_.getPath(node));

    path.add(0, invisibleRoot_);
    return path.toArray(new Object[path.size()]);
  }

  public Hierarchy<H> getData() {
    return new Hierarchy<>(data_.getMembers());
  }

  public TreePath getTreePath(H node) {
    final List<H> path = data_.getPath(data_.resolve(node));

    path.add(0, (H)invisibleRoot_);
    return new TreePath(path.toArray());
  }

  public void fireNodeChanged(H node) {
    TreeModelEvent evt = new TreeModelEvent(this, getPathAsObjectArray(node));

    fireTreeModelEvent(listener -> listener.treeNodesChanged(evt));
  }

  private void fireNodeInserted(TreeModelEvent evt) {
    fireTreeModelEvent(listener -> listener.treeNodesInserted(evt));
  }

  private void fireNodeRemoved(TreeModelEvent evt) {
    fireTreeModelEvent(listener -> listener.treeNodesRemoved(evt));
  }

  private void fireStructureChanged(TreeModelEvent evt) {
    fireTreeModelEvent(listener -> listener.treeStructureChanged(evt));
  }

  private void fireStructureChanged(H node) {
    fireStructureChanged(new TreeModelEvent(this, getPathAsObjectArray(node), null, null));
  }

  @Override
  public void addTreeModelListener(TreeModelListener listenerToAdd) {
    if (!modelListeners_.contains(listenerToAdd)) {
      modelListeners_.add(listenerToAdd);
    }
  }

  private void fireTreeModelEvent(Consumer<TreeModelListener> x) {
    modelListeners_.forEach(x);
  }

  @Override
  public void removeTreeModelListener(TreeModelListener listenerToRemove) {
    modelListeners_.remove(listenerToRemove);
  }

}
