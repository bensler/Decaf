package com.bensler.decaf.swing.view;

import javax.swing.Icon;
import javax.swing.JLabel;

public class SimpleCellRenderer extends Object implements CellRenderer {

  private static final int TEXT_ICON_GAP = 5;

  private   final         Icon      icon_;

  public SimpleCellRenderer() {
    this(null);
  }

  public SimpleCellRenderer(Icon icon) {
    icon_ = icon;
  }

  /** Sets the text to <code>value.toString()</code>.
   * @param cellValue <code>null</code> allowed.
   */
  public boolean setText(JLabel comp, Object cellValue) {
    final String text = renderString_(cellValue);

    comp.setText(text);
    return !text.trim().isEmpty();
  }

  private String renderString_(Object cellValue) {
    return ((cellValue == null) ? " " : renderString(cellValue));
  }

  protected String renderString(Object cellValue) {
    return cellValue.toString();
  }

  @Override
  public void render(Object viewable, Object cellValue, JLabel comp) {
    Icon icon = null;

    if (setText(comp, cellValue)) {
      icon = getIcon(viewable, cellValue);
    }
    comp.setIcon(icon);
    comp.setIconTextGap((icon != null) ? TEXT_ICON_GAP : 0);
    comp.setHorizontalAlignment(JLabel.LEFT);
  }

  public Icon getIcon() {
    return icon_;
  }

  public Icon getIcon(Object viewable, Object cellValue) {
    return getIcon();
  }

}
