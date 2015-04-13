package com.bensler.decaf.swing.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.bensler.decaf.swing.tree.TreeComponent;
import com.bensler.flob.gui.tree.IconProvider;
import com.bensler.flob.gui.tree.SimpleIconProvider;

/**
 */
public class DefaultTreeCellRenderComponent extends RendererBase implements TreeRenderComponent {

  /** True if draws focus border around icon as well. */
  private   final         boolean       drawsFocusBorderAroundIcon_;
  /** Color to use for the focus indicator when the node has focus. */
  private   final         Color         borderSelectionColor_;

  private   final         IconProvider  icons_;
  /** Is the value currently selected. */
  protected               boolean       selected_;
  /** True if has focus. */
  protected               boolean       hasFocus_;

  public DefaultTreeCellRenderComponent() {
    this(new SimpleIconProvider(null, null, null));
  }
  
  public DefaultTreeCellRenderComponent(IconProvider iconProvider) {
    final Object drawsFocusBorderAroundIcon = UIManager.get("Tree.drawsFocusBorderAroundIcon");
    
    icons_ = iconProvider;
    borderSelectionColor_ = UIManager.getColor("Tree.selectionBorderColor");
    drawsFocusBorderAroundIcon_ = (drawsFocusBorderAroundIcon != null && ((Boolean)drawsFocusBorderAroundIcon).booleanValue());
    setOpaque(false);
    setBorder(new EmptyBorder(0, 2, 0, 0));
  }

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

  /**
   * Overrides <code>JComponent.getPreferredSize</code> to
   * return slightly wider preferred size value.
   */
  public Dimension getPreferredSize() {
    Dimension        retDimension = super.getPreferredSize();

    if (retDimension != null) {
      retDimension = new Dimension(retDimension.width + 3, retDimension.height);
    }
    return retDimension;
  }

  public void prepareForTree(
    JTree aTree, boolean selected, boolean expanded, 
    boolean leaf, int row, boolean hasFocus
  ) {
    final TreeComponent tree = (TreeComponent)aTree;
    final Icon          icon = (
      leaf ? icons_.getLeafIcon()
      : (expanded ? icons_.getOpenIcon() : icons_.getClosedIcon())
    );
    
    hasFocus_ = tree.getTree().isFocused();
    selected_ = selected;
    
    setFont(tree.getFont());
    setForeground(
      selected_ ? tree.getForegroundSelectionColor() : tree.getForeground()
    );
    // There needs to be a way to specify disabled icons.
    setEnabled(tree.isEnabled());
    if (!tree.isEnabled()) {
      setDisabledIcon(icon);
    } else {
      setIcon(icon);
    }
    setComponentOrientation(tree.getComponentOrientation());
    if (selected_) {
      setBackground(hasFocus_ ? tree.getBackgroundSelectionColor() : tree.getBackgroundSelectionColorUnfocused());
    } else {
      setBackground(tree.getBackground());
    }
  }

}
