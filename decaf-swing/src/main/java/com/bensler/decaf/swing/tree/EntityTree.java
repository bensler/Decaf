package com.bensler.decaf.swing.tree;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.bensler.decaf.swing.view.EntityComponent;
import com.bensler.decaf.swing.view.EntitySelectionListener;
import com.bensler.decaf.swing.view.NoSelectionModel;
import com.bensler.decaf.swing.view.PropertyView;
import com.bensler.decaf.swing.view.SelectionMode;
import com.bensler.decaf.util.tree.Hierarchical;
import com.bensler.decaf.util.tree.Hierarchy;

/**
 * This is a tree that displays a Hierarchy.
 */
public class EntityTree<H extends Hierarchical<H>> extends Object implements EntityComponent<H>,
TreeSelectionListener, FocusListener {

  private   final         Set<FocusListener>  focusListeners_;

  private   final         JScrollPane         scrollPane_;

  private   final         List<H>             selection_;

  protected final         TreeComponent<H>    tree_;

  private                 EntitySelectionListener<? super H> selectionListener_;

  private                 boolean             silentSelectionChange_;

  protected               TreeModel<H>        model_;

  private                 SelectionMode       selectionMode_;

  private                 TreeSelectionModel  defSelModel_;

  protected               boolean             editable_;

  public EntityTree(PropertyView<H, ?> view) {
    focusListeners_ = new HashSet<>();
    model_ = new TreeModel<>(view);
    tree_ = createCompoent(model_ = new TreeModel<>(view), view);
    // update the selection BEFORE any listener is notified!
    tree_.addTreeSelectionListener(this);
    tree_.setShowsRootHandles(true);
    tree_.addFocusListener(this);

    editable_ = true;
    scrollPane_ = new JScrollPane(tree_);
    selection_ = new ArrayList<>(1);
    silentSelectionChange_ = false;
    setSelectionMode(SelectionMode.SINGLE);
    setSelectionListener(null);
  }

  protected TreeComponent<H> createCompoent(TreeModel<H> model, PropertyView<H, ?> view) {
    return new TreeComponent<>(model, view);
  }

  public void setEditable(boolean editable) {
    if (editable_ ^ editable) {
      editable_ = editable;
      if (editable) {
        setSelectionMode(SelectionMode.MULTIPLE_INTERVAL);
        setBackground(UIManager.getColor("TextField.background"));
      } else {
        setSelectionMode(SelectionMode.NONE);
        setBackground(null);
      }
    }
  }

  public boolean isEditable() {
    return editable_;
  }

  @Override
  public JScrollPane getScrollPane() {
    return scrollPane_;
  }

  void setModel(TreeModel<H> newModel) {
    model_ = newModel;
    tree_.setModel(newModel);
    tree_.setRootVisible(newModel.showRoot());
  }

  public void setShowsRootHandles(boolean visible) {
    tree_.setShowsRootHandles(visible);
  }

  @Override
  public void setBackground(Color color) {
  	// set it for the tree
  	tree_.setBackground(color);
  	// set it for the viewport
  	scrollPane_.getViewport().setBackground(color);
  }

  /** @param widthFactor width = rowhight * visibleRowCount * widthFactor */
  public void setVisibleRowCount(int rowCount, float widthFactor) {
    tree_.setVisibleRowCount(rowCount, widthFactor);
  }

  public void expandCollapseAll(boolean expand) {
    int row = 0;

    while (row < tree_.getRowCount()) {
      if (expand) {
        tree_.expandRow(row++);
      } else {
        tree_.collapseRow(row++);
      }
    }
  }

  public SelectionMode getSelectionMode() {
    return selectionMode_;
  }

  public void setSelectionMode(SelectionMode mode) {
    if (selectionMode_ != mode) {
      if (selectionMode_ == SelectionMode.NONE) {
        tree_.setSelectionModel(defSelModel_);
      }
      if (mode == SelectionMode.NONE) {
        defSelModel_ = tree_.getSelectionModel();
        tree_.setSelectionModel(NoSelectionModel.createTreeModel());
      } else {
        tree_.getSelectionModel().setSelectionMode(mode.getTreeConstant());
      }
      selectionMode_ = mode;
    }
  }

  public void setCellRenderer(TreeCellRenderer renderer) {
    tree_.setCellRenderer(renderer);
  }

  @Override
  public void clearSelection() {
    tree_.clearSelection();
  }

  public void showAll(Collection<H> bosToShow) {
    for (H item : bosToShow) {
      final Object[]  path  = model_.getPathAsObjectArray(item);

      if (path.length > 1) {
        tree_.expandPath(new TreePath(path).getParentPath());
      }
    }
    tree_.clearSelection();
  }

  /** @return the JTable component wrapped by a JScrollpane
   * @see com.bensler.flob.gui.EntityComponent#getComponent()
   */
  @Override
  public TreeComponent<H> getComponent() {
    return tree_;
  }

  @Override
  public void valueChanged(TreeSelectionEvent evt) {
    updateSelection();
    fireSelectionChanged();
  }

  public void refireSelectionChanged() {
    final List<H> oldSelection = new ArrayList<>(selection_);

    select(Collections.emptyList());
    select(oldSelection);
  }

  protected void fireSelectionChanged() {
    if (!silentSelectionChange_) {
      final List<H> selection = Collections.unmodifiableList(selection_);
      selectionListener_.selectionChanged(this, selection);
    }
  }

  /** updates selection_ silently (no events are fired) */
  @SuppressWarnings("unchecked")
  private void updateSelection() {
    final TreePath[]  paths     = tree_.getSelectionPaths();

    selection_.clear();
    if (paths != null) {
      for (int i = 0; i < paths.length; i++) {
        selection_.add((H)paths[i].getLastPathComponent());
      }
    }
    fireSelectionChanged();
  }

  @Override
  public List<H> getSelection() {
    return new ArrayList<>(selection_);
  }

  public void addData(H hierarchical) {
    model_.addNode(hierarchical);
  }

  public void updateData(H hierarchical) {
    model_.updateNode(hierarchical);
  }

  public void removeData(H ref) {
    model_.removeNode(ref);
  }

  public void setData(Hierarchy<H> hierarchy) {
    model_.setData(hierarchy);
    tree_.setRootVisible(!hierarchy.hasSyntheticRoot());
    // try to expand the tree in the same manner as before.
//    applyState();
  }

  public Set<H> getData() {
    return new HashSet<>(model_.data_.getMembers());
  }

  @Override
  public void select(H subject) {
    select(
      ((subject != null) ? Arrays.asList(subject) : Collections.emptyList())
    );
  }

  @Override
  public boolean contains(H entity) {
    return model_.contains(entity);
  }

  @Override
  public void setSelectionListener(EntitySelectionListener<? super H> listener) {
    selectionListener_ = ((listener != null) ? listener : EntitySelectionListener.NOP);
  }

  @Override
  public void select(Collection<H> entities) {
    try {
      silentSelectionChange_ = true;
      tree_.clearSelection();
      for (H node : entities) {
        if (model_.contains(node)) {
          final TreePath selPath = model_.getTreePath(node);

          tree_.expandPath(selPath);
        	tree_.addSelectionPath(selPath);
        	tree_.scrollPathToVisible(selPath);
        };
      }
    } finally {
      silentSelectionChange_ = false;
    }
    fireSelectionChanged();
  }

  public void enableDnd() {
//    final DndController dndController = Client.getInstance().getDndController();
//
//    dndController.registerSourceComponent(tree_, this);
//    dndController.registerTargetComponent(tree_, this);
  }

//  public TransferringEntities startDrag(Component comp, DragGestureEvent dge) {
//    if (comp == tree_) {
//      final Point     location  = dge.getDragOrigin();
//      final TreePath  path      = tree_.getPathForLocation(location.x, location.y);
//
//      if (path == null) {
//        tree_.setSelectionPaths(new TreePath[] {});
//      } else {
//        // return the selection and all of the childs.
//        List selection = getSelection();
//        Set depBos = new HashSet();
//        final Hierarchy h = new Hierarchy(getData());
//
//        for (Iterator iter = getSelection().iterator(); iter.hasNext(); ) {
//          final Hierarchical curr = (Hierarchical) iter.next();
//
//          depBos.addAll(h.getSubHierarchy(curr).getMembers());
//        }
//        return new TransferringEntities(new TypedEntities(selection), new TypedEntities(depBos));
//      }
//    }
//    return null;
//  }

//  /** @see com.bensler.flob.gui.dnd.DragBeginListener#dragMoved(com.bensler.flob.gui.evt.TypedEntities)
//   */
//  public void dragMoved(TransferringEntities transferringEntities) {
//    // remove the given bos
//    for (Entity entity : transferringEntities.getEntities()) {
//      if (contains(entity)) {
//        removeData((Hierarchical)entity);
//      }
//    };
//    applyState();
//  }

//  /** @see com.bensler.flob.gui.dnd.DragEndListener#dragOver(Component, Point)
//   */
//  public Entity dragOver(Component comp, Point location) {
//    if (comp == tree_) {
//      final TreePath treePath = tree_.getPathForLocation(location.x, location.y);
//
//      if (treePath != null) {
//        final Viewable entity = (Viewable)treePath.getLastPathComponent();
//
//        expand(entity, location);
//        return (Entity)entity;
//      }
//    }
//    return null;
//  }

//  private void expand(Viewable entity, Point point) {
//    final DragSituation dragSituation = new DragSituation(point, (Entity)entity);
//
//    if (dragSituation.equals(dragSituation_)) {
//      if ((dragSituation_.getTime() + 1000) < dragSituation.getTime()) {
//        tree_.expandPath(model_.getTreePath((Hierarchical)entity));
//        dragSituation_ = dragSituation;
//      }
//    } else {
//      dragSituation_ = dragSituation;
//    }
//  }
//}
//@SuppressWarnings("unchecked")
//public void dropped(TransferringEntities transferingBos) {
//  // just add the new bos to the hierarchy.
//  Set newData = new HashSet(getData());
//  newData.addAll(transferingBos.getEntities());
//  setData(newData);
//  requestFocus();

  public void requestFocus() {
    tree_.requestFocus();
  }

  @Override
  public H getSingleSelection() {
    return ((selection_.isEmpty() ? null : selection_.get(0)));
  }

  public Hierarchical<?> getRootNode() {
    return model_.getRoot();
  }

  /** Expands the given node and expand all nodes of its parent path.
   * Collapses the given node.  */
  public void expandCollapse(H node, boolean expand) {
    final TreePath path = new TreePath(model_.getPath(node));

    if (expand) {
      tree_.expandPath(path);
    } else {
      tree_.collapsePath(path);
    }
  }

  /** making node visible by expanding its parent path */
  public void makeVisible(H node) {
    final Object[] path = model_.getPathAsObjectArray(node);

    if (path.length > 1) {
      tree_.expandPath(new TreePath(path).getParentPath());
    }
  }

  public TreeState<H> getState() {
    if (!model_.data_.isEmpty()) {
      return new TreeState<>(
        tree_.getExpandedDescendants(new TreePath(model_.getRoot())),
        getSingleSelection()
      );
    } else {
      return null;
    }
  }

  /** repaints the whole tree. Should be called when the SingleView
   * has modified its behavior
   */
  public void repaint() {
    tree_.repaint();
  }

  @Override
  public boolean isEnabled() {
    return tree_.isEnabled();
  }

  @Override
  public void setToolTipText(String hint) {
    tree_.setToolTipText(hint);
  }

  @SuppressWarnings("unchecked")
  public TreeModel<H> getModel() {
    return (TreeModel<H>)tree_.getModel();
  }

  @Override
  public void focusGained(FocusEvent evt) {
    fireFocusGained();
    repaint();
  }

  @Override
  public void focusLost(FocusEvent evt) { }

  public void focusLost() {
    repaint();
  }

  private void fireFocusGained() {
    for (FocusListener listener : focusListeners_) {
      listener.focusGained(this);
    }
  }

  public void addFocusListener(FocusListener listener) {
    focusListeners_.add(listener);
  }

  public void removeFocusListener(FocusListener listener) {
    focusListeners_.remove(listener);
  }

}
