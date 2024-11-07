package com.bensler.decaf.swing.action;

import java.util.List;

@FunctionalInterface
public interface EntityActionFilter<E> {

  ActionState getActionState(List<E> entities);

}
