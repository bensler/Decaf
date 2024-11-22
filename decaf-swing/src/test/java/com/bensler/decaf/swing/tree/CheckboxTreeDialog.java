package com.bensler.decaf.swing.tree;

import static com.bensler.decaf.util.cmp.CollatorComparator.COLLATOR_COMPARATOR;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.bensler.decaf.swing.view.PropertyViewImpl;
import com.bensler.decaf.swing.view.SimplePropertyGetter;
import com.bensler.decaf.util.tree.Folder;
import com.bensler.decaf.util.tree.Hierarchy;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertYellow;

class CheckboxTreeDialog {

  public static void main(String[] args) throws UnsupportedLookAndFeelException {
    new CheckboxTreeDialog().dialog_.setVisible(true);
  }

  final JDialog dialog_;
  final CheckboxTree<Folder> cbTree_;
  final EntityTree<Folder> resultTree_;

  CheckboxTreeDialog() throws UnsupportedLookAndFeelException {
    final Hierarchy<Folder> data = createFolderData();

    Plastic3DLookAndFeel.setCurrentTheme(new DesertYellow());
    UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
    dialog_ = new JDialog(null, "Decaf Swing Test", ModalityType.MODELESS);
    final JPanel panel = new JPanel(new FormLayout(
      "3dlu, f:p:g, 3dlu",
      "3dlu, f:p:g, 3dlu, f:p:g, 3dlu"
    ));
    final PropertyViewImpl<Folder, String> nameView = new PropertyViewImpl<>(
      new SimplePropertyGetter<>(Folder::getName, COLLATOR_COMPARATOR)
    );

    cbTree_ = new CheckboxTree<>(nameView);
    resultTree_ = new EntityTree<>(nameView);
    cbTree_.addCheckedListener(checkedFolders -> {
      resultTree_.setData(new Hierarchy<>(checkedFolders));
      resultTree_.expandCollapseAll(true);
    });
    dialog_.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    cbTree_.setData(data);
    panel.add(cbTree_.getScrollPane(), new CellConstraints(2, 2));
    panel.add(resultTree_.getScrollPane(), new CellConstraints(2, 4));
    panel.setPreferredSize(new Dimension(500, 750));

    dialog_.setContentPane(panel);
    dialog_.pack();
    cbTree_.expandCollapseAll(true);
  }

  private Hierarchy<Folder> createFolderData() {
    final Folder a = new Folder(null, "a");
    final Folder aa = new Folder(a, "aa");
    final Folder ab = new Folder(a, "ab");
    final Folder aba = new Folder(ab, "aba");
    final Folder abb = new Folder(ab, "abb");
    final Folder abba = new Folder(abb, "abba");
    final Folder ac = new Folder(a, "ac");
    final Folder b = new Folder(null, "b");
    final Folder ba = new Folder(b, "ba");
    final Folder bb = new Folder(b, "bb");
    final Folder bc = new Folder(b, "bc");
    final Folder c = new Folder(null, "c");
    final Folder ca = new Folder(c, "ca");
    final Folder cb = new Folder(c, "cb");
    final Folder cbb = new Folder(cb, "cbb");
    final Folder cc = new Folder(c, "cc");

    return new Hierarchy<>(Arrays.asList(
      a, aa, ab, aba, abb, abba, ac, b, ba, bb, bc, c, ca, cb, cbb, cc
    ));
  }

}
