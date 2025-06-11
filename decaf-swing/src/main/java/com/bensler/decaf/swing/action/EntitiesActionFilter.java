package com.bensler.decaf.swing.action;

import java.util.List;

@FunctionalInterface
public interface EntitiesActionFilter<E> {

  ActionState getActionState(List<E> entities);

}
