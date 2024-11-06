package com.bensler.decaf.swing.action;

import java.util.List;

@FunctionalInterface
public interface EntityActionListener<E> {

  public void doAction(List<E> entities);

}
