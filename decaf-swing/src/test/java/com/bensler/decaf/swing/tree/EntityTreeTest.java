package com.bensler.decaf.swing.tree;

import java.awt.AWTException;

import javax.swing.UnsupportedLookAndFeelException;

import org.junit.Test;

import com.bensler.decaf.testutil.Bender;
import com.bensler.decaf.testutil.TestImageSample;

public class EntityTreeTest {

  private final Bender bender_;

  public EntityTreeTest() throws UnsupportedLookAndFeelException, AWTException {
    bender_ = new Bender("target/surefire-reports");
  }

  @Test
  public void interactive() throws Exception {
    final SampleTreeDialog app = new SampleTreeDialog();

    app.dialog_.setLocation(bender_.getLargestScreensOrigin());
    app.dialog_.setVisible(true);
    bender_.assertEqualsVisually(app.dialog_.getContentPane(), new TestImageSample());
    bender_.clickOn(app.button_);
    bender_.waitForAllTasksCompleted();
  }

}
