package com.bensler.decaf.swing.action;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.action.FocusedComponentActionController.ToolbarComponentCollector;

public interface Action {

  void computeState(List<?> currentSelection, ActionStateMap target);

  void createToolbarComponent(ToolbarComponentCollector collector, Supplier<EntityComponent<?>> sourceSupplier, Supplier<List<?>> entitiesSupplier);

  void createPopupmenuItem(
    MenuItemCollector collector, EntityComponent<?> comp, List<?> selection, ActionStateMap states
  );

  Optional<EntityAction<?>> isEntityAction();

}
