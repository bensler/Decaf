package com.bensler.decaf.swing.tree;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeModel;

import com.bensler.decaf.swing.awt.ColorHelper;
import com.bensler.decaf.swing.view.PropertyView;
import com.bensler.decaf.util.tree.Hierarchical;

public class TreeComponent<H extends Hierarchical<H>> extends JTree implements RootChangeListener {

  private   final         Color                   backgroundSelectionColor_;
  private   final         Color                   backgroundSelectionColorUnfocused_;
  private   final         Color                   foregroundSelectionColor_;

  protected final         PropertyView<? super H, ?> view_;
  protected final         EntityTreeModel<H>      model_;

  private                 float                   widthFactor_;

  public TreeComponent(EntityTreeModel<H> model, PropertyView<? super H, ?> view) {
    super(model);
    backgroundSelectionColor_ = UIManager.getColor("Tree.selectionBackground");
    foregroundSelectionColor_ = UIManager.getColor("Tree.selectionForeground");
    backgroundSelectionColorUnfocused_ = ColorHelper.mix(
      backgroundSelectionColor_, 2,
      UIManager.getColor("Tree.background"), 1
    );
    view_ = view;
    model_ = model;
    widthFactor_ = -1;
    setRootVisible(false);
    setCellRenderer(view);
  }

  public Color getBackgroundSelectionColor() {
    return backgroundSelectionColor_;
  }

  public Color getForegroundSelectionColor() {
    return foregroundSelectionColor_;
  }

  public void setVisibleRowCount(int rowCount, float widthFactor) {
    setVisibleRowCount(rowCount);
    widthFactor_ = widthFactor;
  }

  @Override
  public Dimension getPreferredScrollableViewportSize() {
    final Dimension size = super.getPreferredScrollableViewportSize();

    if (widthFactor_ > 0) {
      size.width = Math.round(size.height * widthFactor_);
    }
    return size;
  }

  @Override
  public void setModel(TreeModel newModel) {
    if (treeModel != null) {
      ((EntityTreeModel<?>)treeModel).removeRootChangeListener(this);
    }
    super.setModel(newModel);
    ((EntityTreeModel<?>)treeModel).addRootChangeListener(this);
  }

  @Override
  public void rootChanged(RootProvider source) {
    if (source == treeModel) {
      setRootVisible(!source.hasSyntheticRoot());
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public String convertValueToText(
    Object value, boolean selected, boolean expanded,
    boolean leaf, int row, boolean hasFocus
  ) {
    // during constructor exec --vvvvvvvvvvvvv
    return (((value != null) && (view_ != null)) ? view_.getPropertyString((H)value) : "");
  }

  public Color getBackgroundSelectionColorUnfocused() {
    return backgroundSelectionColorUnfocused_;
  }

}
