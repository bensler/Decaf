package com.bensler.decaf.swing.tree;

import javax.swing.JFrame;

public class EntityTreeTest extends JFrame {

  private final EntityTree tree_;

  EntityTreeTest() {
    tree_ = new EntityTree(propView);
    setSize(500, 800);

  }
  public static void main(String[] args) {
    new EntityTreeTest().setVisible(true);
  }

}
