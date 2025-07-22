package com.bensler.decaf.swing.action;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import com.bensler.decaf.swing.EntityComponent;

public interface Action {

  void computeState(List<?> currentSelection, ActionStateMap target);

  JComponent createToolbarComponent(Supplier<EntityComponent<?>> sourceSupplier, Supplier<List<?>> entitiesSupplier);

  void createPopupmenuItem(
    Consumer<JMenuItem> parentAdder, EntityComponent<?> comp, List<?> selection, ActionStateMap states
  );

  Optional<EntityAction<?>> isEntityAction();

}
