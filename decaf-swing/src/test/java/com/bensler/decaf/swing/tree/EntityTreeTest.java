package com.bensler.decaf.swing.tree;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.junit.Test;

import com.bensler.decaf.swing.view.PropertyViewImpl;
import com.bensler.decaf.util.tree.Hierarchy;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
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
    final JPanel panel = new JPanel(new FormLayout(
      "3dlu, f:p:g, 3dlu",
      "3dlu, f:p:g, 3dlu, p, 3dlu"
    ));
    final EntityTree tree;
    final JButton button;

    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    tree = new EntityTree(new PropertyViewImpl("name"));
    tree.setData(createData());
    panel.add(new JScrollPane(tree.getComponent()), new CellConstraints(2, 2));
    button = new JButton("Close");
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        dialog.setVisible(false);
      }
    });
    panel.add(button, new CellConstraints(2, 4, CellConstraints.RIGHT, CellConstraints.CENTER));

    dialog.setContentPane(panel);
    dialog.setSize(500, 800);
    dialog.setVisible(true);
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

}