package com.bensler.decaf.swing.action;

import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

import javax.swing.JPopupMenu;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.util.Pair;

public class ContextActions<E> {

  private final EntityComponent<E> comp_;

  private final List<E> selection_;

  private final List<Pair<EntityAction<E>, ActionState>> actions_;

  private final Optional<EntityAction<E>> primaryAction_;

  public ContextActions(List<EntityAction<E>> actions, EntityComponent<E> comp) {
    selection_ = (comp_ = comp).getSelection();
    actions_ = actions.stream()
      .map(action -> new Pair<>(action, action.getActionState(selection_)))
      .filter(pair -> (pair.getRight() != ActionState.HIDDEN))
      .toList();
    primaryAction_ = actions_.stream().filter(pair -> pair.getRight() == ActionState.ENABLED).map(Pair::getLeft).findFirst();
  }

  public void showPopupMenu(MouseEvent evt) {
    final Optional<JPopupMenu> popupMenu = actions_.isEmpty() ? Optional.empty() : Optional.of(addItems(new JPopupMenu()));

    popupMenu.ifPresent(popup -> popup.show(comp_.getComponent(), evt.getX(), evt.getY()));
  }

  private JPopupMenu addItems(JPopupMenu menu) {
    actions_.stream().forEach(pair -> {
      menu.add(pair.getRight().applyTo(pair.getLeft().createPopupmenuItem(
        comp_, selection_, primaryAction_.filter(entityAction -> entityAction.equals(pair.getLeft())).isPresent()
      )));
    });
    return menu;
  }

}
