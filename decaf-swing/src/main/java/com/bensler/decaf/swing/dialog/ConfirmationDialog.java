package com.bensler.decaf.swing.dialog;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JDialog;

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
    final JDialog dialog = ctx_.getDialog();
    final Rectangle dialogBounds = ctx_.getDialog().getBounds();
    final Rectangle parentBounds = dialog.getParent().getBounds();

    dialogBounds.x = (int)(parentBounds.getCenterX() - (dialogBounds.width / 2.0));
    dialogBounds.y = (int)(parentBounds.getCenterY() - (dialogBounds.height / 2.0));
    dialog.setBounds(dialogBounds);
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
