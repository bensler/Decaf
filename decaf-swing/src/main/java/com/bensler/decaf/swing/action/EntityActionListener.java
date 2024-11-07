package com.bensler.decaf.swing.action;

import java.util.List;

/** The actual action itself performing some functionality on a list
 * of entities. */
@FunctionalInterface
public interface EntityActionListener<E> {

  public void doAction(List<E> entities);

}
