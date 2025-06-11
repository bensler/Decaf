package com.bensler.decaf.swing.action;

public interface EntityActionFilter<E> {

  ActionState getActionState(E entity);

}
