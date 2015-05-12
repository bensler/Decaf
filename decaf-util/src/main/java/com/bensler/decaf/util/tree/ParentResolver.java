package com.bensler.decaf.util.tree;

public interface ParentResolver <P, R, C extends Hierarchical<R>> {

  P resolveParent(R ref);

}
