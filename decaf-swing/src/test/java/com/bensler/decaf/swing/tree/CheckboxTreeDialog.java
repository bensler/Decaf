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
    final Folder a = new Folder(null, "a", 0);
    final Folder aa = new Folder(a, "aa", 0);
    final Folder ab = new Folder(a, "ab", 0);
    final Folder aba = new Folder(ab, "aba", 0);
    final Folder abb = new Folder(ab, "abb", 0);
    final Folder abba = new Folder(abb, "abba", 0);
    final Folder ac = new Folder(a, "ac", 0);
    final Folder b = new Folder(null, "b", 0);
    final Folder ba = new Folder(b, "ba", 0);
    final Folder bb = new Folder(b, "bb", 0);
    final Folder bc = new Folder(b, "bc", 0);
    final Folder c = new Folder(null, "c", 0);
    final Folder ca = new Folder(c, "ca", 0);
    final Folder cb = new Folder(c, "cb", 0);
    final Folder cbb = new Folder(cb, "cbb", 0);
    final Folder cc = new Folder(c, "cc", 0);

    return new Hierarchy<>(Arrays.asList(
      a, aa, ab, aba, abb, abba, ac, b, ba, bb, bc, c, ca, cb, cbb, cc
    ));
  }

}
