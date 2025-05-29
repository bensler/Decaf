package com.bensler.decaf.swing.view;

import javax.swing.JLabel;

import com.bensler.decaf.swing.tree.SynthRoot;

public class NullPolicy<E, P> extends Object {

  public final SimpleCellRenderer<E, String> NULL_RENDERER   = new SimpleCellRenderer<>();

  public void render(
    E value, JLabel label, CellRenderer<E, P> renderer, PropertyGetter<E, P> getter
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
