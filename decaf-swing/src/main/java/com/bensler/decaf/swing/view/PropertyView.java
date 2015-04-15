package com.bensler.decaf.swing.view;

import java.awt.Component;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import com.bensler.decaf.swing.Viewable;

public interface PropertyView extends Comparator<Viewable>, View, ListCellRenderer, TreeCellRenderer {
  
  public int compare(Viewable o1, Viewable o2);

  public boolean isSortable();
  
  public Object getProperty(Viewable viewable);

  public String getPropertyString(Viewable viewable);

  public RenderComponentFactory getRenderComponentFactory();
  
  public CellRenderer getRenderer();
  
  public PropertyGetter getGetter();

  public JLabel renderLabel(JLabel label, Viewable viewable);

  public Component getCellRendererComponent(
    JTable table, Viewable viewable, Object cellValue, boolean isSelected, boolean hasFocus, int row, int column
  );

}
