package com.bensler.decaf.swing.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.bensler.decaf.swing.Viewable;
import com.bensler.decaf.swing.view.PropertyView;
import com.bensler.decaf.util.tree.Hierarchical;
import com.bensler.decaf.util.tree.Hierarchy;
import com.bensler.flob.biz.Entity;

/**
 */
public class TreeModel extends DefaultTreeModel {

  /** default-filter, which accepts all nodes.
   */
  public    final static TreeFilter ACCEPT_ALL = new TreeFilter() {
    public boolean accept(Viewable node) { return true; };
  };
  
  /** Provides a method called when a TreeModel stops or starts using a synthetic 
   * root. A EntityTree should listen on these events to switch its tree components 
   * root visible flag. */
  public interface RootChangeListener extends EventListener {
    
    /** Called when a TreeModel stops or starts using a synthetic root. */
    public void rootChanged(TreeModel source);
    
  }
  
  protected final         Map<Hierarchical, List<Hierarchical>> parentChildArrayMap_;

  private                 PropertyView                          view_;
  
  protected               Hierarchy                             data_;

  private                 TreeFilter                            filter_;
  
  private                 boolean                               catchNewEntities_;
 
  TreeModel(PropertyView view, boolean catchNewEntities) {
    super(null, false);
    parentChildArrayMap_ = new HashMap<Hierarchical, List<Hierarchical>>();
    data_ = new Hierarchy();
    filter_ = ACCEPT_ALL;
    view_ = view;
    catchNewEntities_ = catchNewEntities;
  }
  
  /** @see javax.swing.tree.TreeModel#getRoot()
   */
  public Hierarchical getRoot() {
    return data_.getRoot();
  }
  
  /** @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
   */
  public Object getChild(Object parent, int index) {
    return getChildren((Hierarchical)parent).get(index);
  }

  @SuppressWarnings("unchecked")
  protected List<Hierarchical> getChildren(Hierarchical parent) {
    if (!parentChildArrayMap_.containsKey(parent)) {
      if ((parent == null) || (!data_.contains(parent))) {
        return Collections.emptyList();
      }
      final Set<? extends Hierarchical> sourceChildren = getChildrenFromSource(parent);
      final List<Hierarchical>          list           = new ArrayList<Hierarchical>();
      
      if (sourceChildren == null) {
        return Collections.emptyList();
      }
      filter(sourceChildren, list);
      if (!list.isEmpty()) {
        Collections.sort((List)list, view_);
      }
      parentChildArrayMap_.put(
        parent, list
      );
    }
    return parentChildArrayMap_.get(parent);
  }
  
  protected Set<? extends Hierarchical> getChildrenFromSource(Hierarchical parent) {
    return data_.getChildren(parent);
  }
  

  protected void filter(Set<? extends Hierarchical> source, List<Hierarchical> target) {
    for (Hierarchical child : source) {
      if (filter_.accept((Viewable)child)) {
        target.add(child);
      }
    }
  }
  
  /** @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
   */
  public int getChildCount(Object parent) {
    return getChildren((Hierarchical)parent).size();
  }

  /** @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
   */
  public boolean isLeaf(Object node) {
    return (getChildCount(node) < 1);
  }

  /** @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
   */
  public void valueForPathChanged(TreePath path, Object newValue) {}


  /** @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
   */
  public int getIndexOfChild(Object parent, Object child) {
    return getChildren((Hierarchical)parent).indexOf(child);
  }

  void setData(Hierarchy data) {
    final boolean hadSynthRoot  = data_.hasSyntheticRoot();
    
    data_ = new Hierarchy(data);
    fireStructureChanged(getRoot());
    fireRootMayHaveChanged(hadSynthRoot);
  }
  
  public boolean showRoot() {
    return (!data_.hasSyntheticRoot());
  }

