package com.bensler.decaf.testutil;

import java.awt.AWTException;

import org.junit.jupiter.api.Test;

class SimpleTest {

  private final Bender bender_;

  SimpleTest() throws AWTException {
    bender_ = new Bender("target/surefire-reports");
  }

  @Test
  void interactive() throws Exception {
    final SimpleDialog app = new SimpleDialog();

    app.dialog_.setLocation(bender_.getLargestScreensOrigin());
    app.dialog_.setVisible(true);
    bender_.clickOn(app.checkBox_);
    bender_.assertEqualsVisually(app.dialog_.getContentPane(), new TestImageSample());
    bender_.clickOn(app.button_);
    bender_.waitForAllTasksCompleted();
  }

}
