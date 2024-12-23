package com.bensler.decaf.swing.tree;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 *
 */
public class CheckboxRendererComponent extends JComponent {

  protected final static  int GAP  = 1;

  private final JCheckBox checkbox_;

  private Component contentComp_;

  public CheckboxRendererComponent() {
    final Font fontValue = UIManager.getFont("Tree.font");

    checkbox_ = new JCheckBox();
    if (fontValue != null) {
      checkbox_.setFont(fontValue);
    }
    checkbox_.setFocusPainted(false);
    checkbox_.setBorder(new EmptyBorder(GAP, GAP, GAP, GAP));
    checkbox_.setOpaque(false);
    add(checkbox_);
  }

  public void setContent(boolean enabled, boolean checked, Component contentComp) {
    if ((contentComp_ != null) && (contentComp_ != contentComp)) {
      remove(contentComp_);
      contentComp_ = null;
    }
    checkbox_.setEnabled(enabled);
    checkbox_.setSelected(checked);
    if (contentComp_ == null) {
      add(contentComp_ = contentComp);
    }
  }

  public JCheckBox getCheckbox() {
    return checkbox_;
  }

  public boolean isCheckboxSelected() {
    return checkbox_.isSelected();
  }

  @Override
  public Dimension getPreferredSize() {
    final Dimension cbPrefSize = checkbox_.getPreferredSize();
    final Dimension labelPrefSize = contentComp_.getPreferredSize();

    return new Dimension(
      cbPrefSize.width + labelPrefSize.width + (3 * GAP),
      Math.max(cbPrefSize.height, labelPrefSize.height) + (2 * GAP)
    );
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);

    final Dimension cbPrefSize = checkbox_.getPreferredSize();
    final Dimension labelPrefSize = contentComp_.getPreferredSize();
    final int vCenter = Math.max(cbPrefSize.height, labelPrefSize.height) / 2;

    checkbox_.setBounds(
      GAP,
      GAP + (vCenter - (cbPrefSize.height / 2)),
      cbPrefSize.width, cbPrefSize.height
    );
    contentComp_.setBounds(
      GAP + cbPrefSize.width + (2 * GAP),
      GAP + (vCenter - (labelPrefSize.height / 2)),
      labelPrefSize.width, labelPrefSize.height
    );
  }

  public boolean checkboxHit(int x, int y) {
    final Rectangle rendererBounds = getBounds();
    final Rectangle checkboxBounds = checkbox_.getBounds();

    checkboxBounds.translate(rendererBounds.x, rendererBounds.y);
    return checkboxBounds.contains(x, y);
  }

  // Override a bunch of (unneeded) methods to improve performance.
  @Override
  protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
  @Override
  public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {}
  @Override
  public void firePropertyChange(String propertyName, char oldValue, char newValue) {}
  @Override
  public void firePropertyChange(String propertyName, short oldValue, short newValue) {}
  @Override
  public void firePropertyChange(String propertyName, int oldValue, int newValue) {}
  @Override
  public void firePropertyChange(String propertyName, long oldValue, long newValue) {}
  @Override
  public void firePropertyChange(String propertyName, float oldValue, float newValue) {}
  @Override
  public void firePropertyChange(String propertyName, double oldValue, double newValue) {}
  @Override
  public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
  @Override
  public void invalidate() {}
  @Override
  public void repaint() {}
  @Override
  public void repaint(Rectangle r) {}
  @Override
  public void repaint(long tm, int x, int y, int width, int height) {}
  @Override
  public void revalidate() {}
  @Override
  public void validate() {}

  /** @return if the background is opaque and differs from
   *    the rendering component (JList/JTable/JTree).
   */
  @Override
  public boolean isOpaque() {
    return false;
  }

}
