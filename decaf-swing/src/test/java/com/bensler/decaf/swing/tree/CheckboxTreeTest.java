package com.bensler.decaf.swing.tree;

import java.awt.AWTException;
import java.awt.Point;

import org.junit.jupiter.api.Test;

import com.bensler.decaf.testutil.Bender;
import com.bensler.decaf.testutil.TestImageSample;

class CheckboxTreeTest {

  private final Bender bender_;

  CheckboxTreeTest() throws AWTException {
    bender_ = new Bender("target/surefire-reports");
  }

  @Test
  void interactive() throws Exception {
    final CheckboxTreeDialog app = new CheckboxTreeDialog();

    app.dialog_.setLocation(bender_.getLargestScreensOrigin());
    app.dialog_.setVisible(true);
    bender_.clickOn(app.cbTree_.getScrollPane(), new Point(88,  93));
    bender_.clickOn(app.cbTree_.getScrollPane(), new Point(68,  244));
    bender_.clickOn(app.cbTree_.getScrollPane(), new Point(49,  229));
    bender_.clickOn(app.cbTree_.getScrollPane(), new Point(28,  196));
    bender_.finish(app.dialog_.getContentPane(), new TestImageSample());
  }

}
