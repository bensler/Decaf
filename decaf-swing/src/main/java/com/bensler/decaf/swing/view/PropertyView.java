package com.bensler.decaf.swing.view;

import java.awt.Component;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import com.bensler.decaf.swing.Viewable;

public interface PropertyView<E> extends Comparator<E>, View, ListCellRenderer, TreeCellRenderer {

  @Override
  public int compare(E o1, E o2);

  public Object getProperty(E viewable);

  public String getPropertyString(E viewable);

  public RenderComponentFactory getRenderComponentFactory();

  public CellRenderer getRenderer();

  public PropertyGetter<E, ?> getGetter();

  public JLabel renderLabel(JLabel label, Viewable viewable);

  public Component getCellRendererComponent(
    JTable table, Viewable viewable, Object cellValue, boolean isSelected, boolean hasFocus, int row, int column
  );

}
