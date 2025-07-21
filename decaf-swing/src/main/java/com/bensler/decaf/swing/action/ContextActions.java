package com.bensler.decaf.swing.action;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.swing.JPopupMenu;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.util.Pair;

public class ContextActions {

  private final EntityComponent<?> comp_;

  private final List<?> selection_;

  private final List<Pair<Action, ActionState>> actions_;

  private final Optional<Action> primaryAction_;

  public ContextActions(List<Action> actions, EntityComponent<?> comp) {
    selection_ = (comp_ = comp).getSelection();
    actions_ = actions.stream()
      .map(action -> new Pair<>(action, action.computeState(selection_, new HashMap<>())))
      .filter(pair -> (pair.getRight() != ActionState.HIDDEN))
      .toList();
    primaryAction_ = actions_.stream().filter(pair -> pair.getRight() == ActionState.ENABLED).map(Pair::getLeft).findFirst();
  }

  public void triggerPrimaryAction() {
    primaryAction_.ifPresent(action -> action.isEntityAction().ifPresent(entityAction -> entityAction.doAction(comp_, selection_)));
  }

  public void showPopupMenu(MouseEvent evt) {
    if (!actions_.isEmpty()) {
      addItems(new JPopupMenu()).show(comp_.getComponent(), evt.getX(), evt.getY());
    }
  }

  private JPopupMenu addItems(JPopupMenu menu) {
    actions_.stream().forEach(pair -> {
      // TODO ActionState.apply
//      pair.getRight().applyTo(pair.getLeft().createPopupmenuItem(
//        menu::add, comp_, selection_, primaryAction_.orElse(null)
//      ));
    });
    return menu;
  }

}
