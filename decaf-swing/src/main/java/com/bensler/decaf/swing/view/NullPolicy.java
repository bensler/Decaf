package com.bensler.decaf.swing.view;

import javax.swing.JLabel;

public interface NullPolicy<E> {

  public void render(
    E value, JLabel label, CellRenderer renderer, PropertyGetter<E, ?> getter
  );

}
