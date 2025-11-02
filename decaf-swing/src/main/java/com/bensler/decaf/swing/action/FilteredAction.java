package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;

public class FilteredAction<E> {

  @FunctionalInterface
  public interface FilterMany<E> {

    boolean match(List<E> entities);

  }

  /** The actual action itself performing some functionality on a list
   * of entities. */
  @FunctionalInterface
  public interface ActionMany<E> {

    public void doAction(List<E> entities);

  }

  @FunctionalInterface
  public interface FilterOne<E> {

    boolean matches(E entity);

  }

  @FunctionalInterface
  public interface ActionOne<E> {

    public void doAction(List<E> entities);

  }

  public static <X> FilterMany<X> allwaysOnFilter() {
    return (x -> true);
  }

  public static <X> FilterMany<X> atLeastOneFilter() {
    return (x -> (x.size() >= 1));
  }

//  static <E> FilteredAction<E> many(Class<E> entityClass, FilterOne<E> filter, ActionMany<E> action) {
//    return new FilteredAction<>(entityClass, entities -> entities.stream().map(filter::getActionState), action);
//  }

  static <E> FilteredAction<E> many(Class<E> entityClass, FilterMany<E> filter, ActionMany<E> action) {
    return new FilteredAction<>(entityClass, filter, action);
  }

  private final Class<E> entityClass_;
  private final FilterMany<E> filter_;
  private final ActionMany<E> action_;

  public FilteredAction(Class<E> entityClass, FilterMany<E> filter, ActionMany<E> action) {
    entityClass_ = requireNonNull(entityClass);
    filter_ = requireNonNull(filter);
    action_ = requireNonNull(action);
  }

  public void doAction(List<?> selection) {
    action_.doAction(filterTypeFittingEntities(selection));
  }

  public List<E> filterTypeFittingEntities(Collection<?> source) {
    return source.stream()
    .filter(entity -> entityClass_.isAssignableFrom(entity.getClass()))
    .map(entity -> entityClass_.cast(entity))
    .toList();
  }

  public boolean matches(List<?> entities){
    return filter_.match(filterTypeFittingEntities(entities));
  }

}
