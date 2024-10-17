package com.bensler.decaf.swing.tree;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import com.bensler.decaf.swing.view.PropertyView;
import com.bensler.decaf.util.tree.Hierarchical;

public class CheckboxTreeComponent<H extends Hierarchical<H>> extends TreeComponent<H> {

  private final Set<H> selectedNodes_;

  public CheckboxTreeComponent(TreeModel<H> newModel, PropertyView<H, ?> view) {
    super(newModel, view);
    selectedNodes_ = new HashSet<>();
    setCellRenderer(new CheckboxNodeRenderer(view));
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        final TreePath path = getSelectionPath();

        if ((path != null) && (e.getKeyChar() == ' ')) {
          final H node = (H)path.getLastPathComponent();

          if (selectedNodes_.contains(node)) {
            selectedNodes_.remove(node);
          } else {
            selectedNodes_.add(node);
          }
          getModel().valueForPathChanged(path, node);
        }
      }
    });
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        final TreePath path = getPathForLocation(e.getX(), e.getY());

        if (path != null) {
          final H node = (H)path.getLastPathComponent();
          final Rectangle bounds = getPathBounds(path);
          final CheckboxRendererComponent rendererComp = (CheckboxRendererComponent)getCellRenderer().getTreeCellRendererComponent(
            CheckboxTreeComponent.this, node, true,
            isExpanded(path),
            getModel().isLeaf(node),
            getRowForPath(path),
            true
          );

          rendererComp.setBounds(bounds);
          if (rendererComp.checkboxHit(e.getX(), e.getY())) {
            if (selectedNodes_.contains(node)) {
              selectedNodes_.remove(node);
            } else {
              selectedNodes_.add(node);
            }
          };
        }
      }
    });
  }

  class CheckboxNodeRenderer implements TreeCellRenderer {

    private final TreeCellRenderer delegate_;
    private final JLabel emptyLabel;
    private final CheckboxRendererComponent rendererComponent;

    public CheckboxNodeRenderer(TreeCellRenderer delegate) {
      delegate_ = delegate;
      emptyLabel = new JLabel();
      rendererComponent = new CheckboxRendererComponent();
    }

    @Override
    public Component getTreeCellRendererComponent(
      JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus
    ) {
      rendererComponent.setContent(
        selected, expanded, leaf,
        delegate_.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus)
      );
//      if (
//        (value instanceof DefaultMutableTreeNode mutableTreeNode)
//        && (mutableTreeNode.getUserObject() instanceof NamedVector namedVector)
//      ) {
//        rendererComponent.setContent(tree.isEnabled(), selected, selectedNodes.contains(namedVector), namedVector.getName());
//        return rendererComponent;
//      }
      return rendererComponent;
    }

  }

}
