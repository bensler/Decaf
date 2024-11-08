package com.bensler.decaf.swing.action;

import java.util.List;

import com.bensler.decaf.swing.EntityComponent;

/** The actual action itself performing some functionality on a list
 * of entities. */
@FunctionalInterface
public interface EntityActionListener<E> {

  public void doAction(EntityComponent<E> source, List<E> entities);

}
