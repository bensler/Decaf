package com.bensler.decaf.swing.list;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.bensler.decaf.swing.view.EntityComponent;
import com.bensler.decaf.swing.view.EntitySelectionListener;
import com.bensler.decaf.swing.view.NoSelectionModel;
import com.bensler.decaf.swing.view.PropertyView;
import com.bensler.decaf.swing.view.SelectionMode;
import com.google.common.collect.Lists;


/**
 * A list that contains bos. Was created as a copy of BoTable with small modifications...
 *
 */
public class EntityList<E> extends Object implements ListSelectionListener, EntityComponent<E> {

  private   final         JScrollPane         scrollPane_;

  protected final         JList<E>            list_;

  private   final         SortedListModel<E>  model_;

  private   final         PropertyView<? super E, ?>  view_;

  private                 EntitySelectionListener<? super E> selectionListener_;

  private   final         List<E>             selection_;

  private   final         List<E>             savedSelection_;

  private                 int                 visibleRowCount_;

  private                 boolean             shrinkIfPossible_;

  private                 boolean             silentSelectionChange_;

  private                 SelectionMode       selectionMode_;

  private                 DefaultListSelectionModel defSelModel_;

  private                 boolean             enabled_;

  private                 Color               enabledBgColor_;

  public EntityList(PropertyView<? super E, ?> view) {
    super();

    view_ = view;
    visibleRowCount_ = 10;
    shrinkIfPossible_ = true;
    selection_ = new ArrayList<E>(2);
    savedSelection_ = new ArrayList<E>(2);
    model_ = new SortedListModel<E>(view);
    list_ = new JList<E>(model_);
    list_.setCellRenderer(view_);
    scrollPane_ = new JScrollPane(list_);
    setVisibleRowCount(4, false);
    setSelectionListener(null);
    silentSelectionChange_ = false;
    setVisibleRowCount();
    scrollPane_.getViewport().setBackground(list_.getBackground());
    setSelectionMode(SelectionMode.SINGLE);
    list_.getSelectionModel().addListSelectionListener(this);
    enabled_ = true;
  }

  /** @return the JList component wrapped by a JScrollpane
   *  @see com.bensler.flob.gui.EntityComponent#getComponent()
   */
  @Override
  public JComponent getComponent() {
    return list_;
  }

  @Override
  public JScrollPane getScrollPane() {
    return scrollPane_;
  }

  public void clear() {
    setData(Collections.emptyList());
  }

  public void setData(Collection<? extends E> newData) {
    final List<E>    oldSelection  = getSelection();

    model_.set(newData);
    if (shrinkIfPossible_) {
      setVisibleRowCount();
    }
    select(oldSelection);
    if (scrollPane_.getParent() != null) {
      ((JComponent)scrollPane_.getParent()).revalidate();
    }
  }

  public SelectionMode getSelectionMode() {
    return selectionMode_;
  }

  public void setSelectionMode(SelectionMode mode) {
    if (selectionMode_ != mode) {
      if (selectionMode_ == SelectionMode.NONE) {
        list_.setSelectionModel(defSelModel_);
      	list_.setSelectionMode(mode.getListConstant());
      }
      if (mode == SelectionMode.NONE) {
        defSelModel_ = (DefaultListSelectionModel)list_.getSelectionModel();
        list_.setSelectionModel(NoSelectionModel.createTableListModel());
      } else {
        list_.setSelectionMode(mode.getTableConstant());
      }
      selectionMode_ = mode;
    }
  }

  @Override
  public List<E> getSelection() {
    return list_.getSelectedValuesList();
  }

  @Override
  public E getSingleSelection() {
    final List<E> selection = getSelection();

    return ((selection.isEmpty() ? null : selection.get(0)));
  }

  public List<E> getValues() {
    return model_.getData();
  }

  private void saveSelection() {
    savedSelection_.clear();
    savedSelection_.addAll(selection_);
  }

  private void applySavedSelection() {
    select(savedSelection_);
  }

  public void updateData(E subject) {
    saveSelection();
    model_.remove(subject);
    model_.add(subject);
    applySavedSelection();
  }

  public void addData(E subject) {
    saveSelection();
    model_.add(subject);
    applySavedSelection();
  }

