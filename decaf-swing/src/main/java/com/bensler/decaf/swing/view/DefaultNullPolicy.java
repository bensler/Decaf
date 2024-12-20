package com.bensler.decaf.swing.view;

import javax.swing.JLabel;

import com.bensler.decaf.swing.tree.SynthRoot;

public class DefaultNullPolicy<E> extends Object implements NullPolicy<E> {

  public    final static  SimpleCellRenderer      NULL_RENDERER   = new SimpleCellRenderer();

  @Override
  public void render(
    E value, JLabel label, CellRenderer renderer, PropertyGetter<E, ?> getter
  ) {
    if (
      (value == null)
      || (value instanceof SynthRoot)
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

}
