package com.bensler.decaf.swing.view;

import javax.swing.JLabel;


public interface CellRenderer<E, P> {

  public void render(E viewable, P cellValue, JLabel comp);

}
