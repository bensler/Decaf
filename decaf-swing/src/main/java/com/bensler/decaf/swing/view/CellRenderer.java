package com.bensler.decaf.swing.view;

import javax.swing.JLabel;


public interface CellRenderer {

  public void render(Object viewable, Object cellValue, JLabel comp);

}
