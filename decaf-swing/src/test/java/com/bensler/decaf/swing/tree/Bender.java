package com.bensler.decaf.swing.tree;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
