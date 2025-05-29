package com.bensler.decaf.swing.view;

import javax.swing.JComponent;


public interface CellRenderer<E, P, C extends JComponent> {

  public C render(E viewable, P cellValue, C comp);

}
