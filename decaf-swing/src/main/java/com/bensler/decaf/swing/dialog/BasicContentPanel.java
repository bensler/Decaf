package com.bensler.decaf.swing.dialog;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;

public abstract class BasicContentPanel<IN, OUT> extends JPanel implements ContentPanel<IN, OUT> {

  protected final FormLayout layout_;

  protected IN inData_;
  protected Context ctx_;

  protected BasicContentPanel(FormLayout layout) {
    super(layout);
    layout_ = layout;
  }

  @Override
  public JComponent getComponent(Context ctx) {
    ctx_ = ctx;
    return this;
  }

  protected abstract void setData(IN inData);

  @Override
  public final void setInData(IN inData) {
    setData(inData_ = inData);
  }

}
