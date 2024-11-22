package com.bensler.decaf.swing.tree;

import java.awt.AWTException;
import java.awt.Point;

import org.junit.jupiter.api.Test;

import com.bensler.decaf.testutil.Bender;
import com.bensler.decaf.testutil.TestImageSample;

class EntityTreeTest {

  private final Bender bender_;

  public EntityTreeTest() throws AWTException {
    bender_ = new Bender("target/surefire-reports");
  }

  @Test
  void interactive() throws Exception {
    final SampleTreeDialog app = new SampleTreeDialog();

    app.dialog_.setLocation(bender_.getLargestScreensOrigin());
    app.dialog_.setVisible(true);
    bender_.clickOn(app.tree_.getScrollPane(), new Point(83,  51));
    bender_.clickOn(app.table_.getScrollPane(), new Point(50,  10));
    bender_.assertEqualsVisually(app.dialog_.getContentPane(), new TestImageSample());
    bender_.clickOn(app.button_);
    bender_.waitForAllTasksCompleted();
  }

}
