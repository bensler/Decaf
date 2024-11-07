package com.bensler.decaf.swing.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AppearanceTest {

  @Test
  void atLeastOnConstructorArgNonNull() {
    Exception e = assertThrows(IllegalArgumentException.class, () -> new Appearance(null, null, null, null));

    assertEquals("At least one of the AppearanceConstructor parameters must be non-null" , e.getMessage());
  }

}
