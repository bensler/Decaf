package com.bensler.decaf.swing.tree;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.bensler.decaf.swing.view.PropertyViewImpl;
import com.bensler.decaf.util.tree.Hierarchy;
import com.jgoodies.plaf.plastic.Plastic3DLookAndFeel;
import com.jgoodies.plaf.plastic.theme.DesertYellow;

public class EntityTreeTest extends JFrame {

  private final EntityTree tree_;

  EntityTreeTest() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    tree_ = new EntityTree(new PropertyViewImpl("name"));
    tree_.setData(createData());
    setContentPane(tree_.getComponent());
    setSize(500, 800);
  }

  public static void main(String[] args) throws UnsupportedLookAndFeelException {
    Plastic3DLookAndFeel.setCurrentTheme(new DesertYellow());
    UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
    new EntityTreeTest().setVisible(true);
  }

  private Hierarchy createData() {
    final Hierarchy tree = new Hierarchy();

    final Folder root = new Folder(null, "/");
    final Folder home = new Folder(root, "home");
    final Folder bobsHome = new Folder(home, "bob");
    final Folder alicesHome = new Folder(home, "alice");

    tree.add(alicesHome);
    tree.add(bobsHome);
    tree.add(home);
    tree.add(root);
    return tree;
  }

}
