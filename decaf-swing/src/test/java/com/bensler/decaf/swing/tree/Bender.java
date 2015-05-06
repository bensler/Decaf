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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import org.junit.Assert;
import org.junit.ComparisonFailure;

/** A {@link Robot}. */
public class Bender extends Object {

  private final File reportsDir_;
  private final ReentrantLock lock_;
  private final Condition allTasksDone_;
  private final List<ComparisonFailure> failures_;
  private int taskCount_;

  private final ScheduledThreadPoolExecutor scheduler_;
  private final Robot robot_;

  public Bender(String reportsDirRelativePath) throws AWTException {
    reportsDir_ = new File(System.getProperty("user.dir"), reportsDirRelativePath);
    reportsDir_.mkdirs();
    lock_ = new ReentrantLock();
    allTasksDone_ = lock_.newCondition();
    failures_ = new ArrayList<>();
    taskCount_ = 0;
    scheduler_ = new ScheduledThreadPoolExecutor(1);
    robot_ = new Robot();
  }

  private void runDelayedInEventDispatcher(int milliseconds, final Runnable runnable) {
    scheduler_.schedule(new Runnable() {
      @Override
      public void run() {
        SwingUtilities.invokeLater(new RunnableWrapper(runnable));
      }
    }, milliseconds, TimeUnit.MILLISECONDS);
    taskCount_++;
  }

  public void clickOn(int milliseconds, final Component component) {
    runDelayedInEventDispatcher(milliseconds, new Clicker(component));
  }

  public void assertEqualsVisually(int milliseconds, final Component component, final String imgResourceName) {
    runDelayedInEventDispatcher(milliseconds, new Snapshooter(component, imgResourceName));
  }

  public void waitForAllTasksCompleted() {
    lock_.lock();
    try {
      allTasksDone_.awaitUninterruptibly();
      if (!failures_.isEmpty()) {
        throw failures_.get(0);
      }
    } finally {
      lock_.unlock();
    }
  }

  final class RunnableWrapper implements Runnable {

    private final Runnable delegate_;

    RunnableWrapper(Runnable delegate) {
      delegate_ = delegate;
    }

    @Override
    public void run() {
      lock_.lock();
      try {
        delegate_.run();
      } finally {
        taskCount_--;
        if (taskCount_ <= 0) {
          allTasksDone_.signalAll();
        }
        lock_.unlock();
      }
    }

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

    private final Component component_;
    private final String expectedImgName_;

    public Snapshooter(Component target, String expectedImgName) {
      component_ = target;
      expectedImgName_ = expectedImgName;
    }

    @Override
    public void run() {
      try {
        final InputStream is = ClassLoader.getSystemResourceAsStream(expectedImgName_);
        final BufferedImage image;
        final BufferedImage actual = new BufferedImage(
          component_.getWidth(), component_.getHeight(),
          BufferedImage.TYPE_INT_RGB
        );
        final BufferedImage diffImage;

        Assert.assertNotNull("missing resource " + expectedImgName_, is);
        image = ImageIO.read(ImageIO.createImageInputStream(is));
        component_.paint(actual.getGraphics());
        diffImage = diffImage(image, actual);
        if (diffImage != null) {
          final AnimatedGifEncoder encoder = new AnimatedGifEncoder();
          final String failedGifName = expectedImgName_ + ".failed.gif";
          final String actualFileName = expectedImgName_ + ".actual.png";

          encoder.setDelay(700);   // ms
          encoder.start(new FileOutputStream(new File(reportsDir_, failedGifName)));
          encoder.addFrame(image);
          encoder.addFrame(diffImage);
          encoder.addFrame(actual);
          encoder.finish();
          ImageIO.write(actual, "png", new File(reportsDir_, actualFileName));
          failures_.add(new ComparisonFailure(
            "screenshot is different from expected appearance", expectedImgName_, failedGifName)
          );
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

}
