package com.bensler.decaf.swing.tree;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;

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
    final JScrollPane table = app.table_.getScrollPane();
    bender_.clickOn(table,                      new Point(410, 60)); // select
    bender_.clickOn(table, KeyEvent.VK_CONTROL, new Point(260, 30)); // add selected row
    bender_.clickOn(table,                      new Point(410, 10)); // sort
    bender_.clickOn(table,                      new Point(270, 10)); // sort
    bender_.clickOn(table,                      new Point( 90, 10)); // sort
    bender_.waitForAllTasksCompleted();
    bender_.assertEqualsVisually(app.dialog_.getContentPane(), new TestImageSample());
    bender_.clickOn(app.button_);
  }

}
