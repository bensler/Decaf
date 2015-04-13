package com.bensler.decaf.swing.view;

import javax.swing.JTable;

public interface TableRenderComponent extends RenderComponent {

  void prepareForTable(
    JTable table, boolean selected, int row, int column, boolean hasFocus
  );

}
