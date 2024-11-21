package com.bensler.decaf.swing.tree;

import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import com.bensler.decaf.swing.view.PropertyView;
import com.bensler.decaf.util.tree.Hierarchical;

public class CheckboxTreeComponent<H extends Hierarchical<H>> extends TreeComponent<H> {

  private final Set<H> checkedNodes_;

  public CheckboxTreeComponent(EntityTreeModel<H> newModel, PropertyView<H, ?> view) {
    super(newModel, view);
    checkedNodes_ = new HashSet<>();
    setCellRenderer(new CheckboxNodeRenderer(view));
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        final TreePath[] paths = getSelectionPaths();

        if ((paths != null) && (e.getKeyChar() == ' ')) {
          final List<?> nodes = Arrays.stream(paths).map(path -> path.getLastPathComponent()).toList();

//          if (checkedNodes_.containsAll(nodes)) { TODO
//            checkedNodes_.removeAll(nodes);
//            nodes.forEach(model_::fireNodeChanged);
//          } else {
//            final List<H> toCheck = nodes.stream().filter(not(checkedNodes_::contains)).toList();
//
//            checkedNodes_.addAll(toCheck);
//            toCheck.forEach(model_::fireNodeChanged);
//          }
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
            if (checkedNodes_.contains(node)) {
              checkedNodes_.remove(node);
            } else {
              checkedNodes_.add(node);
            }
//            model_.fireNodeChanged(node); TODO
          };
        }
      }
    });
  }

  class CheckboxNodeRenderer implements TreeCellRenderer {

    private final TreeCellRenderer delegate_;
    private final CheckboxRendererComponent rendererComponent;

    public CheckboxNodeRenderer(TreeCellRenderer delegate) {
      delegate_ = delegate;
      rendererComponent = new CheckboxRendererComponent();
    }

    @Override
    public Component getTreeCellRendererComponent(
      JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus
    ) {
      final boolean checked = checkedNodes_.contains(value);
      final Component delegateComp = delegate_.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

      delegateComp.setFont(delegateComp.getFont().deriveFont(checked ? Font.BOLD : Font.PLAIN));
      rendererComponent.setContent(isEnabled(), checked,delegateComp);
      return rendererComponent;
    }

  }

  void setCheckedNodes(List<? extends H> toBeChecked) {
    // TODO test if already checked, fire events
    checkedNodes_.clear();
    checkedNodes_.addAll(toBeChecked);
  }

}
