package com.bensler.decaf.swing.action;

/** A bundle of
 * <ul>
 *   <li>an {@link Appearance}, representing this action to the user</li>
 *   <li>a filter, deciding if this action applicable to given entities or is even visible</li>
 *   <li>and the action itself performing an operation on the given entities.</li>
 * </ul>
 */
public class EntityAction<E> {

  private final Appearance appearance_;
  private final EntityActionFilter filter_;
  private final EntityActionListener<E> action_;

  public EntityAction(
    Appearance appearance, EntityActionFilter filter, EntityActionListener<E> action
  ) {
    appearance_ = appearance;
    filter_ = filter;
    action_ = action;
  }

}
