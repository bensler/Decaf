package com.bensler.decaf.swing.action;

import java.util.List;
import java.util.function.Supplier;

import javax.swing.JComponent;

import com.bensler.decaf.swing.EntityComponent;

public interface Action {

  ActionState computeState(List<?> currentSelection);

  JComponent createToolbarComponent(Supplier<EntityComponent<?>> sourceSupplier, Supplier<List<?>> entitiesSupplier);

  JComponent createPopupmenuItem(EntityComponent<?> comp, List<?> selection, boolean present);

  void doAction(EntityComponent<?> comp, List<?> selection);

}
