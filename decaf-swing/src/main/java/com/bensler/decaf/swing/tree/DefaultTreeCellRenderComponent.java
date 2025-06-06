package com.bensler.decaf.swing.tree;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.bensler.decaf.swing.view.RendererLabel;
import com.bensler.decaf.swing.view.TreeRenderComponent;

/**
 */
public class DefaultTreeCellRenderComponent implements TreeRenderComponent<RendererLabel> {

  private final RendererLabel component_;
  /** True if draws focus border around icon as well. */
  private final boolean drawsFocusBorderAroundIcon_;
  /** Color to use for the focus indicator when the node has focus. */
  private final Color borderSelectionColor_;
  /** Is the value currently selected. */
  private boolean selected_;

  public DefaultTreeCellRenderComponent() {
    final Object drawsFocusBorderAroundIcon = UIManager.get("Tree.drawsFocusBorderAroundIcon");

    borderSelectionColor_ = UIManager.getColor("Tree.selectionBorderColor");
    drawsFocusBorderAroundIcon_ = (drawsFocusBorderAroundIcon != null && ((Boolean)drawsFocusBorderAroundIcon).booleanValue());
    component_ = new RendererLabel() {
      @Override
      public void paint(Graphics g) {
        final Color   bsColor     = borderSelectionColor_;
        final Icon    currentIcon = getIcon();
              int     imageOffset = (
                ((currentIcon != null) && (getText() != null))
                ? (currentIcon.getIconWidth() + Math.max(0, getIconTextGap() - 1))
                : 0
              );

        g.setColor(getBackground());
        if (getComponentOrientation().isLeftToRight()) {
          g.fillRect(imageOffset, 0, getWidth() - imageOffset, getHeight());
        } else {
          g.fillRect(0, 0, getWidth() - imageOffset, getHeight());
        }
        super.paint(g);
        if (drawsFocusBorderAroundIcon_) {
          imageOffset = 0;
        }
        if (bsColor != null && selected_) {
          g.setColor(bsColor);
          if (getComponentOrientation().isLeftToRight()) {
            g.drawRect(imageOffset, 0, getWidth() - imageOffset - 1, getHeight() - 1);
          } else {
            g.drawRect(0, 0, getWidth() - imageOffset - 1, getHeight() - 1);
          }
        }
      }
    };
    component_.setOpaque(false);
    component_.setBorder(new EmptyBorder(0, 2, 0, 3));
  }

  @Override
  public RendererLabel prepareForTree(
    JTree aTree, boolean selected, boolean expanded,
    boolean leaf, int row, boolean hasFocus
  ) {
    final TreeComponent<?> tree = (TreeComponent<?>)aTree;

    selected_ = selected;
    component_.setFont(tree.getFont());
    component_.setForeground(
      selected_ ? tree.getForegroundSelectionColor() : tree.getForeground()
    );
    // There needs to be a way to specify disabled icons.
    component_.setEnabled(tree.isEnabled());
//    if (tree.isEnabled()) {
//      component_.setIcon(icon_);
//    } else {
//      component_.setDisabledIcon(icon_);
//    }
    component_.setComponentOrientation(tree.getComponentOrientation());
    if (selected_) {
      component_.setBackground(tree.hasFocus() ? tree.getBackgroundSelectionColor() : tree.getBackgroundSelectionColorUnfocused());
    } else {
      component_.setBackground(tree.getBackground());
    }
    return component_;
  }

}
