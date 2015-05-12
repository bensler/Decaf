package com.bensler.decaf.swing.tree;

import java.awt.AWTException;
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

import org.junit.Test;

import com.bensler.decaf.swing.view.PropertyViewImpl;
import com.bensler.decaf.testutil.Bender;
import com.bensler.decaf.testutil.TestImageSample;
import com.bensler.decaf.util.tree.Hierarchy;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertYellow;

public class EntityTreeTest {

  private final Bender bender_;

  public EntityTreeTest() throws UnsupportedLookAndFeelException, AWTException {
    Plastic3DLookAndFeel.setCurrentTheme(new DesertYellow());
    UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
    bender_ = new Bender("target/surefire-reports");
  }

  @Test
  public void interactive() throws Exception {
    final JDialog dialog = new JDialog(null, "Decaf Swing Test", ModalityType.MODELESS);
    final JPanel panel = new JPanel(new FormLayout(
      "3dlu, f:p:g, 3dlu",
      "3dlu, f:p:g, 3dlu, p, 3dlu"
    ));
    final EntityTree tree = new EntityTree(new PropertyViewImpl("name"));
    final JButton button;

    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    tree.setData(createData());
    panel.add(tree.getScrollPane(), new CellConstraints(2, 2));
    button = new JButton("Close");
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        dialog.setVisible(false);
      }
    });
    panel.add(button, new CellConstraints(2, 4, CellConstraints.RIGHT, CellConstraints.CENTER));
    panel.setPreferredSize(new Dimension(500, 750));

    dialog.setContentPane(panel);
    dialog.pack();
    dialog.setLocation(bender_.getLargestScreensOrigin());
    tree.expandCollapseAll(true);

    bender_.assertEqualsVisually(dialog.getContentPane(), new TestImageSample());
    bender_.clickOn(button);

    dialog.setVisible(true);

    bender_.waitForAllTasksCompleted();
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
