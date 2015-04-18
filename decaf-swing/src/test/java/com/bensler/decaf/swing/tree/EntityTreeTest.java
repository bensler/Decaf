package com.bensler.decaf.swing.tree;

import javax.swing.JFrame;

import com.bensler.decaf.swing.view.PropertyViewImpl;

public class EntityTreeTest extends JFrame {

  private final EntityTree tree_;

  EntityTreeTest() {
    tree_ = new EntityTree(new PropertyViewImpl("name"));
    setSize(500, 800);

  }
  public static void main(String[] args) {
    new EntityTreeTest().setVisible(true);
  }

}
