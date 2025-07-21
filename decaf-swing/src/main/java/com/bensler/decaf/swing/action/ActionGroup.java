package com.bensler.decaf.swing.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.util.Pair;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ActionGroup implements Action {

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

  public ContextActions createContextMenu(EntityComponent<?> comp) {
    return new ContextActions(actions_, comp);
  }

  @Override
  public JComponent createToolbarComponent(Supplier<EntityComponent<?>> sourceSupplier, Supplier<List<?>> entitiesSupplier) {
    final JPanel toolbar = new JPanel(new FormLayout(
      // "[f:p, 3dlu,f:p]*,0dlu:g"
      IntStream.range(0, actions_.size()).mapToObj(i -> "f:p").collect(Collectors.joining(",3dlu,", "", ",0dlu:g")),
      "f:p"
    ));
    IntStream.range(0, actions_.size()).forEach(i -> toolbar.add(
      actions_.get(i).createToolbarComponent(sourceSupplier, entitiesSupplier), new CellConstraints((2 * i) + 1, 1)
    ));
    return toolbar;
  }

  @Override
  public ActionState computeState(List<?> entities, Map<Action, ActionState> target) {
    final List<ActionState> subStates = actions_.stream()
      .map(action -> action.computeState(entities, target)).distinct().toList();

    return (subStates.contains(ActionState.ENABLED) || subStates.contains(ActionState.DISABLED))
      ? ActionState.ENABLED : ActionState.HIDDEN;
  }

  @Override
  public JComponent createPopupmenuItem(Consumer<JMenuItem> parentAdder, EntityComponent<?> comp, List<?> selection, Action primaryAction) {
    actions_.stream().forEach(action -> action.createPopupmenuItem(parentAdder, comp, selection, primaryAction));
    // TODO Auto-generated method stub
    return null;
  }

  public void triggerPrimaryAction(EntityComponent<?> focusedComp, List<?> selection) {
    final Map<Action, ActionState> states = new HashMap<>();

    actions_.stream().map(action -> new Pair<>(action, action.computeState(selection, states)))
    .filter(pair -> pair.getLeft().isEntityAction().isPresent() && (pair.getRight() == ActionState.ENABLED))
    .map(   pair -> pair.getLeft().isEntityAction().get())
    .findFirst().ifPresent(action -> action.doAction(focusedComp, selection));
  }

  @Override
  public Optional<EntityAction<?>> isEntityAction() {
    return Optional.empty();
  }

}
