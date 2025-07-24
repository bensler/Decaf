package com.bensler.decaf.swing.tree;

import static java.util.Objects.requireNonNull;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.action.FocusedComponentActionController;
import com.bensler.decaf.swing.selection.EntitySelectionListener;
import com.bensler.decaf.swing.selection.SelectionMode;
import com.bensler.decaf.swing.view.NoSelectionModel;
import com.bensler.decaf.swing.view.PropertyView;
import com.bensler.decaf.util.tree.Hierarchical;
import com.bensler.decaf.util.tree.Hierarchy;

/**
 * This is a tree that displays a Hierarchy.
 */
public class EntityTree<H extends Hierarchical<H>> extends Object implements EntityComponent<H>,
TreeSelectionListener, FocusListener {

  private final Class<H> entityClass_;
  private final Set<FocusListener> focusListeners_;

  private   final         JScrollPane         scrollPane_;

  private   final         List<H>             selection_;

  protected final         TreeComponent<H>    tree_;

  private final Set<EntitySelectionListener<H>> selectionListeners_;

  private                 boolean             silentSelectionChange_;

  protected               EntityTreeModel<H>  model_;

  private                 SelectionMode       selectionMode_;

  private                 TreeSelectionModel  defSelModel_;

  protected               boolean             editable_;

  public EntityTree(PropertyView<H, ?> view, Class<H> anEntityClass) {
    entityClass_ = anEntityClass;
    focusListeners_ = new HashSet<>();
    tree_ = createComponent(model_ = new EntityTreeModel<>(view), view);
    // update the selection BEFORE any listener is notified!
    tree_.addTreeSelectionListener(this);
    tree_.setShowsRootHandles(true);
    tree_.addFocusListener(this);
    tree_.setRowHeight(0); // ask Renderer

    editable_ = true;
    scrollPane_ = new JScrollPane(tree_);
    selection_ = new ArrayList<>(1);
    silentSelectionChange_ = false;
    setSelectionMode(SelectionMode.SINGLE);
    selectionListeners_ = new HashSet<>();
  }

  public void beforeCtxMenuOpen(MouseEvent evt) {
    final int selRow = tree_.getRowForLocation(evt.getX(), evt.getY());

    tree_.setSelectionRows((selRow > -1) ? new int[] {selRow} : new int[0]);
  }

  @Override
  public Class<H> getEntityClass() {
    return entityClass_;
  }

  protected TreeComponent<H> createComponent(EntityTreeModel<H> model, PropertyView<H, ?> view) {
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

  public void setShowsRootHandles(boolean visible) {
    tree_.setShowsRootHandles(visible);
  }

  public void setBackground(Color color) {
  	tree_.setBackground(color);
  	// in case the tree is smaller than the scrollpane
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
        tree_.setSelectionModel(NoSelectionModel.NOP_MODEL_TREE);
      } else {
        tree_.getSelectionModel().setSelectionMode(mode.getTreeConstant());
      }
      selectionMode_ = mode;
    }
  }

  @Override
  public void clearSelection() {
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
  }

  public void refireSelectionChanged() {
    final List<H> oldSelection = new ArrayList<>(selection_);

    select(Collections.emptyList());
    select(oldSelection);
  }

  protected void fireSelectionChanged() {
    if (!silentSelectionChange_) {
      final List<H> selection = List.copyOf(selection_);

      selectionListeners_.forEach(l -> l.selectionChanged(this, selection));
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
    return List.copyOf(selection_);
  }

  public void addData(H entity, boolean select) {
    model_.addNode(entity);
    if (select) {
      select(entity);
    }
  }

  public void removeNode(H ref) {
    model_.removeNode(ref);
  }

  public void removeTree(H ref) {
    model_.removeTree(ref);
  }

  public void setData(Hierarchy<H> hierarchy) {
    model_.setData(hierarchy);
  }

  public Hierarchy<H> getData() {
    return model_.getData();
  }

  @Override
  public void select(H subject) {
    select(((subject != null) ? Arrays.asList(subject) : Collections.emptyList()));
  }

  @Override
  public Optional<H> contains(Object entity) {
    return model_.contains(entity);
  }

  @Override
  public void addSelectionListener(EntitySelectionListener<H> listener) {
    selectionListeners_.add(requireNonNull(listener));
  }

  @Override
  public void select(Collection<H> entities) {
    try {
      silentSelectionChange_ = true;
      tree_.clearSelection();
      entities.stream().flatMap(node -> model_.contains(node).stream()).forEach(node -> {
        final TreePath selPath = model_.getTreePath(node);

        expandCollapse(node, true);
        tree_.addSelectionPath(selPath);
        tree_.scrollPathToVisible(selPath);
      });
    } finally {
      silentSelectionChange_ = false;
    }
    fireSelectionChanged();
  }

  public void requestFocus() {
    tree_.requestFocus();
  }

  public Hierarchical<?> getRootNode() {
    return model_.getRoot();
  }

  /** Expands the given node and expand all nodes of its parent path.
   * Collapses the given node.  */
  public void expandCollapse(H node, boolean expand) {
    final TreePath path = model_.getTreePath(node).getParentPath();

    if (expand) {
      tree_.expandPath(path);
    } else {
      tree_.collapsePath(path);
    }
  }

  /** repaints the whole tree. Should be called when the SingleView
   * has modified its behavior
   */
  public void repaint() {
    tree_.repaint();
  }

  @SuppressWarnings("unchecked")
  public EntityTreeModel<H> getModel() {
    return (EntityTreeModel<H>)tree_.getModel();
  }

  @Override
  public void focusGained(FocusEvent evt) {
    focusListeners_.forEach(l -> l.focusGained(this));
    repaint();
  }

  @Override
  public void focusLost(FocusEvent evt) {
    focusListeners_.forEach(l -> l.focusGained(this));
    repaint();
  }

  public void focusLost() { }

  @Override
  public void addFocusListener(FocusListener listener) {
    focusListeners_.add(requireNonNull(listener));
  }

  public void removeFocusListener(FocusListener listener) {
    focusListeners_.remove(listener);
  }

  public void setCtxActions(FocusedComponentActionController actions) {
    actions.attachTo(this, tree -> tree.getComponent().setToggleClickCount(0), this::beforeCtxMenuOpen);
  }

}
