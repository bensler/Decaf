package com.bensler.decaf.swing.tree;

import java.util.Comparator;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.bensler.decaf.util.tree.Hierarchical;
import com.bensler.decaf.util.tree.Hierarchy;

/**
 */
public class TreeModel <H extends Hierarchical<H>> extends DefaultTreeModel implements EntityTreeModel<H> {

  protected final         ListHierarchy<H>   data_;

  private   final         SynthRoot<H>       invisibleRoot_;

  TreeModel(Comparator<? super H> comparator) {
    super(null, false);
    data_ = new ListHierarchy<H>(comparator);
    invisibleRoot_ = new SynthRoot<>();
  }

  @Override
  public Hierarchical<H> getRoot() {
    return invisibleRoot_;
  }

  @Override
  public Object getChild(Object parent, int index) {
    return getChildren((Hierarchical<?>)parent).get(index);
  }

  protected List<H> getChildren(Hierarchical<?> parent) {
    return data_.getChildrenNoCopy((parent == invisibleRoot_) ? null : parent);
  }

  @Override
  public int getChildCount(Object parent) {
    return data_.getChildCount((parent == invisibleRoot_) ? null : (Hierarchical<?>)parent);
  }

  @Override
  public boolean isLeaf(Object node) {
    return (getChildCount(node) < 1);
  }

  /** Does not work with custom nodes */
  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {}

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    return getChildren((Hierarchical<?>)parent).indexOf(child);
  }

  @Override
  public void setData(Hierarchy<H> data) {
//    final boolean hadSynthRoot  = data_.hasSyntheticRoot();

    data_.clear();
    data_.addAll(data.getMembers());
    fireTreeStructureChanged(this, null,  null, null);
//    fireRootChanged();
//    fireRootMayHaveChanged(hadSynthRoot);
    fireTreeStructureChanged(this, new Object[] {getRoot()},  null, null);
  }

  @Override
  public void addNode(H node) {
    final H         newParent     = data_.resolve(node.getParent());

    if (data_.contains(node)) {
      int oldIndex  = getChildren(newParent).indexOf(node);

      data_.removeNode(node);
      fireTreeNodesRemoved(
        this, getPathAsObjectArray(newParent),
        new int[] {oldIndex}, null
      );
    }
    data_.add(node);
//    if (node == data_.getRoot()) {
//      fireStructureChanged(node);
//    } else {
      fireTreeNodesInserted(
        this, getPathAsObjectArray(newParent),
        new int[] {getChildren(newParent).indexOf(node)}, null
      );
//    }
  }

  /** Updates a node in this TreeModel. If the TreeModel does not already contain an equal node it is
   * simply added. */
  void updateNode(H node) {
    if (!contains(node)) {
      addNode(node);
    } else {
      // node exists in this model -> update
      final H       oldNode       = resolve(node);
      final H       oldParent     = data_.resolve(oldNode.getParent());
            int     oldIndex      = -1;
      final H       parent        = data_.resolve(node.getParent());
            int     newIndex      = -1;

      if (oldParent != null) {
        // oldNode is root
        oldIndex = getIndexOfChild(oldParent, node);
      }
      data_.add(node);
      if (parent != null) {
        // node will be root
        newIndex = getIndexOfChild(parent, node);
      }
      if ((oldIndex < 0) && (newIndex < 0)) {
        // oldNode was and node is root
        fireNodeChanged(node);
      } else {
        if ((oldIndex < 0) ^ (newIndex < 0)) {
          fireRootChanged();
        }
        final Object[] parentPath    = getPathAsObjectArray(parent);

        if ((oldIndex == newIndex) && (oldParent == parent) && (newIndex >= 0)) {
          fireTreeNodesChanged(this, parentPath, new int[] {newIndex}, null);
        } else {
          if (oldIndex >= 0) {
            fireTreeNodesRemoved(this, getPathAsObjectArray(oldParent), new int[] {oldIndex}, null);
          }
          fireTreeNodesInserted(this, parentPath, new int[] {newIndex}, null);
        }
      }
    }
  }

  void removeNode(H node) {
		final H   hierarchical  = data_.resolve(node);
		final H   parent        = data_.resolve(hierarchical.getParent());
    final int index;

		if (data_.contains(node)) {
      index = getChildren(parent).indexOf(node);
      data_.removeTree(node);
      fireTreeNodesRemoved(
        this, getPathAsObjectArray(parent),
        new int[] {index}, null
      );
    }
  }

  @SuppressWarnings("unchecked")
  public H getLastPathComponent(TreePath path) {
    return (H)path.getLastPathComponent();
  }

  public void fireNodeChanged(H element) {
    fireTreeNodesChanged(getPathAsObjectArray(element));
  }

  public void fireTreeNodesChanged(Object[] path) {
    fireTreeNodesChanged(this, path, null, null);
  }

  public List<H> getPath(H node) {
    return data_.getPath(node);
  }

  Object[] getPathAsObjectArray(H node) {
    final List<H> path = data_.getPath(node);

    return (path.isEmpty() ? new Object[] {invisibleRoot_} : path.toArray(new Object[path.size()]));
  }

  @Override
  public TreePath getTreePath(H node) {
    final List<H> path = data_.getPath(data_.resolve(node));

    path.add(0, (H)invisibleRoot_);
    return new TreePath(path.toArray());
  }

  private void fireRootChanged() {
    fireTreeStructureChanged(this, new Object[] {invisibleRoot_}, null, null);
  }

  private void fireStructureChanged(H node) {
    fireTreeStructureChanged(this, getPathAsObjectArray(node), null, null);
  }

  void removeTree(H subject) {
    final H parent = data_.resolve(subject.getParent());

    data_.removeTree(subject);
    if (parent != null) {
      fireStructureChanged(parent);
    }
  }

  /** Removes a node. If the removed node has children they became siblings of the root
   * node. */
  void remove(H subject) {
    final H         parent      = data_.resolve(subject.getParent());
    final boolean   hadChildren = !data_.isLeaf(subject);

    data_.removeNode(subject);
    if (parent != null) {
      fireStructureChanged(parent);
    }
    if (hadChildren) {
      fireRootChanged();
    }
  }

  @Override
  public boolean contains(H entity) {
    return data_.contains(entity);
  }

  public void clear() {
    data_.clear();
    fireRootChanged();
  }

  public H resolve(Object hierarchical) {
    return data_.resolve(hierarchical);
  }

  @Override
  public Hierarchy<H> getData() {
    return new Hierarchy<>(data_.getMembers());
  }

}
