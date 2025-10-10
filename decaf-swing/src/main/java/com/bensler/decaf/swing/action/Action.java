package com.bensler.decaf.swing.action;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.action.FocusedComponentActionController.ToolbarComponentCollector;

public interface Action {

  void computeState(List<?> currentSelection, ActionStateMap target);

  void createToolbarComponent(FocusedComponentActionController ctrl, ToolbarComponentCollector collector);

  void createPopupmenuItem(
    MenuItemCollector collector, Supplier<EntityComponent<?>> comp, Supplier<List<?>> selection, ActionStateMap states
  );

  Optional<EntityAction<?>> isEntityAction();

}
