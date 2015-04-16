package com.bensler.decaf.swing.view;

import javax.swing.JLabel;

import com.bensler.decaf.swing.Viewable;
import com.bensler.decaf.util.tree.Hierarchy;

public class DefaultNullPolicy extends Object implements NullPolicy {

  public    final static  SimpleCellRenderer      NULL_RENDERER   = new SimpleCellRenderer();

  public void render(
    Object value, JLabel label, CellRenderer renderer, PropertyGetter getter
  ) {
    if (
      (value == null)
      || (!(value instanceof Viewable))
      || (value instanceof Hierarchy.Root)
    ) {
      NULL_RENDERER.render(null, " ", label);
      return;
    }
    final Viewable              viewable        = (Viewable)value;

    try {
      renderer.render(viewable, getter.getProperty(viewable), label);
    } catch (Exception e) {
      e.printStackTrace();
      NULL_RENDERER.render(viewable, " ", label);
    }
  }

  public String getPropertyString(Object propertyValue) {
    return ((propertyValue == null) ? " " : propertyValue.toString());
  }

}
