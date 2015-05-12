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

import org.junit.ComparisonFailure;

/** A {@link Robot}. */
public class Bender extends Object {

  public final static int DELAY = 300;

  private final File reportsDir_;
  private final ReentrantLock lock_;
  private final Condition allTasksDone_;
  private final List<ComparisonFailure> failures_;
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

  void addFailure(ComparisonFailure failure) {
    failures_.add(failure);
  }

  public void clickOn(final Component component) {
    runDelayedInEventDispatcher(new Clicker(component));
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

}
