package com.bensler.decaf.swing.view;

import javax.swing.Icon;
import javax.swing.JLabel;

import com.bensler.decaf.swing.Viewable;

public class SimpleCellRenderer extends Object implements CellRenderer {

  private   final         BinResKey<Icon> icon_;

  private   final         boolean         renderIconEvenWithEmptyText_;

  public SimpleCellRenderer() {
    this(null, false);
  }

  public SimpleCellRenderer(BinResKey<Icon> iconKey) {
    this(iconKey, false);
  }

  public SimpleCellRenderer(boolean renderIconEvenWithEmptyText) {
    this(null, renderIconEvenWithEmptyText);
  }

  public SimpleCellRenderer(BinResKey<Icon> iconKey, boolean renderIconEvenWithEmptyText) {
    super();
    icon_ = iconKey;
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

  public void render(Viewable value, Object cellValue, JLabel comp) {
    if (setText(comp, cellValue) || renderIconEvenWithEmptyText_) {
      comp.setIcon(getIcon(getIcon(value, cellValue)));
    } else {
      comp.setIcon(null);
    }
    comp.setHorizontalAlignment(JLabel.LEFT);
  }

  public Icon getIcon() {
    return getIcon(icon_);
  }

  protected Icon getIcon(BinResKey<Icon> iconKey) {
    return ((iconKey == null) ? null : Client.getRes().getRessource(iconKey));
  }

  public BinResKey<Icon> getIconKey() {
    return icon_;
  }

  public BinResKey<Icon> getIcon(Viewable value, Object cellValue) {
    return icon_;
  }

}
