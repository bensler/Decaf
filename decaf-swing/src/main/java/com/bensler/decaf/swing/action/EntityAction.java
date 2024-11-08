package com.bensler.decaf.swing.action;

import java.util.List;

/** A bundle of
 * <ul>
 *   <li>an {@link Appearance}, representing this action to the user</li>
 *   <li>a filter, deciding if this action applicable to given entities or is even visible</li>
 *   <li>and the action itself performing an operation on the given entities.</li>
 * </ul>
 */
public class EntityAction<E> {

  private final Appearance appearance_;
  private final EntityActionFilter<E> filter_;
//  private final EntityActionListener<E> action_;

  public EntityAction(
    Appearance appearance, EntityActionFilter<E> filter //, EntityActionListener<E> action
  ) {
    appearance_ = appearance;
    filter_ = filter;
//    action_ = action;
  }

  public ActionState getActionState(List<E> entities) {
    return filter_.getActionState(entities);
  }

}
