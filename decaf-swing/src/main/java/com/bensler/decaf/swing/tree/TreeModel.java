package com.bensler.decaf.swing.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.bensler.decaf.util.tree.Hierarchical;
import com.bensler.decaf.util.tree.Hierarchy;

/**
 */
public class TreeModel <H extends Hierarchical<?>> extends DefaultTreeModel {

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
  }

  public static final Hierarchical<?> invisibleRoot = new Root();

  protected final         Map<Object, List<H>>    parentChildArrayMap_;

  private                 Comparator<? super H>   comparator_;

  protected               Hierarchy<H>            data_;

  TreeModel(Comparator<? super H> comparator) {
    super(null, false);
    parentChildArrayMap_ = new HashMap<Object, List<H>>();
    data_ = new Hierarchy<H>();
    comparator_ = comparator;
  }

  @Override
  public Hierarchical<?> getRoot() {
    final H dataRoot = data_.getRoot();

    return ((dataRoot == null) ? invisibleRoot : dataRoot);
  }

  @Override
  public Object getChild(Object parent, int index) {
    return getChildren((Hierarchical<?>)parent).get(index);
  }

  protected List<H> getChildren(Hierarchical<?> parent) {
    if (!parentChildArrayMap_.containsKey(parent)) {
      final Collection<H>   sourceChildren = data_.getChildren((parent == invisibleRoot) ? null : parent);
      final List<H>         list           = new ArrayList<>(sourceChildren);

      Collections.sort(list, comparator_);
      parentChildArrayMap_.put(parent, list);
    }
    return parentChildArrayMap_.get(parent);
  }

  @Override
  public int getChildCount(Object parent) {
    return data_.getChildCount((parent == invisibleRoot) ? null : (Hierarchical<?>)parent);
  }

  @Override
  public boolean isLeaf(Object node) {
    return (getChildCount(node) < 1);
  }

  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {}

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    return getChildren((Hierarchical<?>)parent).indexOf(child);
  }

  void setData(Hierarchy<H> data) {
    final boolean hadSynthRoot  = data_.hasSyntheticRoot();

    data_ = new Hierarchy<H>(data.getMembers());
    fireStructureChanged(getRoot());
    fireRootMayHaveChanged(hadSynthRoot);
  }

  public boolean showRoot() {
    return (!data_.hasSyntheticRoot());
  }

  public void addNode(H node) {
    final H         newParent     = data_.resolve(node.getParent());
    final boolean   synthRoot     = data_.hasSyntheticRoot();

    if (data_.contains(node)) {
      int oldIndex  = getChildren(newParent).indexOf(node);

      data_.remove(node, false);
      fireTreeNodesRemoved(
        this, getPathAsObjectArray(newParent),
        new int[] {oldIndex}, null
      );
    }
    data_.add(node);
    parentChildArrayMap_.remove(newParent);
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
    if (hadSynthRoot ^ data_.hasSyntheticRoot()) {
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
      parentChildArrayMap_.remove(parent);
      if (parent != null) {
        // node will be root
        newIndex = getIndexOfChild(parent, node);
      }
      if ((oldIndex < 0) && (newIndex < 0)) {
        // oldNode was and node is root
        fireNodeChanged(node);
      } else {
        if ((oldIndex < 0) ^ (newIndex < 0)) {
          fireStructureChanged(getRoot());
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

  public void removeNode(Hierarchical<?> node) {
		final H   hierarchical  = data_.resolve(node);
		final H   parent        = data_.resolve(hierarchical.getParent());
    final int index;

		if (data_.contains(node)) {
      index = getChildren(parent).indexOf(node);
      data_.remove(node, true);
      parentChildArrayMap_.remove(parent);
      fireTreeNodesRemoved(
        this, getPathAsObjectArray(parent),
        new int[] {index}, null
      );
    }
  }

  public void fireNodeChanged(Hierarchical<?> element) {
    fireTreeNodesChanged(getPathAsObjectArray(element));
  }

  public void fireTreeNodesChanged(Object[] path) {
    fireTreeNodesChanged(this, path, null, null);
  }

  public List<H> getPath(Hierarchical<?> node) {
    return data_.getPath(node);
  }

  public Object[] getPathAsObjectArray(Hierarchical<?> node) {
    final List<H> path = data_.getPath(node);

    return (path.isEmpty() ? new Object[] {invisibleRoot} : path.toArray(new Object[path.size()]));
  }

  public TreePath getTreePath(Hierarchical<?> node) {
    return new TreePath(getPath(data_.resolve(node)));
  }

  void fireStructureChanged(Hierarchical<?> node) {
    parentChildArrayMap_.clear();
    fireTreeStructureChanged(this, getPathAsObjectArray(node), null, null);
  }

  void removeTree(Hierarchical<?> subject) {
    final H parent = data_.resolve(subject.getParent());

    data_.remove(subject, true);
    if (parent != null) {
      fireStructureChanged(parent);
    }
  }

  /** Removes a node. If the removed node has children they became siblings of the root
   * node. */
  void remove(Hierarchical<?> subject) {
    final H         parent      = data_.resolve(subject.getParent());
    final boolean   hadChildren = !data_.isLeaf(subject);

    data_.remove(subject, false);
    if (parent != null) {
      fireStructureChanged(parent);
    }
    if (hadChildren) {
      fireStructureChanged(getRoot());
    }
  }

  boolean contains(Hierarchical<?> entity) {
    return data_.contains(entity);
  }

  public void clear() {
    setData(new Hierarchy<H>());
  }

  public boolean hasSyntheticRoot() {
    return data_.hasSyntheticRoot();
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

  public Hierarchy<H> getData() {
    return new Hierarchy<H>(data_);
  }

  public Set<H> getMembers() {
    return data_.getMembers();
  }

}
