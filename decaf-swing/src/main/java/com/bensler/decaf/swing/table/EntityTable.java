package com.bensler.decaf.swing.table;

import static java.util.Objects.requireNonNull;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.selection.EntitySelectionListener;
import com.bensler.decaf.swing.selection.SelectionMode;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


/**
 */
public class EntityTable<E> extends Object implements FocusListener, EntityComponent<E> {

  private final Class<E> entityClass_;
  private final Set<FocusListener> focusListeners_;

  protected final JScrollPane scrollPane_;

  protected final TableComponent<E> table_;

  private final TableModel<E> model_;

  private final TableView<E> view_;

  /** May be null! If not null it is shown when the table empty. */
  private Background emptyBackgroundComp_;

  public EntityTable(TableView<E> view, Class<E> anEntityClass) {
    entityClass_ = anEntityClass;
    view_ = view;
    focusListeners_ = new HashSet<>();
    table_ = new TableComponent<>(this, model_ = new TableModel<>(view_), view_);
    table_.addFocusListener(this);
    scrollPane_ = createScrollPane(table_);
    scrollPane_.getViewport().setBackground(table_.getBackground());
    setSelectionMode(SelectionMode.SINGLE);
  }

  @Override
  public Class<E> getEntityClass() {
    return entityClass_;
  }

  protected JScrollPane createScrollPane(TableComponent<E> tableComponent) {
    return new JScrollPane(tableComponent);
  }

  @Override
  public TableComponent<E> getComponent() {
    return table_;
  }

  @Override
  public JScrollPane getScrollPane() {
    return scrollPane_;
  }

  TableView<E> getView() {
    return view_;
  }

  public void clear() {
    removeData(getValues());
  }

  public void setSelectionMode(SelectionMode mode) {
    table_.selectionCtrl_.setSelectionMode(mode);
  }

  @Override
  public List<E> getSelection() {
    return table_.selectionCtrl_.getSelection();
  }

  public List<E> getValues() {
    return model_.getValues();
  }

  public void addOrUpdateData(Collection<E> entities) {
    try (var _ = table_.selectionCtrl_.new SelectionKeeper()) {
      model_.addOrUpdateData(entities);
    }
  }

  public void removeData(Collection<?> entities) {
    try (var _ = table_.selectionCtrl_.new SelectionKeeper()) {
      model_.removeData(entities);
    }
  }

  public void showGrid(boolean horizontal, boolean vertikal) {
    final Dimension spacing = table_.getIntercellSpacing();

    table_.setShowHorizontalLines(horizontal);
    table_.setShowVerticalLines(vertikal);
    table_.setIntercellSpacing(new Dimension(
      (vertikal ? spacing.width : 0),
      (horizontal ? spacing.height : 0)
    ));
  }

  public void sortByColumn(TablePropertyView<E, ?> column) {
    sortByColumn(column, Sorting.ASCENDING);
  }

  public void sortByColumn(TablePropertyView<E, ?> column, Sorting sorting) {
    table_.sortByColumn(column, sorting);
  }

  @Override
  public void addSelectionListener(EntitySelectionListener<E> listener) {
    table_.selectionCtrl_.addSelectionListener(listener);
  }

  public void fireSelectionChanged() {
    table_.selectionCtrl_.fireSelectionChanged();
  }

  @Override
  public void select(Object subject) {
    select(List.of(subject));
  }

  @Override
  public void select(Collection<?> subject) {
    throw new UnsupportedOperationException();    // TODO
//    table_.setSelectedValues(subject);
//    fireSelectionChanged();
//    scrollSelectionVisible();
  }

  /** Works only if there is one entry is selected */
  public void scrollSelectionVisible() {
    throw new UnsupportedOperationException();    // TODO
//    if (selection_.size() == 1) {
//      scrollIndexVisible(model_.indexOf(selection_.get(0)));
//    }
  }

  /** Works only if exactly one entry if selected. */
  protected void scrollIndexVisible(final int index) {
    table_.scrollRectToVisible(
      table_.getCellRect(index, 0, true)
    );
  }

  public void doubleclickTriggered(MouseEvent evt) {
    if (scrollPane_.getViewport().getView() == table_) {
      if (evt.getSource() == table_) {
//        final ActionImpl action = createContextActionGroup().getPrimaryAction();
//
//        if (action != null) {
//          action.actionPerformed(null);
//        }
      }
    }
  }

  public void setSelectionBackground(Color color) {
    table_.setSelectionBackground(color);
  }

  public void setBackground(Color color) {
  	table_.setBackground(color);
    // in case the tree is smaller than the scrollpane
    scrollPane_.getViewport().setBackground(color);
  }

  public void requestFocus() {
    table_.requestFocus();
  }

  public E getValue(Point point) {
    return model_.getValueAt(table_.rowAtPoint(point));
  }

  @Override
  public void clearSelection() {
    table_.clearSelection();
  }

  @Override
  public Optional<E> contains(Object entity) {
    return model_.contains(entity);
  }

  public boolean isEmpty() {
    return (model_.getRowCount() == 0);
  }

  /** Selects the first item of this table or does nothing
   * if it is empty.
   */
  public void selectFirstItem() {
    if (!isEmpty()) {
      select(model_.getValueAt(0));
    }
  }

  public int getIndexOf(Object object) {
    return model_.indexOf(object);
  }

  public int getRowCount() {
    return model_.getRowCount();
  }

  public void setEmptyBackgroundComp(JComponent emptyBackgroundComp) {
    emptyBackgroundComp_ = ((emptyBackgroundComp == null) ? null : new Background(emptyBackgroundComp));
    updateBackground();
  }

  private void updateBackground() {
    final JViewport vp      = scrollPane_.getViewport();
    final Component oldView = vp.getView();

    if (emptyBackgroundComp_ != null) {
      if ((model_.getRowCount() < 1) && (oldView != emptyBackgroundComp_)) {
        vp.setView(emptyBackgroundComp_);
        vp.setBackground(emptyBackgroundComp_.getBackground());
      }
    }
    if ((model_.getRowCount() > 0) && (oldView != table_)) {
      vp.setView(table_);
      vp.setBackground(table_.getBackground());
    }
  }

  private final class Background extends JPanel {

    private Background(JComponent comp) {
      super(new FormLayout("c:p:g", "c:p:g"));
      add(comp, new CellConstraints(1, 1));
    }

    @Override
    public Dimension getPreferredSize() {
      return super.getPreferredSize();
    }

  }

  @Override
  public void focusGained(FocusEvent e) {
    focusListeners_.forEach(l -> l.focusGained(this));
    table_.repaintSelection();
  }

  @Override
  public void focusLost(FocusEvent e) {
    table_.repaintSelection();
  }

  @Override
  public void addFocusListener(FocusListener listener) {
    focusListeners_.add(requireNonNull(listener));
  }

  public void removeFocusListener(FocusListener listener) {
    focusListeners_.remove(listener);
  }

}
