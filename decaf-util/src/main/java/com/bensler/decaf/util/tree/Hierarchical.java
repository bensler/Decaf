package com.bensler.decaf.util.tree;

import java.util.LinkedList;
import java.util.List;

/**
 * Implemented by classes being in a hierarchical order, which means they have a parent. There is no assumption made
 * about children. This is possible only in the context of a Hierarchy. So one Hierarchical might be a member of
 * different hierarchies possibly performing some filtering.
 */
public interface Hierarchical <P> {

    P getParent();

    public static <H extends Hierarchical<H>> List<H> toPath(H node) {
      final LinkedList<H> path = new LinkedList<>();

      do {
        path.addFirst(node);
      } while((node = node.getParent()) != null);
      return path;
    }

}
