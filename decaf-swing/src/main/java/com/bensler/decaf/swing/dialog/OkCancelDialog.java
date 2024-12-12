package com.bensler.decaf.swing.dialog;

import java.awt.Component;
import java.awt.Window;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class OkCancelDialog<IN, OUT> extends JDialog implements ContentPanel.Context {

  private final JButton okButton_;

  private final ContentPanel<IN, OUT> contentPanel_;

  private OUT outData_;

  public OkCancelDialog(Component ownerComponent, DialogAppearance appearance, ContentPanel<IN, OUT> contentPanel) {
    super((Window)SwingUtilities.getRoot(ownerComponent), appearance.getWindowTitle(), ModalityType.TOOLKIT_MODAL);

    final CellConstraints cc = new CellConstraints();
    final JPanel mainPanel = new JPanel(new FormLayout(
      "3dlu, f:p:g, 3dlu",
      "3dlu, f:p:g, 3dlu, f:p, 3dlu"
    ));

    mainPanel.add((contentPanel_ = contentPanel).getComponent(this), cc.xy(2, 2));
    mainPanel.add(createButtonPanel(okButton_ = new JButton("Ok")), cc.xy(2, 4));
    setContentPane(mainPanel);
  }

  private JPanel createButtonPanel(JButton okButton) {
    final CellConstraints cc = new CellConstraints();
    final FormLayout buttonLayout = new FormLayout(
      "f:p:g, 3dlu, f:p, 3dlu, f:p",
      "f:p:g"
    );
    final JPanel buttonPanel = new JPanel(buttonLayout);
    final JButton cancelButton = new JButton("Cancel");

    buttonLayout.setColumnGroup(3, 5);
    buttonPanel.add(cancelButton, cc.xy(3,  1));
    okButton.addActionListener(evt -> {
      outData_ = contentPanel_.getData();
      setVisible(false);
    });
    cancelButton.addActionListener(evt -> setVisible(false));
    buttonPanel.add(okButton, cc.xy(5,  1));
    return buttonPanel;
  }

  public void show(IN input, Consumer<OUT> action) {
    setValid(false);
    outData_ = null;
    contentPanel_.setInData(input);
    pack();
    setVisible(true);
    Optional.ofNullable(outData_).ifPresent(action);
    dispose();
  }

  @Override
  public void setValid(boolean valid) {
    okButton_.setEnabled(valid);
  }

}
