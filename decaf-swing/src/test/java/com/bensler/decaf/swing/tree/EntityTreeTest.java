package com.bensler.decaf.swing.tree;

import java.awt.Dialog.ModalityType;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.junit.Test;

import com.bensler.decaf.swing.view.PropertyViewImpl;
import com.bensler.decaf.util.tree.Hierarchy;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertYellow;

public class EntityTreeTest {

  public EntityTreeTest() throws UnsupportedLookAndFeelException {
    Plastic3DLookAndFeel.setCurrentTheme(new DesertYellow());
    UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
  }

  @Test
  public void interactive() {
    final JDialog dialog = new JDialog(null, "Decaf Swing Test", ModalityType.APPLICATION_MODAL);
    final EntityTree tree;

    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    tree = new EntityTree(new PropertyViewImpl("name"));
    tree.setData(createData());
    dialog.setContentPane(tree.getComponent());
    dialog.setSize(500, 800);
    dialog.setVisible(true);
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
