package com.bensler.decaf.util.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ChildrenCollectionMaintainer<H extends Hierarchical<?>, C extends Collection<H>> {

  C createCollection(Collection<H> collectionContents);

  C createEmptyCollection();

  void addChild(H child, Collection<H> target);

  class DefaultCollectionMaintainer<H extends Hierarchical<?>> implements ChildrenCollectionMaintainer<H, Set<H>> {

    @Override
    public Set<H> createCollection(Collection<H> collectionContents) {
      return new HashSet<>(collectionContents);
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

  abstract class AbstractListMaintainer<H extends Hierarchical<?>> implements ChildrenCollectionMaintainer<H, List<H>> {

    @Override
    public List<H> createCollection(Collection<H> collectionContents) {
      return new ArrayList<>(collectionContents);
    }

    @Override
    public List<H> createEmptyCollection() {
      return Collections.emptyList();
    }

  }

  class SortedListMaintainer<H extends Hierarchical<?>> extends AbstractListMaintainer<H> {

    private final Comparator<? super H> comparator_;

    public SortedListMaintainer(Comparator<? super H> comparator) {
      comparator_ = comparator;
    }

    @Override
    public List<H> createCollection(Collection<H> collectionContents) {
      final List<H> createdCollection = super.createCollection(collectionContents);

      Collections.sort(createdCollection, comparator_);
      return createdCollection;
    }

    @Override
    public void addChild(H child, Collection<H> target) {
      final List<H> list = (List<H>) target;

      list.add(child);
      Collections.sort(list, comparator_);
    }

  }

}
