package com.bensler.decaf.swing.tree;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.bensler.decaf.swing.view.NamePropertyGetter;
import com.bensler.decaf.swing.view.PropertyViewImpl;
import com.bensler.decaf.util.cmp.CollatorComparator;
import com.bensler.decaf.util.tree.Folder;
import com.bensler.decaf.util.tree.Hierarchy;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertYellow;

public class SampleTreeDialog implements ActionListener {

  public static void main(String[] args) throws UnsupportedLookAndFeelException {
    new SampleTreeDialog().dialog_.setVisible(true);
  }

  final JDialog dialog_;
  final EntityTree<Folder> tree_;
  final JButton button_;

  public SampleTreeDialog() throws UnsupportedLookAndFeelException {
    Plastic3DLookAndFeel.setCurrentTheme(new DesertYellow());
    UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
    dialog_ = new JDialog(null, "Decaf Swing Test", ModalityType.MODELESS);
    final JPanel panel = new JPanel(new FormLayout(
      "3dlu, f:p:g, 3dlu",
      "3dlu, f:p:g, 3dlu, p, 3dlu"
    ));
    tree_ = new EntityTree<>(new PropertyViewImpl<>(
      new NamePropertyGetter<String>("name", CollatorComparator.COLLATOR_COMPARATOR))
    );
    dialog_.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    tree_.setData(createData());
    panel.add(tree_.getScrollPane(), new CellConstraints(2, 2));
    button_ = new JButton("Close");
    button_.addActionListener(this);
    panel.add(button_, new CellConstraints(2, 4, CellConstraints.RIGHT, CellConstraints.CENTER));
    panel.setPreferredSize(new Dimension(500, 750));

    dialog_.setContentPane(panel);
    dialog_.pack();
    tree_.expandCollapseAll(true);
  }

  private Hierarchy<Folder> createData() {
    final Hierarchy<Folder> tree = new Hierarchy<>();

    final Folder root = new Folder(null, "/");
    final Folder home = new Folder(root, "home");
    final Folder bobsHome = new Folder(home, "bob");
    final Folder alicesHome = new Folder(home, "alice");
    final Folder winRoot = new Folder(null, "C:");

    tree.add(alicesHome);
    tree.add(bobsHome);
    tree.add(home);
    tree.add(root);
    tree.add(winRoot);
    return tree;
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    dialog_.setVisible(false);
  }

}
