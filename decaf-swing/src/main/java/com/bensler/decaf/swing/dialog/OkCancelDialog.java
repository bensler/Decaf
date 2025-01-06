package com.bensler.decaf.swing.dialog;

import static java.awt.Dialog.ModalityType.TOOLKIT_MODAL;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.bensler.decaf.swing.awt.IconComponent;
import com.bensler.decaf.util.prefs.BulkPrefPersister;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class OkCancelDialog<IN, OUT> extends JDialog implements ContentPanel.Context {

  private final JButton okButton_;

  private final JButton cancelButton_;

  private final JPanel headerPanel_;

  private final ContentPanel<IN, OUT> contentPanel_;

  private OUT outData_;

  private Optional<BulkPrefPersister> prefs_;

  public OkCancelDialog(Component ownerComponent, ContentPanel<IN, OUT> contentPanel) {
    super((Window)SwingUtilities.getRoot(ownerComponent), contentPanel.getAppearance().getWindowTitle(), TOOLKIT_MODAL);

    final CellConstraints cc = new CellConstraints();
    final JPanel mainPanel = new JPanel(new FormLayout(
      "3dlu, f:p:g, 3dlu",
      "3dlu, f:p, 3dlu, f:p:g, 3dlu, f:p, 3dlu"
    ));

    prefs_ = Optional.empty();
    mainPanel.add((headerPanel_ = createHeaderPanel(contentPanel.getAppearance())), cc.xy(2, 2));
    mainPanel.add((contentPanel_ = contentPanel).getComponent(), cc.xy(2, 4));
    mainPanel.add(createButtonPanel(okButton_ = new JButton("Ok"), cancelButton_ = new JButton("Cancel")), cc.xy(2, 6));
    setContentPane(mainPanel);
    pack();
    centerOnParent();
    contentPanel_.setContext(this);
    rootPane.setDefaultButton(okButton_);
    rootPane.registerKeyboardAction(evt -> setVisible(false), KeyStroke.getKeyStroke(VK_ESCAPE, 0), WHEN_IN_FOCUSED_WINDOW);
    setMinimumSize(getPreferredSize());
  }

  private void centerOnParent() {
    final Rectangle dialogBounds = getBounds();
    final Rectangle parentBounds = getParent().getBounds();

    dialogBounds.x = (int)(parentBounds.getCenterX() - (dialogBounds.width / 2.0));
    dialogBounds.y = (int)(parentBounds.getCenterY() - (dialogBounds.height / 2.0));
    setBounds(dialogBounds);
  }

  private JPanel createHeaderPanel(DialogAppearance appearance) {
    final JLabel titleLabel = new JLabel(appearance.getTitle());
    final Font titleFont = titleLabel.getFont();
    final CellConstraints cc = new CellConstraints();
    final JPanel headerPanel = new JPanel(new FormLayout(
      "3dlu, f:p:g, 3dlu, f:p, 3dlu",
      "3dlu, f:p, 3dlu"
    ));

    titleLabel.setFont(titleFont.deriveFont(titleFont.getSize() * 1.3f).deriveFont(Font.BOLD));
    headerPanel.add(titleLabel, cc.xy(2, 2, "l, c"));
    headerPanel.add(new IconComponent(appearance.getIcon()), cc.xy(4, 2));
    headerPanel.setBackground(Color.WHITE);
    return headerPanel;
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
      outData_ = contentPanel_.getData();
      setVisible(false);
    });
    cancelButton.addActionListener(evt -> setVisible(false));
    buttonPanel.add(okButton, cc.xy(5,  1));
    return buttonPanel;
  }

  public Optional<OUT> show(IN input) {
    show(input, none -> {});
    return Optional.ofNullable(outData_);
  }

  public void show(IN input, Consumer<OUT> action) {
    setValid(false);
    outData_ = null;
    contentPanel_.setInData(input);
    setVisible(true);
    prefs_.ifPresent(BulkPrefPersister::store);
    Optional.ofNullable(outData_).ifPresent(action);
    dispose();
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
  public void setValid(boolean valid) {
    okButton_.setEnabled(valid);
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
