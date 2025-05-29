package com.bensler.decaf.swing.view;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class SimpleCellRenderer<E, P> extends Object implements CellRenderer<E, P, JLabel> {

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
  public JLabel render(E entity, P property, JLabel label) {
    final Icon icon = getIcon(entity, property);

    label.setText(getText(entity, property));
    label.setIcon(icon);
    label.setIconTextGap((icon != null) ? TEXT_ICON_GAP : 0);
    label.setHorizontalAlignment(alignment_);
    return label;
  }

  protected Icon getIcon(E entity, P property) {
    return icon_;
  }

}
