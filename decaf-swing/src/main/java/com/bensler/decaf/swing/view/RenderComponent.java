package com.bensler.decaf.swing.view;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;

public interface RenderComponent {

  public JLabel getComponent();

  void prepareForList(JList<?> list, boolean selected, int index, boolean hasFocus);

  void prepareForTable(
    JTable table, boolean selected, int row, int column, boolean hasFocus
  );

}