  public void removeData(E subject) {
    saveSelection();
    model_.remove(subject);
    applySavedSelection();
  }

  public void removeData(Collection<?> bos) {
    saveSelection();
    model_.removeAll(bos);
    applySavedSelection();
  }

  @Override
  public void setSelectionListener(EntitySelectionListener<? super E> listener) {
    selectionListener_ = ((listener != null) ? listener : EntitySelectionListener.NOP);
  }

  @Override
  public void valueChanged(ListSelectionEvent evt) {
    if (!evt.getValueIsAdjusting()) {
      selection_.clear();
      selection_.addAll(getSelection());
      fireSelectionChanged();
    }
  }

  @Override
  public void select(Object subject) {
    select(Lists.newArrayList(subject));
  }

  @Override
  public void select(Collection<?> subject) {
    silentSelectionChange_ = true;

    try {
      list_.clearSelection();
      for (Object entity : subject) {
        final int     index   = model_.indexOf(entity);

        list_.addSelectionInterval(index, index);
      }
    } finally {
      silentSelectionChange_ = false;
      fireSelectionChanged();
      scrollSelectionVisible();
    }
  }

  /** Works only if there is one entry is selected */
  public void scrollSelectionVisible() {
    if (selection_.size() == 1) {
      scrollIndexVisible(model_.indexOf(selection_.get(0)));
    }
  }

  /** Works only if exactly one entry if selected. */
  protected void scrollIndexVisible(final int index) {
    list_.scrollRectToVisible(
      list_.getCellBounds(index, index)
    );
  }

  private void fireSelectionChanged() {
    if (!silentSelectionChange_) {
      selectionListener_.selectionChanged(this, new ArrayList<E>(selection_));
    }
  }

  private void modifySelection(Point point) {
    final int index         = list_.locationToIndex(point);

    if (index > -1) {
      int[] selIndices = list_.getSelectedIndices();
      boolean indexIsSelected = false;
      for (int i=0; i<selIndices.length; i++) {
        if (selIndices[i]==index) {
          indexIsSelected = true;
          break;
        }
      }
      if (!indexIsSelected) {
        list_.setSelectionInterval(index, index);
      }
    } else {
      clearSelection();
    }
  }


  public void popupTriggered(MouseEvent evt) {
    modifySelection(evt.getPoint());
  }

  public void setVisibleRowCount(int count, boolean shrinkIfPossible) {
    visibleRowCount_ = count;
    shrinkIfPossible_ = shrinkIfPossible;
    setVisibleRowCount();
  }

  private void setVisibleRowCount() {
    int count = visibleRowCount_;

    if (shrinkIfPossible_) {
      count = Math.max(1, Math.min(
        model_.getSize(), visibleRowCount_
      ));
    }
    list_.setVisibleRowCount(count);
  }

  public void setSelectionBackground(Color color) {
    list_.setSelectionBackground(color);
  }

  /**
   * sets the background of this Bo-List
   * @param c
   */
  @Override
  public void setBackground(Color c) {
  	// set it for the listComponen
  	list_.setBackground(c);
  }

  public void requestFocus() {
    list_.requestFocus();
  }

  public void setEnabled(boolean enabled) {
    if (enabled_ ^ enabled) {
      if (enabled) {
        scrollPane_.getViewport().setBackground(enabledBgColor_);
        list_.setBackground(enabledBgColor_);
        setSelectionMode(selectionMode_);
      } else {
        enabledBgColor_ = list_.getBackground();
        scrollPane_.getViewport().setBackground(null);
        scrollPane_.setBackground(null);
        list_.setBackground(null);
        setSelectionMode(SelectionMode.NONE);
      }
      enabled_ = enabled;
    }
  }

  @Override
  public boolean isEnabled() {
    return enabled_;
  }

  @Override
  public void clearSelection() {
    list_.clearSelection();
  }

  @Override
  public boolean contains(Object entity) {
    return model_.contains(entity);
  }

  public boolean isEmpty() {
    return model_.getSize()==0;
  }

  /** Selects the first item of this combo or does nothing
   * if combo is empty.
   */
  public void selectFirstItem() {
    if (!isEmpty()) {
      list_.setSelectedIndex(0);
    }
  }

  @Override
  public void setToolTipText(String hint) {
    list_.setToolTipText(hint);
  }

}
