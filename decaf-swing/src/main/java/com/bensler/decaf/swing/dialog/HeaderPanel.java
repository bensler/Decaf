package com.bensler.decaf.swing.dialog;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bensler.decaf.swing.awt.IconComponent;
import com.bensler.decaf.swing.dialog.ContentPanel.ValidationContext;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class HeaderPanel {

  private final JPanel component_;
  private final JLabel errorLabel_;

  public HeaderPanel(DialogAppearance appearance) {
    final JLabel titleLabel = new JLabel(appearance.getTitle());
    final Font titleFont = titleLabel.getFont();
    final CellConstraints cc = new CellConstraints();

    component_ = new JPanel(new FormLayout(
      "3dlu, l:p:g, 3dlu, p,   3dlu",
      "3dlu, c:p:g, 3dlu, c:p:g, 3dlu"
    ));
    titleLabel.setFont(titleFont.deriveFont(titleFont.getSize() * 1.2f).deriveFont(Font.BOLD));
    component_.setBackground(Color.WHITE);
    errorLabel_ = new JLabel();

    if (appearance.isValidating()) {
      component_.add(titleLabel,  cc.xy(2, 2));
      component_.add(errorLabel_, cc.xy(2, 4));
    } else {
      component_.add(titleLabel, cc.xywh(2, 2, 1, 3, "l, c"));
    }
    component_.add(new IconComponent(appearance.getIcon()), cc.xywh(4, 2, 1, 3, "d, c"));
  }

  public JPanel getComponent() {
    return component_;
  }

  public void setErrors(ValidationContext validationCtx) {
    if (validationCtx.isValid()) {
      errorLabel_.setText(" ");
    } else {
      errorLabel_.setText("<html><font color=\"red\">%s</font></html>".formatted(validationCtx.popFirstErrorMsg()));
    }
  }

}