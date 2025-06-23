package com.bensler.decaf.swing.action;

import java.util.Arrays;

import com.bensler.decaf.swing.EntityComponent;

public class FocusedComponentActionController {

  public FocusedComponentActionController(EntityComponent<?>... components) {
    Arrays.stream(components).forEach(comp -> comp.addFocusListener(
      (fComp, entities) -> System.out.println("%s(%s)".formatted(fComp.getEntityClass().getSimpleName(), entities.size()))
    ));
  }

}
