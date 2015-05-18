package com.bensler.decaf.swing.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.bensler.decaf.swing.view.PropertyView;
import com.bensler.decaf.util.NamedImpl;
import com.bensler.decaf.util.tree.Hierarchical;
import com.bensler.decaf.util.tree.Hierarchy;

/**
 */
public class TreeModel <H extends Hierarchical<?>> extends DefaultTreeModel {

  /** default-filter, which accepts all nodes.
   */
  public    final static class AcceptAllFilter<H extends Hierarchical<?>> implements TreeFilter<H> {
    @Override
    public boolean accept(H node) {
      return true;
    };
  };

  /** Provides a method called when a TreeModel stops or starts using a synthetic
   * root. A EntityTree should listen on these events to switch its tree components
   * root visible flag. */
  public interface RootChangeListener extends EventListener {

    /** Called when a TreeModel stops or starts using a synthetic root. */
    public void rootChanged(TreeModel<?> source);

  }

  public static final class Root extends NamedImpl implements Hierarchical<Object> {

      public Root() {
          super("SynthRoot");
      }

      @Override
      public Object getParent() {
          return null;
      }

  }

  public static final Hierarchical<?> invisibleRoot = new Root();

  protected final         Map<Object, List<H>>    parentChildArrayMap_;

  private                 PropertyView<? super H, ?> view_;

  protected               Hierarchy<H>            data_;

  private                 TreeFilter<H>           filter_;

  TreeModel(PropertyView<? super H, ?> view) {
    super(null, false);
    parentChildArrayMap_ = new HashMap<Object, List<H>>();
    data_ = new Hierarchy<H>();
    setFilter(null);
    view_ = view;
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
      final List<H>         list           = new ArrayList<>();

      Collections.sort(filter(sourceChildren, list), view_);
      parentChildArrayMap_.put(parent, list);
    }
    return parentChildArrayMap_.get(parent);
  }

  protected List<H> filter(Collection<H> source, List<H> target) {
    for (H child : source) {
      if (filter_.accept(child)) {
        target.add(child);
      }
    }
    return target;
  }

  /** @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
   */
  @Override
  public int getChildCount(Object parent) {
    return data_.getChildCount((parent == invisibleRoot) ? null : (Hierarchical<?>)parent);
  }

  /** @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
   */
  @Override
  public boolean isLeaf(Object node) {
    return (getChildCount(node) < 1);
  }

  /** @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
   */
  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {}

  /** @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
   */
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

  public void setFilter(TreeFilter<H> newFilter) {
    filter_ = ((newFilter == null) ? new AcceptAllFilter<H>() : newFilter);
    fireFilterChanged();
  }

  public TreeFilter<H> getFilter() {
    return filter_;
  }

  /** Should be called, whenever the filter changes
   */
  private void fireFilterChanged() {
    if (!data_.isEmpty()) {
      // fire event only if tree is not empty
      fireStructureChanged(getRoot());
    }
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
