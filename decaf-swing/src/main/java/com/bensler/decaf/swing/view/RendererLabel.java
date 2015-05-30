package com.bensler.decaf.swing.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * @author Bensler
 */
public class RendererLabel extends JLabel {

  protected final static  Border BORDER_NO_FOCUS  = new EmptyBorder(1, 1, 1, 1);

  /** Overridden for performance reasons. Works for property "text" only.
   */
  @Override
  protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    // String literals are interned...
    if (propertyName == "text") {
      super.firePropertyChange(propertyName, oldValue, newValue);
    }
  }

  public RendererLabel() {
    super();
    setOpaque(true);
  }

  /** Overridden empty for performance reasons.*/
  @Override
  public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {}
  /** Overridden empty for performance reasons.*/
  @Override
  public void firePropertyChange(String propertyName, char oldValue, char newValue) {}
  /** Overridden empty for performance reasons.*/
  @Override
  public void firePropertyChange(String propertyName, short oldValue, short newValue) {}
  /** Overridden empty for performance reasons.*/
  @Override
  public void firePropertyChange(String propertyName, int oldValue, int newValue) {}
  /** Overridden empty for performance reasons.*/
  @Override
  public void firePropertyChange(String propertyName, long oldValue, long newValue) {}
  /** Overridden empty for performance reasons.*/
  @Override
  public void firePropertyChange(String propertyName, float oldValue, float newValue) {}
  /** Overridden empty for performance reasons.*/
  @Override
  public void firePropertyChange(String propertyName, double oldValue, double newValue) {}
  /** Overridden empty for performance reasons.*/
  @Override
  public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}

  /** Overridden empty for performance reasons.*/
  @Override
  public void invalidate() {}
  /** Overridden empty for performance reasons.*/
  @Override
  public void repaint() {}
  /** Overridden empty for performance reasons.*/
  @Override
  public void repaint(Rectangle r) {}
  /** Overridden empty for performance reasons.*/
  @Override
  public void repaint(long tm, int x, int y, int width, int height) {}
  /** Overridden empty for performance reasons.*/
  @Override
  public void revalidate() {}
  /** Overridden empty for performance reasons.*/
  @Override
  public void validate() {}

  /** @return if the background is opaque and differs from
   *    the rendering component (JList/JTable/JTree).
   */
  @Override
  public boolean isOpaque() {
    final Color     bgColor     = getBackground();
          Component parent      = getParent();
    final boolean   sameOpaqueColor;

    if (parent != null) {
      parent = parent.getParent();
    }
    // p is the JList/JTable/JTree now.
    sameOpaqueColor = (
      (bgColor != null) && (parent != null)
      && bgColor.equals(parent.getBackground())
      && parent.isOpaque()
    );
    return !sameOpaqueColor && super.isOpaque();
  }

  public JLabel getComponent() {
    return this;
  }

}
