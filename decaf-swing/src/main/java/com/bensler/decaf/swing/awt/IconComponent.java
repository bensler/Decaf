package com.bensler.decaf.swing.awt;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;

/** Adapter class to let an {@link Icon} appear as a {@link JComponent} which has a preferred size of
 * the icon.*/
public class IconComponent extends JComponent {

  private final Icon icon_;

  public IconComponent(Icon icon) {
    icon_ = icon;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    icon_.paintIcon(this, g, 0, 0);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(icon_.getIconWidth(), icon_.getIconWidth());
  }

}
