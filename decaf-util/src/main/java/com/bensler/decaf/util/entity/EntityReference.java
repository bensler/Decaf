package com.bensler.decaf.util.entity;

import java.util.Collection;
import java.util.Objects;

public class EntityReference<E extends Entity<E>> extends AbstractEntity<E> {

  public static <ENTITY extends Entity<ENTITY>, CIN extends Collection<ENTITY>, COUT extends Collection<EntityReference<ENTITY>>> COUT createCollection(
    CIN entities, COUT collector
  ) {
    entities.stream().forEach(entity -> collector.add(new EntityReference<>(entity)));
    return collector;
  }

  public EntityReference(E entity) {
    super(entity.getEntityClass(), Objects.requireNonNull(entity.getId()));
  }

  public EntityReference(Class<E> entityClass, Integer id) {
    super(entityClass, id);
  }

  @Override
  public String toString() {
    return "Ref[%s]".formatted(super.toString());
  }

}
