package com.bensler.decaf.testutil;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.SwingUtilities;

import org.opentest4j.AssertionFailedError;


/** A {@link Robot}. */
public class Bender extends Object {

  public final static int DELAY = 300;

  private final File reportsDir_;
  private final ReentrantLock lock_;
  private final Condition allTasksDone_;
  private final List<AssertionFailedError> failures_;
  private int taskCount_;

  private final ScheduledThreadPoolExecutor scheduler_;
  private final Robot robot_;

  private int currentDelay_;

  public Bender(String reportsDirRelativePath) throws AWTException {
    reportsDir_ = new File(System.getProperty("user.dir"), reportsDirRelativePath);
    reportsDir_.mkdirs();
    lock_ = new ReentrantLock();
    allTasksDone_ = lock_.newCondition();
    failures_ = new ArrayList<>();
    taskCount_ = 0;
    scheduler_ = new ScheduledThreadPoolExecutor(1);
    robot_ = new Robot();
    currentDelay_ = 0;
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
    scheduler_.schedule(new Runnable() {
      @Override
      public void run() {
        SwingUtilities.invokeLater(new RunnableWrapper(runnable));
      }
    }, (currentDelay_ += DELAY), TimeUnit.MILLISECONDS);
    taskCount_++;
  }

  public File getReportsDir() {
    return reportsDir_;
  }

  void addFailure(AssertionFailedError failure) {
    failures_.add(failure);
  }

  public void clickOn(final Component component) {
    clickOn(component, null);
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

  public void waitForAllTasksCompleted() {
    lock_.lock();
    try {
      if (taskCount_ > 0) {
        allTasksDone_.awaitUninterruptibly();
      }
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
