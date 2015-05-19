package com.bensler.decaf.testutil;

import java.awt.AWTException;

import javax.swing.UnsupportedLookAndFeelException;

import org.junit.Test;

import com.bensler.decaf.testutil.Bender;
import com.bensler.decaf.testutil.TestImageSample;

public class SimpleTest {

  private final Bender bender_;

  public SimpleTest() throws UnsupportedLookAndFeelException, AWTException {
    bender_ = new Bender("target/surefire-reports");
  }

  @Test
  public void interactive() throws Exception {
    final SimpleDialog app = new SimpleDialog();

    app.dialog_.setLocation(bender_.getLargestScreensOrigin());
    app.dialog_.setVisible(true);
    bender_.clickOn(app.checkBox_);
    bender_.assertEqualsVisually(app.dialog_.getContentPane(), new TestImageSample());
    bender_.clickOn(app.button_);
    bender_.waitForAllTasksCompleted();
  }

}
