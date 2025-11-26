package com.bensler.decaf.testutil;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.Timer;

import org.opentest4j.AssertionFailedError;


/** A {@link Robot}. */
public class Bender extends Object {

  public final static int DELAY = 300;

  private final File reportsDir_;
  private final LinkedList<Runnable> tasks_;
  private final List<AssertionFailedError> failures_;

  private final Robot robot_;

  public Bender(String reportsDirRelativePath) throws AWTException {
    reportsDir_ = new File(System.getProperty("user.dir"), reportsDirRelativePath);
    reportsDir_.mkdirs();
    tasks_ = new LinkedList<>();
    failures_ = new ArrayList<>();
    robot_ = new Robot();
  }

  public Point getLargestScreensOrigin() {
    Rectangle candidate = null;

    for (final GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
      final Rectangle bounds = device.getDefaultConfiguration().getBounds();

      if (
        (candidate == null)
        || ((candidate.width < bounds.width) && (candidate.height < bounds.height))
      ) {
        candidate = bounds;
      }
    }

    return new Point(candidate.x, candidate.y);
  }

  private void runDelayedInEventDispatcher(final Runnable runnable) {
    tasks_.add(runnable);
  }

  public File getReportsDir() {
    return reportsDir_;
  }

  void addFailure(AssertionFailedError failure) {
    failures_.add(failure);
  }

  public void clickOn(final Component component, Point position) {
    runDelayedInEventDispatcher(new Clicker(component, position));
  }

  public void clickOn(final Component component, int keyEventKeyCode, Point position) {
    runDelayedInEventDispatcher(new Clicker(component, keyEventKeyCode, position));
  }

  public void assertEqualsVisually(final Component component, final TestImageSample sample) {
    runDelayedInEventDispatcher(new Snapshooter(this, component, sample));
  }

  public void finish(Container contentPane, TestImageSample testImageSample, JButton button) throws InterruptedException {
    assertEqualsVisually(contentPane, testImageSample);
    if (button != null) {
      clickOn(button, null);
    }
    execute();
  }

  private void execute() throws InterruptedException {
    final Timer timer = new Timer(DELAY, null);
    final CountDownLatch latch = new CountDownLatch(1);

    timer.addActionListener(_ -> {
      tasks_.removeFirst().run();
      if (tasks_.isEmpty()) {
        timer.stop();
        latch.countDown();
      }
    });
    timer.start();
    latch.await();
    if (!failures_.isEmpty()) {
      throw failures_.get(0);
    }
  }

  final class Clicker implements Runnable {

    private final Component target_;
    private final Integer modifier_;
    private final Point relativePosition_;

    public Clicker(Component clickTarget, Point position) {
      this(clickTarget, null, position);
    }

    public Clicker(Component clickTarget, Integer modifierKeycode, Point position) {
      target_ = clickTarget;
      modifier_ = modifierKeycode;
      relativePosition_ = ((position != null) ? new Point(position.x, position.y) : position);
    }

    @Override
    public void run() {
      final Point location = target_.getLocationOnScreen();
      final Dimension size = target_.getSize();
      final Point diff = (
        (relativePosition_ == null)
        ? new Point((size.width / 2), (size.height / 2))
        : relativePosition_
      );

      robot_.mouseMove(
        location.x + diff.x,
        location.y + diff.y
      );
      if (modifier_ != null) {
        robot_.keyPress(modifier_);
      }
      robot_.mousePress(InputEvent.BUTTON1_DOWN_MASK);
      robot_.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
      if (modifier_ != null) {
        robot_.keyRelease(modifier_);
      }
    }

  }

}
