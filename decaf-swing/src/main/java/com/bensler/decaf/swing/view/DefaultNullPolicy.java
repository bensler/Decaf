package com.bensler.decaf.swing.view;

import javax.swing.JLabel;

import com.bensler.decaf.swing.tree.TreeModel;

public class DefaultNullPolicy extends Object implements NullPolicy {

  public    final static  SimpleCellRenderer      NULL_RENDERER   = new SimpleCellRenderer();

  @Override
  public void render(
    Object value, JLabel label, CellRenderer renderer, PropertyGetter getter
  ) {
    if (
      (value == null)
      || (value instanceof TreeModel.Root)
    ) {
      NULL_RENDERER.render(null, " ", label);
    } else {
      try {
        renderer.render(value, getter.getProperty(value), label);
      } catch (Exception e) {
        e.printStackTrace(); // TODO
        NULL_RENDERER.render(value, " ", label);
      }
    }
  }

  @Override
  public String getPropertyString(Object propertyValue) {
    return ((propertyValue == null) ? " " : propertyValue.toString());
  }

}
