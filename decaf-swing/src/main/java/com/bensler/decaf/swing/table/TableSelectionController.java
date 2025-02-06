package com.bensler.decaf.swing.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.bensler.decaf.swing.selection.EntitySelectionListener;

class TableSelectionController<E> implements ListSelectionListener {

  private final EntityTable<E> owner_;
  private final TableComponent<E> table_;
  private final Set<EntitySelectionListener<E>> selectionListeners_;
  private final List<E> selection_;
  private boolean ignoreSourceEvents_;

  TableSelectionController(EntityTable<E> owner) {
    selectionListeners_ = new HashSet<>();
    selection_ = new ArrayList<>();
    ignoreSourceEvents_ = false;
    (table_ = (owner_ = owner).getComponent()).getSelectionModel().addListSelectionListener(this);
  }

  @Override
  public void valueChanged(ListSelectionEvent evt) {
    if ((!ignoreSourceEvents_) && (!evt.getValueIsAdjusting())) {
      final List<E> newSelection = table_.getSelectedValues();

      if (!Set.copyOf(selection_).equals(Set.copyOf(newSelection))) {
        selection_.clear();
        selection_.addAll(newSelection);
        fireSelectionChanged();
      }
    }
  }

  private void fireSelectionChanged() {
    if (!silentSelectionChange_ && enabled_) {
      selectionListener_.selectionChanged(this, new ArrayList<>(selection_));
    }
  }

  public void addSelectionListener(EntitySelectionListener<E> listener) {
    selectionListeners_.add(listener);
  }

  public void removeSelectionListener(EntitySelectionListener<E> listener) {
    selectionListeners_.remove(listener);
  }

  private void fireEvent() {
    final List<E> selection = List.copyOf(selection_);

    selectionListeners_.forEach(listener -> listener.selectionChanged(owner_, selection));
  }

  void setSelectionSilent() {
    ignoreSourceEvents_ = true;
  }

  void setSelectionUnsilent(List<E> selectionBefore) {
    if (!new HashSet<>(selectionBefore).equals(new HashSet<>(selection_))) {
      table_.setSelectedValues(selectionBefore);
      fireSelectionChanged();
      table_.repaint();
    }
    ignoreSourceEvents_ = false;
  }

  public List<E> getSelection() {
    return  List.copyOf(selection_);
  }

}
