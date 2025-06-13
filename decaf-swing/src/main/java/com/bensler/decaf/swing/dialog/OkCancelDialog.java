package com.bensler.decaf.swing.dialog;

import static java.awt.Dialog.ModalityType.TOOLKIT_MODAL;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

import java.awt.Component;
import java.awt.Window;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.bensler.decaf.swing.awt.WindowHelper;
import com.bensler.decaf.swing.dialog.ContentPanel.ValidationContext;
import com.bensler.decaf.util.prefs.BulkPrefPersister;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class OkCancelDialog<IN, OUT> extends JDialog implements ContentPanel.Context {

  private final JButton okButton_;

  private final JButton cancelButton_;

  private final HeaderPanel headerPanel_;

  private final ContentPanel<IN, OUT> contentPanel_;

  private Optional<JComponent> compToFocus_;

  private Optional<OUT> outData_;

  private Optional<BulkPrefPersister> prefs_;

  public OkCancelDialog(Component ownerComponent, ContentPanel<IN, OUT> contentPanel) {
    super((Window)SwingUtilities.getRoot(ownerComponent), contentPanel.getAppearance().getWindowTitle(), TOOLKIT_MODAL);

    final CellConstraints cc = new CellConstraints();
    final JPanel mainPanel = new JPanel(new FormLayout(
      "3dlu, f:p:g, 3dlu",
      "3dlu, f:p, 3dlu, f:p:g, 3dlu, f:p, 3dlu"
    ));

    compToFocus_ = Optional.empty();
    prefs_ = Optional.empty();
    mainPanel.add((headerPanel_ = new HeaderPanel(contentPanel.getAppearance())).getComponent(), cc.xy(2, 2));
    mainPanel.add((contentPanel_ = contentPanel).getComponent(), cc.xy(2, 4));
    mainPanel.add(createButtonPanel(okButton_ = new JButton("Ok"), cancelButton_ = new JButton("Cancel")), cc.xy(2, 6));
    setContentPane(mainPanel);
    pack();
    WindowHelper.centerOnParent(this);
    contentPanel_.setContext(this);
    rootPane.setDefaultButton(okButton_);
    rootPane.registerKeyboardAction(evt -> setVisible(false), KeyStroke.getKeyStroke(VK_ESCAPE, 0), WHEN_IN_FOCUSED_WINDOW);
    setMinimumSize(getPreferredSize());
  }

  private JPanel createButtonPanel(JButton okButton, JButton cancelButton) {
    final CellConstraints cc = new CellConstraints();
    final FormLayout buttonLayout = new FormLayout(
      "f:p:g, 3dlu, f:p, 3dlu, f:p",
      "f:p:g"
    );
    final JPanel buttonPanel = new JPanel(buttonLayout);

    buttonLayout.setColumnGroup(3, 5);
    buttonPanel.add(cancelButton, cc.xy(3,  1));
    okButton.addActionListener(evt -> {
      outData_ = Optional.ofNullable(contentPanel_.getData());
      setVisible(false);
    });
    cancelButton.addActionListener(evt -> setVisible(false));
    buttonPanel.add(okButton, cc.xy(5,  1));
    return buttonPanel;
  }

  public Optional<Optional<OUT>> show(IN input) {
    show(input, none -> {});
    return Optional.ofNullable(outData_);
  }

  public void show(IN input, Consumer<OUT> action) {
    setValid(new ValidationContext());
    outData_ = null;
    contentPanel_.setInData(input);
    SwingUtilities.invokeLater(() -> compToFocus_.ifPresent(JComponent::requestFocusInWindow));
    setVisible(true);
    prefs_.ifPresent(BulkPrefPersister::store);
    if (outData_ != null) {
      outData_.ifPresent(action);
    }
    dispose();
  }

  @Override
  public void setComponentToFocus(JComponent comp) {
    compToFocus_ = Optional.ofNullable(comp);
  }

  @Override
  public void setOkButtonText(String okButtonText) {
    okButton_.setText(okButtonText);
  }

  @Override
  public void setCancelButtonText(String cancelButtonText) {
    cancelButton_.setText(cancelButtonText);
  }

  @Override
  public void setValid(ValidationContext validationCtx) {
    okButton_.setEnabled(validationCtx.isValid());
    headerPanel_.setErrors(validationCtx);
  }

  @Override
  public JDialog getDialog() {
    return this;
  }

  @Override
  public void setPrefs(BulkPrefPersister prefs) {
    prefs_ = Optional.ofNullable(prefs);
  }

}
