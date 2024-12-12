package com.bensler.decaf.swing.action;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import com.bensler.decaf.swing.EntityComponent;

/** A bundle of
 * <ul>
 *   <li>an {@link ActionAppearance}, representing this action to the user</li>
 *   <li>a filter, deciding if this action applicable to given entities or is even visible</li>
 *   <li>and the action itself performing an operation on the given entities.</li>
 * </ul>
 */
public class EntityAction<E> {

  private static <X> EntityActionFilter<X> allwaysOnFilter() {
    return (x -> ActionState.ENABLED);
  }

  private final ActionAppearance appearance_;
  private final EntityActionFilter<E> filter_;
  private final EntityActionListener<E> action_;

  /** @param filter <code>null</code> means always on */
  public EntityAction(
    ActionAppearance appearance, EntityActionFilter<E> filter, EntityActionListener<E> action
  ) {
    appearance_ = requireNonNull(appearance);
    filter_ = Optional.ofNullable(filter).orElseGet(EntityAction::allwaysOnFilter);
    action_ = requireNonNull(action);
  }

  public ActionState getActionState(List<E> entities) {
    return filter_.getActionState(entities);
  }

  public JMenuItem createPopupmenuItem(EntityComponent<E> comp, List<E> selection) {
    final JMenuItem menuitem = appearance_.createPopupmenuItem();

    menuitem.addActionListener(evt -> action_.doAction(comp, selection));
    return menuitem;
  }

  public JButton createToolbarButton() {
    final JButton button = appearance_.createToolbarButton();

    button.addActionListener(evt -> action_.doAction(null, List.of()));
    return button;
  }

}
