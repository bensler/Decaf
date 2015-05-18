package com.bensler.decaf.swing.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import com.bensler.decaf.swing.view.PropertyView;

/**
 *
 */
public class SortedListModel<E> extends AbstractListModel<E> implements ComboBoxModel<E> {

  private   final         PropertyView<? super E, ?> view_;

  private   final         List<E>         entities_;

  private                 Object          selectedItem_     = null;

  private                 boolean         sort_;

  public SortedListModel(PropertyView<? super E, ?> view) {
    super();
    view_ = view;
    entities_ = new ArrayList<E>();
    sort_ = true;
  }

  public SortedListModel(PropertyView<? super E, ?> view, Collection<? extends E> data) {
    this(view);
    set(data);
  }

  protected void add(E newObject, boolean fireEvent) {
    final int     oldIndex  = entities_.indexOf(newObject);
    final boolean replace   = entities_.remove(newObject);
    final int     newIndex;

    entities_.add(newObject);
    resort();
    newIndex = entities_.indexOf(newObject);
    if (fireEvent) {
      if (replace) {
        fireContentsChanged(this, Math.min(oldIndex, newIndex), Math.max(oldIndex, newIndex));
      } else {
        fireIntervalAdded(this, newIndex, newIndex);
      }
    }
  }

  public void add(E newObject) {
    add(newObject, true);
  }

  protected void set(Collection<? extends E> newObjects, boolean fireEvent) {
    final int oldSize = entities_.size();
    final int newSize;

    entities_.clear();
    entities_.addAll(newObjects);
    resort();
    newSize = entities_.size();
    if (fireEvent && ((oldSize > 0) || (newSize > 0))) {
      if (oldSize == newSize) {
        fireContentsChanged(this, 0, newSize);
      }
      if (oldSize < newSize) {
        fireIntervalAdded(this, oldSize, (newSize - 1));
        fireContentsChanged(this, 0, (oldSize - 1));
      } else {
        fireIntervalRemoved(this, newSize, (oldSize - 1));
        fireContentsChanged(this, 0, (newSize - 1));
      }
    }
  }

  public void set(Collection<? extends E> newObjects) {
    set(newObjects, true);
  }

  protected void retainAll(Collection<?> objectsToRetain, boolean fireEvent) {
    final int oldSize = entities_.size();
    final int newSize;

    entities_.retainAll(objectsToRetain);
    newSize = entities_.size();
    if (fireEvent && (oldSize > newSize)) {
      fireIntervalRemoved(this, newSize, (oldSize - 1));
      fireContentsChanged(this, 0, (newSize - 1));
    }
  }

  public void retainAll(Collection<?> objectsToRetain) {
    retainAll(objectsToRetain, true);
  }

  protected void addAll(Collection<? extends E> newObjects, boolean fireEvent) {
    final int oldSize = entities_.size();
    final int newSize;

    if (entities_.addAll(newObjects)) {
      resort();
      newSize = entities_.size();
      if (fireEvent) {
        fireIntervalAdded(this, oldSize, (newSize - 1));
        fireContentsChanged(this, 0, (oldSize - 1));
      }
    }
  }

  private void resort() {
    if (sort_) {
      if (view_ != null) {
        Collections.sort(entities_, view_);
      }
    }
  }

  public void addAll(Collection<? extends E> newObjects) {
    addAll(newObjects, true);
  }

  protected void removeAll(Collection<?> objectsToRemove, boolean fireEvent) {
    final int oldSize = entities_.size();

    if (entities_.removeAll(objectsToRemove)) {
      if (fireEvent) {
        fireIntervalRemoved(this, entities_.size(), (oldSize - 1));
        fireContentsChanged(this, 0, (entities_.size() - 1));
      }
    }
  }

  public void removeAll(Collection<?> objectsToRemove) {
    removeAll(objectsToRemove, true);
  }

  protected void remove(Object objectToRemove, boolean fireEvent) {
    final int oldIndex = entities_.indexOf(objectToRemove);

    if (entities_.remove(objectToRemove)) {
      if (fireEvent) {
        fireIntervalRemoved(this, oldIndex, oldIndex);
      }
    }
  }

  public void remove(Object objectToRemove) {
    remove(objectToRemove, true);
  }

  protected void clear(boolean fireEvent) {
    final int oldSize = entities_.size();

    entities_.clear();
    if (fireEvent && (oldSize > 0)) {
      fireIntervalRemoved(this, 0, (oldSize - 1));
    }
  }

  public void clear() {
    clear(true);
  }

  @Override
  public int getSize() {
    return entities_.size();
  }

  @Override
  public E getElementAt(int index) {
    return entities_.get(index);
  }

  public List<E> getData() {
    return new ArrayList<>(entities_);
  }

  public boolean contains(Object object) {
    return entities_.contains(object);
  }

  public int indexOf(Object object) {
    return entities_.indexOf(object);
  }

  @Override
  public void setSelectedItem(Object anItem) {
    selectedItem_ = anItem;
    fireContentsChanged(this, -1, -1);
  }

  @Override
  public Object getSelectedItem() {
    return selectedItem_;
  }

  public void setSorted(boolean sorted) {
    sort_ = sorted;
  }

}
