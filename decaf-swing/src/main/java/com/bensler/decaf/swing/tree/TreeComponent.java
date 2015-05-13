package com.bensler.decaf.swing.tree;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.bensler.decaf.swing.Viewable;
import com.bensler.decaf.swing.awt.ColorHelper;
import com.bensler.decaf.swing.view.PropertyView;

public class TreeComponent extends JTree implements TreeModel.RootChangeListener {

  private   final         Color                   backgroundSelectionColor_;
  private   final         Color                   backgroundSelectionColorUnfocused_;
  private   final         Color                   foregroundSelectionColor_;

  private   final         EntityTree              tree_;
  private   final         PropertyView            view_;

  private                 TreeSelectionListener   masterSelListener_;

  private                 float                   widthFactor_;

  public TreeComponent(EntityTree tree, TreeModel newModel, PropertyView view) {
    super(newModel);
    backgroundSelectionColor_ = UIManager.getColor("Tree.selectionBackground");
    foregroundSelectionColor_ = UIManager.getColor("Tree.selectionForeground");
    backgroundSelectionColorUnfocused_ = ColorHelper.mix(
      backgroundSelectionColor_, 2,
      UIManager.getColor("Tree.background"), 1
    );
    masterSelListener_ = null;
    tree_ = tree;
    view_ = view;
    widthFactor_ = -1;
    setRootVisible(false);
  }

  public Color getBackgroundSelectionColor() {
    return backgroundSelectionColor_;
  }

  public Color getForegroundSelectionColor() {
    return foregroundSelectionColor_;
  }

  /** Sets a listener that will be informed BEFORE all others! **/
  public void setMasterSelectionListener(TreeSelectionListener l) {
    masterSelListener_ = l;
  }

  protected void fireValueChanged(TreeSelectionEvent e) {
    if (masterSelListener_ != null) {
      masterSelListener_.valueChanged(e);
    }
    super.fireValueChanged(e);
  }

  public void setVisibleRowCount(int rowCount, float widthFactor) {
    setVisibleRowCount(rowCount);
    widthFactor_ = widthFactor;
  }

  public Dimension getPreferredScrollableViewportSize() {
    final Dimension size = super.getPreferredScrollableViewportSize();

    if (widthFactor_ > 0) {
      size.width = Math.round(size.height * widthFactor_);
    }
    return size;
  }

  @Override
  public void setModel(javax.swing.tree.TreeModel newModel) {
    if (treeModel != null) {
      ((TreeModel)treeModel).removeRootChangeListener(this);
    }
    super.setModel(newModel);
    ((TreeModel)treeModel).addRootChangeListener(this);
  }

  public void rootChanged(TreeModel source) {
    if (source == treeModel) {
      setRootVisible(!source.hasSyntheticRoot());
    }
  }

  public String convertValueToText(
    Object value, boolean selected, boolean expanded,
    boolean leaf, int row, boolean hasFocus
  ) {
    if ((value != null) && (view_ != null)) {
      final Object cellValue  = view_.getProperty((Viewable)value);

      if (cellValue != null) {
        return cellValue.toString();
      }
    }
    return "";
  }

  public Color getBackgroundSelectionColorUnfocused() {
    return backgroundSelectionColorUnfocused_;
  }

}
