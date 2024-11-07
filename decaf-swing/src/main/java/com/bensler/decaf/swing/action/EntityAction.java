package com.bensler.decaf.swing.action;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Function;

import javax.swing.AbstractAction;

/** A bundle of
 * <ul>
 *   <li>an {@link Appearance}, representing this action to the user</li>
 *   <li>a filter, deciding if this action applicable to given enities or is even visible</li>
 *   <li>and the action itself performing an operation on the given entities.</li>
 * </ul>
 */
public class EntityAction<E> extends AbstractAction {

  private final Appearance appearance_;
  private final Function<List<E>, ActionState> filter_;
  private final EntityActionListener<E> action_;

  public EntityAction(
    Appearance appearance,
    Function<List<E>, ActionState> filter,
    EntityActionListener<E> action
  ) {
    appearance_ = appearance;
    filter_ = filter;
    action_ = action;
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    // TODO Auto-generated method stub

  }


}
