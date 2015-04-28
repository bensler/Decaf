package com.bensler.decaf.swing.tree;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
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
    final EntityTree tree = new EntityTree(new PropertyViewImpl("name"));
    final JButton button;

    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
    dialog.setLocation(500,  100);
    new ScheduledThreadPoolExecutor(1).schedule(new Runnable() {
      @Override
      public void run() {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
             Point location;


            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] defaultScreen = ge.getScreenDevices();

            GraphicsDevice dialogDevice = dialog.getGraphicsConfiguration().getDevice();

            getScreenShot(dialog.getContentPane());
            location = dialog.getLocationOnScreen();
            try {
              new Robot().mouseMove((int)location.getX(), (int)location.getY());
            } catch (AWTException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
        });
      }
    }, 10, TimeUnit.SECONDS);
    dialog.setVisible(true);
  }

  void getScreenShot(Component component) {
    try {
      final BufferedImage image = new BufferedImage(
        component.getWidth(), component.getHeight(),
        BufferedImage.TYPE_INT_RGB
      );
      component.paint( image.getGraphics() );
      ImageIO.write(image, "PNG", new File("/home/tbensler/tmp/test.png"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    };
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
