package com.bensler.decaf.swing.view;

import javax.swing.Icon;
import javax.swing.JLabel;

import com.bensler.decaf.swing.Viewable;

public class SimpleCellRenderer extends Object implements CellRenderer {

  private   final         Icon      icon_;

  private   final         boolean   renderIconEvenWithEmptyText_;

  public SimpleCellRenderer() {
    this(null, false);
  }

  public SimpleCellRenderer(Icon icon) {
    this(icon, false);
  }

  public SimpleCellRenderer(boolean renderIconEvenWithEmptyText) {
    this(null, renderIconEvenWithEmptyText);
  }

  public SimpleCellRenderer(Icon icon, boolean renderIconEvenWithEmptyText) {
    super();
    icon_ = icon;
    renderIconEvenWithEmptyText_ = renderIconEvenWithEmptyText;
  }

  /** Sets the text to <code>value.toString()</code>.
   * @param cellValue <code>null</code> allowed.
   */
  public boolean setText(JLabel comp, Object cellValue) {
    final String text = renderString_(cellValue);

    comp.setText(text);
    return ((text.length() > 0) && (!text.equals(" ")));
  }

  private String renderString_(Object cellValue) {
    return ((cellValue == null) ? " " : renderString(cellValue));
  }

  protected String renderString(Object cellValue) {
    return cellValue.toString();
  }

  @Override
  public void render(Viewable value, Object cellValue, JLabel comp) {
    if (setText(comp, cellValue) || renderIconEvenWithEmptyText_) {
      comp.setIcon(getIcon(value, cellValue));
    } else {
      comp.setIcon(null);
    }
    comp.setHorizontalAlignment(JLabel.LEFT);
  }

  public Icon getIcon() {
    return icon_;
  }

  public Icon getIcon(Viewable value, Object cellValue) {
    return getIcon();
  }

}
