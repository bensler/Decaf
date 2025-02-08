package com.bensler.decaf.swing.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.bensler.decaf.swing.selection.EntitySelectionListener;
import com.bensler.decaf.swing.selection.SelectionMode;
import com.bensler.decaf.swing.view.NoSelectionModel;

class TableSelectionController<E> implements ListSelectionListener {

  private final EntityTable<E> owner_;
  private final TableComponent<E> table_;
  private final ListSelectionModel defSelModel_;
  private final Set<EntitySelectionListener<E>> selectionListeners_;
  private final List<E> selection_;
  private SelectionKeeper selectionKeeper_;

  TableSelectionController(EntityTable<E> owner, TableComponent<E> table) {
    selectionListeners_ = new HashSet<>();
    selection_ = new ArrayList<>();
    selectionKeeper_ = null;
    owner_ = owner;
    table_ = table;
    (defSelModel_ = table_.getSelectionModel()).addListSelectionListener(this);
  }

  @Override
  public void valueChanged(ListSelectionEvent evt) {
    if ((selectionKeeper_ == null) && (!evt.getValueIsAdjusting())) {
      final List<E> newSelection = table_.getSelectedValues();

      if (!Set.copyOf(selection_).equals(Set.copyOf(newSelection))) {
        selection_.clear();
        selection_.addAll(newSelection);
        fireSelectionChanged();
      }
    }
  }

  public void addSelectionListener(EntitySelectionListener<E> listener) {
    selectionListeners_.add(listener);
  }

  public void removeSelectionListener(EntitySelectionListener<E> listener) {
    selectionListeners_.remove(listener);
  }

  private void fireSelectionChanged() {
    final List<E> selection = List.copyOf(selection_);

    selectionListeners_.forEach(listener -> listener.selectionChanged(owner_, selection));
  }

  public List<E> getSelection() {
    return  List.copyOf(selection_);
  }

  public void setSelectionMode(SelectionMode mode) {
    final int oldModeInt = table_.getSelectionModel().getSelectionMode();
    final int newModeInt = mode.getTableConstant();

    if (oldModeInt != newModeInt) {
      if (oldModeInt == SelectionMode.NONE.getTableConstant()) {
        table_.setSelectionModel(defSelModel_);
      }
      if (mode == SelectionMode.NONE) {
        table_.setSelectionModel(NoSelectionModel.NOP_MODEL_TABLE_LIST);
      } else {
        table_.setSelectionModel(defSelModel_);
        table_.setSelectionMode(mode.getTableConstant());
      }
    }
  }

  class SelectionKeeper implements AutoCloseable {

    private final List<E> oldSelection_;

    SelectionKeeper() {
      if (selectionKeeper_ != null ) {
        throw new IllegalStateException("Do not nest SelectionKeeper objects!");
      } else {
        selectionKeeper_ = this;
      }
      oldSelection_ = List.copyOf(selection_);
    }

    @Override
    public void close() {
      table_.setSelectedValues(oldSelection_);
      if (!new HashSet<>(oldSelection_).equals(new HashSet<>(selection_))) {
        fireSelectionChanged();
      }
      selectionKeeper_ = null;
    }

  }
}
