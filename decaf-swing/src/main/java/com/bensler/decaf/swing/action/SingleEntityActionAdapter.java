package com.bensler.decaf.swing.action;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/** Adapter for {@link EntityActionListener} to make it viable for actions being applicable
 * for single entity object only. */
public class SingleEntityActionAdapter<E> implements EntityActionListener<E> {

  private final Consumer<Optional<E>> action_;

  public SingleEntityActionAdapter(Consumer<Optional<E>> action) {
    action_ = action;
  }

  @Override
  public void doAction(List<E> entities) {
    action_.accept(entities.isEmpty() ? Optional.empty() : Optional.of(entities.get(0)));
  }

}
