package com.bensler.decaf.swing.view;

import javax.swing.JLabel;

public interface RenderComponent {

  public void setValue(Object cellValue);
  
  public JLabel getComponent();
  
}
