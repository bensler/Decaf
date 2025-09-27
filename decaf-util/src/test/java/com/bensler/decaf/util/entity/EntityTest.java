package com.bensler.decaf.util.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

import org.junit.jupiter.api.Test;

class EntityTest {

  static class SomethingA extends AbstractEntity<SomethingA> {

    SomethingA(Integer id) {
      super(SomethingA.class, id);
    }

  }

  static class SomethingB extends AbstractEntity<SomethingB> {

    SomethingB(Integer id) {
      super(SomethingB.class, id);
    }

  }

  @Test
  void entityEquality() {
    final SomethingA somethingA1 = new SomethingA(1);
    final SomethingA somethingA2 = new SomethingA(2);
    final SomethingB somethingB1 = new SomethingB(1);

    assertFalse(Objects.equals(somethingA1, somethingA2));
    assertFalse(Objects.equals(somethingA2, somethingA1));
    assertTrue(Objects.equals(somethingA1, somethingA1));
    assertTrue(Objects.equals(somethingA1, new SomethingA(1)));

    assertFalse(Objects.equals(somethingA1, somethingB1));
    assertFalse(Objects.equals(somethingA2, somethingA1));
  }

  @Test
  void entityReferenceEquality() {
    final SomethingA somethingA1 = new SomethingA(1);
    final EntityReference<SomethingA> refA1 = new EntityReference<>(somethingA1);
    final SomethingA somethingA2 = new SomethingA(2);
    final SomethingB somethingB1 = new SomethingB(1);
    final EntityReference<SomethingB> refB1 = new EntityReference<>(SomethingB.class, somethingB1.getId());

    assertTrue(Objects.equals(somethingA1, refA1));
    assertFalse(Objects.equals(somethingA2, refA1));

    assertTrue(Objects.equals(somethingB1, refB1));
    assertFalse(Objects.equals(refA1, refB1));
  }

}
