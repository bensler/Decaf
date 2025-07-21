package com.bensler.decaf.swing.action;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import com.bensler.decaf.swing.EntityComponent;

public interface Action {

  ActionState computeState(List<?> currentSelection, Map<Action, ActionState> target);

  JComponent createToolbarComponent(Supplier<EntityComponent<?>> sourceSupplier, Supplier<List<?>> entitiesSupplier);

  JComponent createPopupmenuItem(Consumer<JMenuItem> parentAdder, EntityComponent<?> comp, List<?> selection, Action primaryAction);

  Optional<EntityAction<?>> isEntityAction();

}
