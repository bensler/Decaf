package com.bensler.decaf.swing.tree;

import static com.bensler.decaf.swing.view.SimplePropertyGetter.chain;
import static com.bensler.decaf.swing.view.SimplePropertyGetter.createComparableGetter;
import static com.bensler.decaf.util.cmp.CollatorComparator.COLLATOR_COMPARATOR;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.bensler.decaf.swing.list.EntityList;
import com.bensler.decaf.swing.selection.SelectionMode;
import com.bensler.decaf.swing.table.EntityTable;
import com.bensler.decaf.swing.table.TablePropertyView;
import com.bensler.decaf.swing.table.TableView;
import com.bensler.decaf.swing.view.PropertyViewImpl;
import com.bensler.decaf.swing.view.SimplePropertyGetter;
import com.bensler.decaf.util.tree.Folder;
import com.bensler.decaf.util.tree.Hierarchy;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertYellow;

class SampleTreeDialog {

  public static void main(String[] args) throws UnsupportedLookAndFeelException {
    new SampleTreeDialog().dialog_.setVisible(true);
  }

  final JDialog dialog_;
  final EntityTree<Folder> tree_;
  final EntityList<Folder> list_;
  final EntityTable<Folder> table_;
  final JButton button_;

   SampleTreeDialog() throws UnsupportedLookAndFeelException {
    final Hierarchy<Folder> data = createFolderData();

    Plastic3DLookAndFeel.setCurrentTheme(new DesertYellow());
    UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
    dialog_ = new JDialog(null, "Decaf Swing Test", ModalityType.MODELESS);
    final JPanel panel = new JPanel(new FormLayout(
      "3dlu, f:p:g, 3dlu",
      "3dlu, f:p:g, 3dlu, f:p:g, 3dlu, f:p:g, 3dlu, p, 3dlu"
    ));
    final PropertyViewImpl<Folder, String> nameView = new PropertyViewImpl<>(
      new SimplePropertyGetter<>(Folder::getName, COLLATOR_COMPARATOR)
    );
    final PropertyViewImpl<Folder, String> parentNameView = new PropertyViewImpl<>(
      new SimplePropertyGetter<>(chain(Folder::getParent, Folder::getName), COLLATOR_COMPARATOR)
    );

    final PropertyViewImpl<Folder, Integer> sizeView = new PropertyViewImpl<>(
      createComparableGetter(this::getFolderSize)
    );

    tree_ = new EntityTree<>(nameView);
    tree_.setVisibleRowCount(15, 1);
    list_ = new EntityList<>(nameView);
    table_ = new EntityTable<>(new TableView<>(
      new TablePropertyView<>("parentName", "Parent", parentNameView),
      new TablePropertyView<>("name", "Name", nameView),
      new TablePropertyView<>("size", "Size", sizeView)
    ));
    table_.setSelectionMode(SelectionMode.MULTIPLE_INTERVAL);
    dialog_.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    tree_.setData(data);
    list_.setData(data.getMembers());
    table_.addOrUpdateData(data.getMembers());
    panel.add(tree_.getScrollPane(), new CellConstraints(2, 2));
    panel.add(list_.getScrollPane(), new CellConstraints(2, 4));
    panel.add(table_.getScrollPane(), new CellConstraints(2, 6));
    button_ = new JButton("Close");
    button_.addActionListener(evt -> dialog_.setVisible(false));
    panel.add(button_, new CellConstraints(2, 8, CellConstraints.RIGHT, CellConstraints.CENTER));
    panel.setPreferredSize(new Dimension(500, 750));

    dialog_.setContentPane(panel);
    dialog_.pack();
    tree_.expandCollapseAll(true);
  }

  Integer getFolderSize(Folder folder) {
    return folder.getSize();
  }

  private Hierarchy<Folder> createFolderData() {
    final Hierarchy<Folder> tree = new Hierarchy<>();

    final Folder root = new Folder(null, "/", 0);
    final Folder home = new Folder(root, "home", 10);
    final Folder bobsHome = new Folder(home, "bob", 1000);
    final Folder alicesHome = new Folder(home, "alice", 2000);
    final Folder winRoot = new Folder(null, "C:", 20);
    final Folder winHome = new Folder(winRoot, "Eigene Dateien", 200);
    final Folder winBobsHome = new Folder(winHome, "bob", 3000);
    final Folder winAlicesHHome = new Folder(winHome, "alice", 4000);

    tree.addAll(List.of(alicesHome, bobsHome, home, root, winRoot, winHome, winBobsHome, winAlicesHHome));
    return tree;
  }

}
