package com.bensler.decaf.util.entity;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class EntityReference<E extends Entity<E>> extends AbstractEntity<E> {

  public static <ENTITY extends Entity<ENTITY>, CIN extends Collection<ENTITY>, COUT extends Collection<EntityReference<ENTITY>>> COUT createCollection(
    CIN entities, COUT collector
  ) {
    entities.stream().forEach(entity -> collector.add(new EntityReference<>(entity)));
    return collector;
  }

  public static <E extends Entity<E>> Optional<E> resolve(Object sample, Collection<E> source) {
    return source.stream().filter(blob -> (blob.hashCode() == sample.hashCode()) && blob.equals(sample)).findFirst();
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
