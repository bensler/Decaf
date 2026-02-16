package com.bensler.decaf.swing.dialog;

import java.awt.Component;

import javax.swing.JComponent;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ConfirmationDialog extends BasicContentPanel<Void, Boolean>{

  private final DialogAppearance appearance_;

  public ConfirmationDialog(DialogAppearance appearance) {
    this(appearance, null);
  }

  public ConfirmationDialog(DialogAppearance appearance, JComponent compnent) {
    super(appearance, new FormLayout("f:p:g", "f:p:g"));
    appearance_ = appearance;
    if (compnent != null) {
      add(compnent, new CellConstraints(1, 1));
    }
  }

  @Override
  public DialogAppearance getAppearance() {
    return appearance_;
  }

  @Override
  public Boolean getData() {
    return true;
  }

  @Override
  protected void setData(Void inData) {
    ctx_.setOkButtonText("Yes");
    ctx_.setCancelButtonText("No");
  }

  public boolean confirm(Component parent) {
    return new OkCancelDialog<>(parent, this).show(null).isPresent();
  }

}
