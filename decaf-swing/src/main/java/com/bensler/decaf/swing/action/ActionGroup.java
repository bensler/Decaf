package com.bensler.decaf.swing.action;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.bensler.decaf.swing.EntityComponent;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

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
