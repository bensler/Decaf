package com.bensler.decaf.swing.view;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class SimpleCellRenderer<E, P> extends Object implements CellRenderer<E, P> {

  private static final int TEXT_ICON_GAP = 5;

  protected final Icon icon_;
  protected final int alignment_;

  public SimpleCellRenderer() {
    this(null);
  }

  public SimpleCellRenderer(Icon icon) {
    this(icon, SwingConstants.LEFT);
  }

  public SimpleCellRenderer(Icon icon, int alignment) {
    icon_ = icon;
    alignment_ = alignment;
  }

  protected String getText(E entity, P property) {
    return (property != null) ? property.toString() : " ";
  }

  @Override
  public void render(E entity, P property, JLabel comp) {
    final Icon icon = getIcon(entity, property);

    comp.setText(getText(entity, property));
    comp.setIcon(icon);
    comp.setIconTextGap((icon != null) ? TEXT_ICON_GAP : 0);
    comp.setHorizontalAlignment(alignment_);
  }

  protected Icon getIcon(E entity, P property) {
    return icon_;
  }

}
