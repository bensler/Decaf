package com.bensler.decaf.swing.action;

import java.util.List;

import com.bensler.decaf.swing.EntityComponent;
import com.bensler.decaf.swing.EntityComponent.FocusListener;

public class FocusedComponentActionController {

  public FocusedComponentActionController(EntityComponent<?>... components) {
    components[0].addFocusListener(new FocusListener() {

      @Override
      public void focusGained(EntityComponent<?> component, List<?> selection) {
        // TODO Auto-generated method stub

      }
    });
    // TODO Auto-generated constructor stub
  }

}
