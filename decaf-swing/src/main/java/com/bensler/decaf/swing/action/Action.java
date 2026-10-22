package com.bensler.decaf.swing.action;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.bensler.decaf.swing.action.FocusedComponentActionController.Selection;
import com.bensler.decaf.swing.action.FocusedComponentActionController.ToolbarComponentCollector;

public interface Action {

  void computeState(Selection currentSelection, ActionStateMap target);

  void createToolbarComponent(FocusedComponentActionController ctrl, ToolbarComponentCollector collector);

  MenuItemCollector createPopupmenuItem(MenuItemCollector collector, Supplier<List<?>> selection, ActionStateMap states);

  Optional<UiAction> isEntityAction();

}
