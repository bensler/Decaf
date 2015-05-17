package com.bensler.decaf.testutil;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertYellow;

public class SimpleDialog implements ActionListener {

  public static void main(String[] args) throws UnsupportedLookAndFeelException {
    new SimpleDialog().dialog_.setVisible(true);
  }

  final JDialog dialog_;
  final JCheckBox checkBox_;
  final JButton button_;

  public SimpleDialog() throws UnsupportedLookAndFeelException {
    Plastic3DLookAndFeel.setCurrentTheme(new DesertYellow());
    UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
    dialog_ = new JDialog(null, "Decaf Swing Test", ModalityType.MODELESS);
    final JPanel panel = new JPanel(new FormLayout(
      "3dlu, l:p:g, 3dlu",
      "3dlu, t:p:g, 3dlu, p, 3dlu"
    ));
    button_ = new JButton("Close");
    checkBox_ = new JCheckBox("I accept the licence agreement");
    checkBox_.setFocusPainted(false);
    checkBox_.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        button_.setEnabled(checkBox_.isSelected());
      }
    });
    dialog_.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    panel.add(checkBox_, new CellConstraints(2, 2));
    button_.setEnabled(false);
    button_.addActionListener(this);
    panel.add(button_, new CellConstraints(2, 4, CellConstraints.RIGHT, CellConstraints.CENTER));

    dialog_.setContentPane(panel);
    dialog_.pack();
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    dialog_.setVisible(false);
  }

}
