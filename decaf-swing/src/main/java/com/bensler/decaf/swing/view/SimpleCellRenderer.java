package com.bensler.decaf.swing.view;

import javax.swing.Icon;
import javax.swing.JLabel;

public class SimpleCellRenderer extends Object implements CellRenderer {

  private   final         Icon      icon_;

  public SimpleCellRenderer() {
    this(null);
  }

  public SimpleCellRenderer(Icon icon) {
    super();
    icon_ = icon;
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
  public void render(Object viewable, Object cellValue, JLabel comp) {
    if (setText(comp, cellValue)) {
      comp.setIcon(getIcon(viewable, cellValue));
    } else {
      comp.setIcon(null);
    }
    comp.setHorizontalAlignment(JLabel.LEFT);
  }

  public Icon getIcon() {
    return icon_;
  }

  public Icon getIcon(Object viewable, Object cellValue) {
    return getIcon();
  }

}
