package com.bensler.decaf.swing.view;

import java.awt.Component;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public interface PropertyView<E, P> extends Comparator<E>, ListCellRenderer<E>, TreeCellRenderer {

  @Override
  public int compare(E o1, E o2);

  public P getProperty(E viewable);

  /** for keyboard navigation in trees */
  public String getPropertyString(E viewable);

  public RenderComponentFactory getRenderComponentFactory();

  public JLabel renderLabel(JLabel label, E viewable);

  public Component getCellRendererComponent(
    JTable table, E viewable, Object cellValue, boolean isSelected, boolean hasFocus, int row, int column
  );

}
