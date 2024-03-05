package com.bensler.decaf.util.tree;

import java.util.function.Function;

public class ParentResolver <TARGET extends Hierarchical<TARGET>> {

  private final Function<TARGET, TARGET> refProvider_;

  public ParentResolver(Function<TARGET, TARGET> refProvider) {
    refProvider_ = refProvider;
  }

  Function<TARGET, TARGET> getParentRefProvider() {
    return refProvider_;
  }

}
