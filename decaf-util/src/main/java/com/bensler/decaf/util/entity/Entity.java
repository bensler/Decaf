package com.bensler.decaf.util.entity;

public interface Entity<E extends Entity<E>> {

  public Integer getId();

  default boolean hasId() {
    return (getId() != null);
  }

  public Class<E> getEntityClass();

}
