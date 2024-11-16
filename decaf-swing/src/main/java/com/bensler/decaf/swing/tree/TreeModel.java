package com.bensler.decaf.swing.tree;

import java.util.Comparator;
import java.util.EventListener;
import java.util.List;
import java.util.Set;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.bensler.decaf.util.tree.AbstractHierarchy;
import com.bensler.decaf.util.tree.ChildrenCollectionMaintainer.SortedListMaintainer;
import com.bensler.decaf.util.tree.Hierarchical;
import com.bensler.decaf.util.tree.Hierarchy;

/**
 */
public class TreeModel <H extends Hierarchical<H>> extends DefaultTreeModel {

  /** Provides a method called when a TreeModel stops or starts using a synthetic
   * root. A EntityTree should listen on these events to switch its tree components
   * root visible flag. */
  public interface RootChangeListener extends EventListener {

    /** Called when a TreeModel stops or starts using a synthetic root. */
    public void rootChanged(TreeModel<?> source);

  }

  public static final class Root extends Object implements Hierarchical<Object> {
      @Override
      public Object getParent() {
          return null;
      }

      @Override
      public String toString() {
        return "<SynthRoot>";
      }
  }

  static class ListHierarchy<M extends Hierarchical<M>> extends AbstractHierarchy<M, List<M>> {

    public ListHierarchy(Comparator<? super M> comparator) {
      super(new SortedListMaintainer<>(comparator));
    }

    /** Widening visibility */
    @Override
    protected List<M> getChildrenNoCopy(Hierarchical<?> member) {
      return super.getChildrenNoCopy(member);
    }

  }

  protected final         ListHierarchy<H>            data_;

  private Hierarchical<?> invisibleRoot_ = new Root();

  TreeModel(Comparator<? super H> comparator) {
    super(null, false);
    data_ = new ListHierarchy<H>(comparator);
    invisibleRoot_ = new Root();
  }

  @Override
  public Hierarchical<?> getRoot() {
    final H dataRoot = data_.getRoot();

    return ((dataRoot == null) ? invisibleRoot_ : dataRoot);
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

  void setData(Hierarchy<H> data) {
//    final boolean hadSynthRoot  = data_.hasSyntheticRoot();

    invisibleRoot_ = new Root();
    data_.clear();
    data_.addAll(data.getMembers());
    fireTreeStructureChanged(this, null,  null, null);
//    fireRootChanged();
//    fireRootMayHaveChanged(hadSynthRoot);
    fireTreeStructureChanged(this, new Object[] {getRoot()},  null, null);
  }

  public boolean showRoot() {
    return (!data_.hasNullRoot());
  }

  public void addNode(H node) {
    final H         newParent     = data_.resolve(node.getParent());
    final boolean   synthRoot     = data_.hasNullRoot();

    if (data_.contains(node)) {
      int oldIndex  = getChildren(newParent).indexOf(node);

      data_.remove(node, false);
      fireTreeNodesRemoved(
        this, getPathAsObjectArray(newParent),
        new int[] {oldIndex}, null
      );
    }
    data_.add(node);
    if (node == data_.getRoot()) {
      fireStructureChanged(node);
    } else {
      fireTreeNodesInserted(
        this, getPathAsObjectArray(newParent),
        new int[] {getChildren(newParent).indexOf(node)}, null
      );
    }
    fireRootMayHaveChanged(synthRoot);
  }

  private void fireRootMayHaveChanged(boolean hadSynthRoot) {
    if (hadSynthRoot ^ data_.hasNullRoot()) {
      for (RootChangeListener listener : listenerList.getListeners(RootChangeListener.class)) {
        listener.rootChanged(this);
      }
    }
  }

  /** Updates a node in this TreeModel. If the TreeModel does not already contain an equal node it is
   * simply added. */
  public void updateNode(H node) {
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

  public void removeNode(H node) {
		final H   hierarchical  = data_.resolve(node);
		final H   parent        = data_.resolve(hierarchical.getParent());
    final int index;

		if (data_.contains(node)) {
      index = getChildren(parent).indexOf(node);
      data_.remove(node, true);
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

  TreePath getTreePath(Hierarchical<?> node) {
    final List<H> path = getPath(data_.resolve(node));

    if (hasSyntheticRoot()) {
      path.add(0, (H)invisibleRoot_);
    }
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

    data_.remove(subject, true);
    if (parent != null) {
      fireStructureChanged(parent);
    }
  }

  /** Removes a node. If the removed node has children they became siblings of the root
   * node. */
  void remove(H subject) {
    final H         parent      = data_.resolve(subject.getParent());
    final boolean   hadChildren = !data_.isLeaf(subject);

    data_.remove(subject, false);
    if (parent != null) {
      fireStructureChanged(parent);
    }
    if (hadChildren) {
      fireRootChanged();
    }
  }

  boolean contains(H entity) {
    return data_.contains(entity);
  }

  public void clear() {
    final boolean hadSynthRoot  = data_.hasNullRoot();

    data_.clear();
    fireRootChanged();
    fireRootMayHaveChanged(hadSynthRoot);
  }

  public boolean hasSyntheticRoot() {
    return data_.hasNullRoot();
  }

  public void removeRootChangeListener(RootChangeListener listener) {
    listenerList.remove(RootChangeListener.class, listener);
  }

  public void addRootChangeListener(RootChangeListener listener) {
    listenerList.add(RootChangeListener.class, listener);
  }

  public H resolve(Object hierarchical) {
    return data_.resolve(hierarchical);
  }

  public Set<H> getMembers() {
    return data_.getMembers();
  }

}
