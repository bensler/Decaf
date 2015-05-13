package com.bensler.decaf.util.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

  class ListCollectionMaintainer <H extends Hierarchical<?>> implements ChildrenCollectionMaintainer<H, List<H>> {

    @Override
    public List<H> createCollection() {
      return new ArrayList<H>(2);
    }

     @Override
    public List<H> createEmptyCollection() {
      return Collections.emptyList();
    }

    @Override
    public void addChild(H child, Collection<H> target) {
      List<H> list = (List<H>) target;
      list.add(child);
    }

  }

}
