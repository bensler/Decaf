package com.bensler.decaf.swing.view;

import javax.swing.JLabel;

public interface NullPolicy {

  public void render(
    Object value, JLabel label, CellRenderer renderer, PropertyGetter getter
  );

  public String getPropertyString(Object propertyValue);

}
