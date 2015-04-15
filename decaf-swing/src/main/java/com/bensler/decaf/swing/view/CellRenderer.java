package com.bensler.decaf.swing.view;

import javax.swing.JLabel;

import com.bensler.decaf.swing.Viewable;


public interface CellRenderer {

  public void render(Viewable value, Object cellValue, JLabel comp);

}
 