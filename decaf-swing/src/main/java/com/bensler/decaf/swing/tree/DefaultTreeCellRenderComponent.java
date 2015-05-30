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
public class DefaultTreeCellRenderComponent extends RendererLabel implements TreeRenderComponent {

  /** True if draws focus border around icon as well. */
  private   final         boolean       drawsFocusBorderAroundIcon_;
  /** Color to use for the focus indicator when the node has focus. */
  private   final         Color         borderSelectionColor_;

  private   final         Icon          icon_;
  /** Is the value currently selected. */
  protected               boolean       selected_;

  public DefaultTreeCellRenderComponent() {
    this(null);
  }

  public DefaultTreeCellRenderComponent(Icon icon) {
    final Object drawsFocusBorderAroundIcon = UIManager.get("Tree.drawsFocusBorderAroundIcon");

    icon_ = icon;
    borderSelectionColor_ = UIManager.getColor("Tree.selectionBorderColor");
    drawsFocusBorderAroundIcon_ = (drawsFocusBorderAroundIcon != null && ((Boolean)drawsFocusBorderAroundIcon).booleanValue());
    setOpaque(false);
    setBorder(new EmptyBorder(0, 2, 0, 3));
  }

  @Override
  public void paint(Graphics g) {
    final Color   bsColor     = borderSelectionColor_;
          int     imageOffset = getLabelStart();

    g.setColor(getBackground());
    if(getComponentOrientation().isLeftToRight()) {
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
      if(getComponentOrientation().isLeftToRight()) {
        g.drawRect(imageOffset, 0, getWidth() - imageOffset - 1, getHeight() - 1);
      } else {
        g.drawRect(0, 0, getWidth() - imageOffset - 1, getHeight() - 1);
      }
    }
  }

  private int getLabelStart() {
    final Icon currentIcon = getIcon();

    if ((currentIcon != null) && (getText() != null)) {
      return currentIcon.getIconWidth() + Math.max(0, getIconTextGap() - 1);
    }
    return 0;
  }

  @Override
  public void prepareForTree(
    JTree aTree, boolean selected, boolean expanded,
    boolean leaf, int row, boolean hasFocus
  ) {
    final TreeComponent<?> tree = (TreeComponent<?>)aTree;

    selected_ = selected;
    setFont(tree.getFont());
    setForeground(
      selected_ ? tree.getForegroundSelectionColor() : tree.getForeground()
    );
    // There needs to be a way to specify disabled icons.
    setEnabled(tree.isEnabled());
    if (!tree.isEnabled()) {
      setDisabledIcon(icon_);
    } else {
      setIcon(icon_);
    }
    setComponentOrientation(tree.getComponentOrientation());
    if (selected_) {
      setBackground(tree.hasFocus() ? tree.getBackgroundSelectionColor() : tree.getBackgroundSelectionColorUnfocused());
    } else {
      setBackground(tree.getBackground());
    }
  }

}
