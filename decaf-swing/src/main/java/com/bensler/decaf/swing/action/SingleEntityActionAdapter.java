package com.bensler.decaf.swing.action;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.bensler.decaf.swing.EntityComponent;

/** The actual action itself performing some functionality on a single entity. */
public class SingleEntityActionAdapter<E> implements EntityActionListener<E> {

  private final BiConsumer<EntityComponent<E>, Optional<E>> action_;

  public SingleEntityActionAdapter(BiConsumer<EntityComponent<E>, Optional<E>> action) {
    action_ = action;
  }

  @Override
  public void doAction(EntityComponent<E> source, List<E> entities) {
    action_.accept(source, entities.isEmpty() ? Optional.empty() : Optional.of(entities.get(0)));
  }

}
