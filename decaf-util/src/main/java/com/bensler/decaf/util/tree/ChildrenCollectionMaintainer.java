package com.bensler.decaf.util.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ChildrenCollectionMaintainer <H extends Hierarchical<?>, C extends Collection<H>> {

  C createCollection();

  C createEmptyCollection();

  void addChild(H child, Collection<H> target);

  class DefaultCollectionMaintainer <H extends Hierarchical<?>> implements ChildrenCollectionMaintainer<H, Set<H>> {

    @Override
    public Set<H> createCollection() {
      return new HashSet<H>(2);
    }

    @Override
    public Set<H> createEmptyCollection() {
      return Collections.emptySet();
    }

    @Override
    public void addChild(H child, Collection<H> target) {
      target.add(child);
    }

  }

  abstract class AbstractListMaintainer <H extends Hierarchical<?>> implements ChildrenCollectionMaintainer<H, List<H>> {

    @Override
    public List<H> createCollection() {
      return new ArrayList<H>(2);
    }

    @Override
    public List<H> createEmptyCollection() {
      return Collections.emptyList();
    }

  }

  class SimpleListMaintainer <H extends Hierarchical<?>> extends AbstractListMaintainer<H> {

    private final boolean append_;

    public SimpleListMaintainer(boolean append) {
      append_ = append;
    }

    @Override
    public void addChild(H child, Collection<H> target) {
      ((List<H>) target).add((append_ ? target.size() : 0), child);
    }

  }

  class SortedListMaintainer <H extends Hierarchical<?>> extends AbstractListMaintainer<H> {

    private final Comparator<H> comparator_;

    public SortedListMaintainer(Comparator<H> comparator) {
      comparator_ = comparator;
    }

    @Override
    public void addChild(H child, Collection<H> target) {
      final List<H> list = (List<H>) target;

      for (int i = 0; i < list.size(); i++) {
        if (comparator_.compare(child, list.get(i)) <= 0) {
          list.add(i, child);
          return;
        }
      }
      list.add(list.size(), child);
    }

  }

}
