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

  public static <X> EntitiesActionFilter<X> allwaysOnFilter() {
    return (x -> ActionState.ENABLED);
  }

  public static <X> EntitiesActionFilter<X> atLeastOneFilter(ActionState zeroElementsState) {
    return (x -> ((x.size() < 1) ? zeroElementsState : ActionState.ENABLED));
  }

  private final ActionAppearance appearance_;
  private final EntitiesActionFilter<E> filter_;
  private final EntityActionListener<E> action_;

  /** @param filter <code>null</code> means always on */
  public EntityAction(
    ActionAppearance appearance, EntitiesActionFilter<E> filter, EntityActionListener<E> action
  ) {
    appearance_ = requireNonNull(appearance);
    filter_ = Optional.ofNullable(filter).orElseGet(EntityAction::allwaysOnFilter);
    action_ = requireNonNull(action);
  }

  public ActionState getActionState(List<E> entities) {
    return filter_.getActionState(entities);
  }

  public void doAction(EntityComponent<E> comp, List<E> selection) {
    action_.doAction(comp, selection);
  }

  public JMenuItem createPopupmenuItem(EntityComponent<E> comp, List<E> selection, boolean primary) {
    final JMenuItem menuitem = appearance_.createPopupmenuItem(primary);

    menuitem.addActionListener(evt -> action_.doAction(comp, selection));
    return menuitem;
  }

  public JButton createToolbarButton() {
    final JButton button = appearance_.createToolbarButton();

    button.addActionListener(evt -> action_.doAction(null, List.of()));
    return button;
  }

}
