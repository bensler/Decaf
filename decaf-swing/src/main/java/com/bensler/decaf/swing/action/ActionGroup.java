package com.bensler.decaf.swing.action;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.JPopupMenu;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.util.Pair;

public class ActionGroup<E> {

  private final List<EntityAction<E>> actions_;

  public ActionGroup() {
    actions_ = List.of();
  }

  public ActionGroup(EntityAction<E>... actions) {
    actions_= Arrays.asList(actions);
  }

  public boolean isEmpty() {
    return actions_.isEmpty();
  }

  public Optional<JPopupMenu> createContextMenu(EntityComponent<E> comp) {
    final List<E> selection = comp.getSelection();

    final List<Pair<EntityAction<E>, ActionState>> actions = actions_.stream()
      .map(action -> new Pair<>(action, action.getActionState(selection)))
      .filter(pair -> (pair.getRight() != ActionState.HIDDEN))
      .toList();

    return actions.isEmpty() ? Optional.empty() : Optional.of(addItems(comp, new JPopupMenu(), selection, actions));
  }

  private JPopupMenu addItems(
    EntityComponent<E> sourceComp, JPopupMenu menu,
    List<E> selection,
    List<Pair<EntityAction<E>, ActionState>> actions
  ) {
    actions.stream().forEach(pair -> {
      menu.add(pair.getRight().applyTo(pair.getLeft().createPopupmenuItem(sourceComp, selection)));
    });
    return menu;
  }


}
