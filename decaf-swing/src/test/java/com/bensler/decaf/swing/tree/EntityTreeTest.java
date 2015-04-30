package com.bensler.decaf.swing.tree;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
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

  private final Bender bender_;

  public EntityTreeTest() throws UnsupportedLookAndFeelException, AWTException {
    Plastic3DLookAndFeel.setCurrentTheme(new DesertYellow());
    UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
    bender_ = new Bender();
  }

  @Test
  public void interactive() throws Exception {
    final JDialog dialog = new JDialog(null, "Decaf Swing Test", ModalityType.APPLICATION_MODAL);
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

    dialog.setContentPane(panel);
    dialog.setSize(500, 800);
    dialog.setLocation(500,  100);
    tree.expandCollapseAll(true);

    bender_.clickOn(1000, button);

    dialog.setVisible(true);
  }

  void getScreenShot(Component component, String expected) {
    try {
      final BufferedImage image = ImageIO.read(ImageIO.createImageInputStream(
        ClassLoader.getSystemResourceAsStream(expected)
      ));
      final BufferedImage actual = new BufferedImage(
        component.getWidth(), component.getHeight(),
        BufferedImage.TYPE_INT_RGB
      );
      final BufferedImage diffImage;

      component.paint(actual.getGraphics());
      diffImage = diffImage(image, actual);
      if (diffImage != null) {
        final AnimatedGifEncoder encoder = new AnimatedGifEncoder();

        encoder.setDelay(700);   // ms
        encoder.start(new FileOutputStream(new File(System.getProperty("user.dir"), "test.gif")));
        encoder.addFrame(image);
        encoder.addFrame(diffImage);
        encoder.addFrame(actual);
        encoder.finish();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    };
  }

  private BufferedImage diffImage(BufferedImage img1, BufferedImage img2) {
    final int width1 = img1.getWidth();
    final int width2 = img2.getWidth();
    final int height1 = img1.getHeight();
    final int height2 = img2.getHeight();
    final int width = Math.max(width1, width2);
    final int height = Math.max(height1, height2);
    final BufferedImage diffImg = new BufferedImage(
      width, height, BufferedImage.TYPE_INT_RGB
    );
    boolean diff = false;

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        final int rgb1 = img1.getRGB(x, y);
        final int rgb2 = img2.getRGB(x, y);

        if (rgb1 != rgb2) {
          diffImg.setRGB(x, y, Color.RED.getRGB());
          diff = true;
        } else {
          diffImg.setRGB(x, y, rgb1);
        }
      }
    }
    return (diff ? diffImg : null);
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
