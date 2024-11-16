package com.bensler.decaf.swing.tree;

import java.util.Comparator;
import java.util.List;

import com.bensler.decaf.util.tree.AbstractHierarchy;
import com.bensler.decaf.util.tree.ChildrenCollectionMaintainer.SortedListMaintainer;
import com.bensler.decaf.util.tree.Hierarchical;

class ListHierarchy<M extends Hierarchical<M>> extends AbstractHierarchy<M, List<M>> {

  ListHierarchy(Comparator<? super M> comparator) {
    super(new SortedListMaintainer<>(comparator));
  }

  /** Widening visibility */
  @Override
  protected List<M> getChildrenNoCopy(Hierarchical<?> member) {
    return super.getChildrenNoCopy(member);
  }

}