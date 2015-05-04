package com.bensler.decaf.swing.tree;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

/** A {@link Robot}. */
public class Bender extends Object {

  private final ScheduledThreadPoolExecutor scheduler_;
  private final Robot robot_;

  public Bender() throws AWTException {
    scheduler_ = new ScheduledThreadPoolExecutor(1);
    robot_ = new Robot();
  }

  public void runDelayedInEventDispatcher(int milliseconds, final Runnable runnable) {
    scheduler_.schedule(new Runnable() {
      @Override
      public void run() {
        SwingUtilities.invokeLater(runnable);
      }
    }, milliseconds, TimeUnit.MILLISECONDS);
  }

  public void clickOn(int milliseconds, final Component component) {
    runDelayedInEventDispatcher(milliseconds, new Clicker(component));
  }

  public void assertEqualsVisually(int milliseconds, final Component component, final String imgResourceName) {
    runDelayedInEventDispatcher(milliseconds, new Snapshooter(component, imgResourceName));
  }

  final class Clicker implements Runnable {

    private final Component target_;

    public Clicker(Component clickTarget) {
      target_ = clickTarget;
    }

    @Override
    public void run() {
      final Point location = target_.getLocationOnScreen();
      final Dimension size = target_.getSize();

      robot_.mouseMove(
        (int)(location.getX() + (size.getWidth() / 2)),
        (int)(location.getY() + (size.getHeight() / 2))
      );
      robot_.mousePress(InputEvent.BUTTON1_DOWN_MASK);
      robot_.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

  }

  final class Snapshooter implements Runnable {

    private final Component target_;
    private final String imgResourceName_;

    public Snapshooter(Component clickTarget, String imgResourceName) {
      target_ = clickTarget;
      imgResourceName_ = imgResourceName;
    }

    @Override
    public void run() {
      getScreenShot(target_, imgResourceName_);
    }

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
      width, height, BufferedImage.TYPE_INT_ARGB
    );
    final Color warningColor = new Color(255, 128, 128, 255);
    boolean diff = false;

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        final int rgb1 = img1.getRGB(x, y);
        final int rgb2 = img2.getRGB(x, y);

        if (rgb1 != rgb2) {
          diffImg.setRGB(x, y, warningColor.getRGB());
          diff = true;
        } else {
          diffImg.setRGB(x, y, rgb1);
        }
      }
    }
    return (diff ? diffImg : null);
  }

}
