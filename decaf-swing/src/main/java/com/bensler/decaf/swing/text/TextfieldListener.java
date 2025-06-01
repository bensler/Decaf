package com.bensler.decaf.swing.text;

import java.util.function.Consumer;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/** Adapter class to narrow three method interface {@link DocumentListener} down
 * to a simple {@link Consumer} .
 */
public class TextfieldListener implements DocumentListener {

  public static void addTextfieldListener(JTextField textfield, Consumer<String> consumer) {
    textfield.getDocument().addDocumentListener(new TextfieldListener(consumer));
  }

  private final Consumer<String> consumer_;

  public TextfieldListener(Consumer<String> consumer) {
    consumer_ = consumer;
  }

  public void notifyListener(DocumentEvent evt) {
    try {
      final Document document = evt.getDocument();
      consumer_.accept(document.getText(0, document.getLength()));
    } catch (BadLocationException ble) { /* Cannot happen*/ }
  }

  @Override
  public void removeUpdate(DocumentEvent evt) {
    notifyListener(evt);
  }

  @Override
  public void insertUpdate(DocumentEvent evt) {
    notifyListener(evt);
  }

  @Override
  public void changedUpdate(DocumentEvent evt) {
    notifyListener(evt);
  }

}