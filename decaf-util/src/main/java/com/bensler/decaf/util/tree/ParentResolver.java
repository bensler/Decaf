package com.bensler.decaf.util.tree;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ParentResolver <TARGET extends Hierarchical<TARGET>> {

  private final Map<Object, TARGET> cache_;

  private final Function<TARGET, TARGET> refProvider_;

  public ParentResolver(Function<TARGET, TARGET> refProvider) {
    cache_ = new HashMap<>();
    refProvider_ = refProvider;
  }

  Function<TARGET, TARGET> getParentRefProvider() {
    return refProvider_;
  }

  public TARGET resolveParent(TARGET child) {
    return cache_.get(child.getParent());
  }

  public boolean containsTarget(TARGET target) {
    checkNotNull(target);
    return cache_.containsKey(refProvider_.apply(target));
  }

  public void addTarget(TARGET target) {
    checkNotNull(target);
    cache_.put(refProvider_.apply(target), target);
  }

  public void removeTarget(TARGET target) {
    checkNotNull(target);
    cache_.remove(refProvider_.apply(target));
  }

  public boolean isEmpty() {
    return cache_.isEmpty();
  }

  public int size() {
    return cache_.size();
  }

}
