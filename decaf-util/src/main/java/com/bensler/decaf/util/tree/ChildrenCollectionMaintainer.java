package com.bensler.decaf.util.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ChildrenCollectionMaintainer<H extends Hierarchical<?>, C extends Collection<H>> {

  C createCopy(Collection<H> collectionContents);

  C createEmptyCollection();

  C addChild(H child, C target);

  class DefaultCollectionMaintainer<H extends Hierarchical<?>> implements ChildrenCollectionMaintainer<H, Set<H>> {

    @Override
    public Set<H> createCopy(Collection<H> collectionContents) {
      return new HashSet<>(collectionContents);
    }

    @Override
    public Set<H> createEmptyCollection() {
      return Collections.emptySet();
    }

    @Override
    public Set<H> addChild(H child, Set<H> target) {
      if (target.isEmpty()) {
        target = new HashSet<>();
      }
      target.add(child);
      return target;
    }

  }

  abstract class AbstractListMaintainer<H extends Hierarchical<?>> implements ChildrenCollectionMaintainer<H, List<H>> {

    @Override
    public List<H> createCopy(Collection<H> collectionContents) {
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
    public List<H> createCopy(Collection<H> collectionContents) {
      final List<H> createdCollection = super.createCopy(collectionContents);

      Collections.sort(createdCollection, comparator_);
      return createdCollection;
    }

    @Override
    public List<H> addChild(H child, List<H> target) {
      if (target.isEmpty()) {
        target = new ArrayList<>(1);
      }
      target.add(child);
      Collections.sort(target, comparator_);
      return target;
    }

  }

}
