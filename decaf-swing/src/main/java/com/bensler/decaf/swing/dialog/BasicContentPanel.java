package com.bensler.decaf.swing.dialog;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.bensler.decaf.swing.EntityComponent;
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
  public JComponent getComponent() {
    return this;
  }

  @Override
  public final void setContext(Context ctx) {
    contextSet(ctx_ = ctx);
  }

  protected void contextSet(Context ctx) { /* noop */ }

  protected abstract void setData(IN inData);

  @Override
  public final void setInData(IN inData) {
    setData(inData_ = inData);
    validate(null);
  }

  protected abstract boolean validateContent(Object eventSource);

  protected void addValidationSource(EntityComponent<?> entityComponent) {
    entityComponent.setSelectionListener((source, selection) -> validate(entityComponent));
  }

  protected void addValidationSource(JTextField textfield) {
    textfield.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void removeUpdate(DocumentEvent e) {
        validate(textfield);
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        validate(textfield);
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        validate(textfield);
      }
    });
  }

  void validate(Object validationSource) {
    ctx_.setValid(validateContent(validationSource));
  }

}