  public void addNode(Hierarchical node) {
    final Hierarchical  newParent     = data_.resolve(node.getParent());
    final boolean       synthRoot     = data_.hasSyntheticRoot();
    
    if (data_.contains(node)) {
      int oldIndex  = getChildren(newParent).indexOf(node);
      
      data_.remove(node, false);
      fireTreeNodesRemoved(
        this, getPath(newParent), 
        new int[] {oldIndex}, null
      );
    }
    data_.add(node);
    parentChildArrayMap_.remove(newParent);
    if (node == data_.getRoot()) {
      fireStructureChanged(node);
    } else {
      fireTreeNodesInserted(
        this, getPath(newParent), 
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
  public void updateNode(Hierarchical node) {
    if (!contains(node)) {
      addNode(node);
    } else {
      // node exists in this model -> update
      final Hierarchical   oldNode       = resolve((Viewable)node);
      final Hierarchical   oldParent     = data_.resolve(oldNode.getParent());
            int            oldIndex      = -1;
      final Hierarchical   parent        = data_.resolve(node.getParent());
            int            newIndex      = -1;
      
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
        final Hierarchical[] parentPath    = getPath(parent);

        if ((oldIndex == newIndex) && (oldParent == parent) && (newIndex >= 0)) {
          fireTreeNodesChanged(this, parentPath, new int[] {newIndex}, null);
        } else {
          if (oldIndex >= 0) {
            fireTreeNodesRemoved(this, getPath(oldParent), new int[] {oldIndex}, null);
          }
          fireTreeNodesInserted(this, parentPath, new int[] {newIndex}, null);
        }
      }
    }
  }
 
  public void removeNode(Hierarchical node) {
		final Hierarchical  hierarchical  = data_.resolve(node);
		final Hierarchical  parent        = data_.resolve(hierarchical.getParent());
    final int           index;
    
		if (data_.contains(node)) {
      index = getChildren(parent).indexOf(node);
      data_.remove(node, true);
      parentChildArrayMap_.remove(parent);
      fireTreeNodesRemoved(
        this, getPath(parent), 
        new int[] {index}, null
      );
    }
  }
  
  public void fireNodeChanged(Hierarchical element) {
    fireTreeNodesChanged(getPath(element));
  }
  
  public void fireTreeNodesChanged(Object[] path) {
    fireTreeNodesChanged(this, path, null, null);
  }
  
  public List<Hierarchical> getPath(Hierarchical node, boolean removeSyntheticRoot) {
    final List<Hierarchical> path = getPath_(node);
    
    if (removeSyntheticRoot) {
      path.remove(data_.getSyntheticRoot());
    }
    return path;
  }
  
  private List<Hierarchical> getPath_(Hierarchical node) {
    final List<Hierarchical> list = data_.getPath(node);

    if (list.isEmpty()) {
      list.add(0, data_.getSyntheticRoot());
    }
    return list;
  }
  
  public Hierarchical[] getPath(Hierarchical node) {
    final List<Hierarchical> path = getPath_(node);
    
    return path.toArray(new Hierarchical[path.size()]);
  }
  
  public TreePath getTreePath(Hierarchical node) {
    return new TreePath(getPath(data_.resolve(node)));
  }
  
  void fireStructureChanged(Hierarchical node) {
    parentChildArrayMap_.clear();
    fireTreeStructureChanged(this, getPath(node), null, null);
  }

  void removeTree(Hierarchical subject) {
    final Hierarchical    parent = data_.resolve(subject.getParent());
    
    data_.remove(subject, true);
    if (parent != null) {
      fireStructureChanged(parent);
    }
  }

  /** Removes a node. If the removed node has children they became siblings of the root
   * node. */
  void remove(Hierarchical subject) {
    final Hierarchical    parent      = data_.resolve(subject.getParent());
    final boolean         hadChildren = !data_.getChildren(subject).isEmpty();
    
    data_.remove(subject, false);
    if (parent != null) {
      fireStructureChanged(parent);
    }
    if (hadChildren) {
      fireStructureChanged(getRoot());
    }
  }

  boolean contains(Viewable entity) {
    return data_.contains(entity);
  }

  public void setFilter(TreeFilter newFilter) {
    filter_ = ((newFilter == null) ? ACCEPT_ALL : newFilter);
    fireFilterChanged();
  }
  
  public TreeFilter getFilter() {
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

  public Set< ? extends Hierarchical> getLeafNodes() {
    return data_.getLeafNodes();
  }

  public void clear() {
    setData(new Hierarchy());
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

  public Hierarchical resolve(Viewable hierarchical) {
    return data_.resolve(hierarchical);
  }

  public Hierarchy getData() {
    return new Hierarchy(data_);
  }

  public Set<? extends Hierarchical> getMembers() {
    return data_.getMembers();
  }

  void entityChanged(Viewable entity) {
    final Hierarchical hierarchical = (Hierarchical)entity;
    
    if (contains(hierarchical)) {
      updateNode(hierarchical);
    }
  }

  void entityCreated(Viewable entity) {
    final Hierarchical hierarchical = (Hierarchical)entity;

    if (catchNewEntities_ && contains(hierarchical.getParent())) {
      addNode(hierarchical);
    }
  }

  void entityRemoved(Viewable entity) {
    final Hierarchical hierarchical = (Hierarchical)entity;
    
    if (contains(hierarchical.getParent())) {
      remove(hierarchical);
    }
  }

  void setCatchNewEntities(boolean catchNewEntities) {
    catchNewEntities_ = catchNewEntities;
  }
  
  boolean isCatchingNewEntities() {
    return catchNewEntities_;
  }
 
}
