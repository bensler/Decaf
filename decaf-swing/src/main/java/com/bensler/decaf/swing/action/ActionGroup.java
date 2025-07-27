package com.bensler.decaf.swing.action;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JMenuItem;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.action.FocusedComponentActionController.ToolbarComponentCollector;
import com.bensler.decaf.util.Pair;

public class ActionGroup implements Action {

  public static final Set<ActionState> VISIBLE_STATES = Set.of(ActionState.ENABLED, ActionState.DISABLED);

  private final List<Action> actions_;

  public ActionGroup() {
    actions_ = List.of();
  }

  public ActionGroup(Action... actions) {
    actions_= Arrays.asList(actions);
  }

  public boolean isEmpty() {
    return actions_.isEmpty();
  }

  @Override
  public void createToolbarComponent(ToolbarComponentCollector collector, Supplier<EntityComponent<?>> sourceSupplier, Supplier<List<?>> entitiesSupplier) {
    collector.add(new Pair<>(null, this));
    actions_.forEach(action -> action.createToolbarComponent(collector, sourceSupplier, entitiesSupplier));
  }

  @Override
  public void computeState(List<?> entities, ActionStateMap target) {
    actions_.forEach(action -> action.computeState(entities, target));
    target.put(this, actions_.stream().map(target::getState).filter(VISIBLE_STATES::contains).findFirst().isPresent()
      ? ActionState.ENABLED : ActionState.HIDDEN
    );
  }

  @Override
  public void createPopupmenuItem(
    Consumer<JMenuItem> parentAdder, EntityComponent<?> comp, List<?> selection, ActionStateMap states
  ) {
    if (states.getState(this) != ActionState.HIDDEN) {
      actions_.stream().filter(action -> states.getState(action) != ActionState.HIDDEN)
      .forEach(action -> action.createPopupmenuItem(parentAdder, comp, selection, states));
    }
  }

  @Override
  public Optional<EntityAction<?>> isEntityAction() {
    return Optional.empty();
  }

}
