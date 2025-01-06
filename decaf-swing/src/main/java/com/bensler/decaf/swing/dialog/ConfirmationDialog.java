package com.bensler.decaf.swing.dialog;

import java.awt.Component;

import com.jgoodies.forms.layout.FormLayout;

public class ConfirmationDialog extends BasicContentPanel<Void, Boolean>{

  private final DialogAppearance appearance_;

  public ConfirmationDialog(DialogAppearance appearance) {
    super(new FormLayout("f:p:g", "f:p:g"));
    appearance_ = appearance;
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

  @Override
  protected boolean validateContent(Object eventSource) {
    return true;
  }

  public boolean show(Component parent) {
    return new OkCancelDialog<>(parent, this).show(null).isPresent();
  }

}
