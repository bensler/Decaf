package com.bensler.decaf.util.cmp;

import static com.bensler.decaf.util.cmp.CollatorComparator.COLLATOR_COMPARATOR;
import static com.bensler.decaf.util.cmp.NopComparator.NOP_COMPARATOR;
import static com.bensler.decaf.util.cmp.NullSafeComparator.NullPolicy.NULLS_FIRST;
import static com.bensler.decaf.util.cmp.NullSafeComparator.NullPolicy.NULLS_LAST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

class ComparatorTest {

  @Test
  void nop() {
    final NopComparator cmp = NOP_COMPARATOR;

    assertEquals(0, cmp.compare("a", null));
    assertEquals(0, cmp.compare(null, "b"));
    assertEquals(0, cmp.compare(null, null));
    assertEquals(0, cmp.compare("a", "b"));
    assertEquals(0, cmp.compare(2, new Object()));
    assertEquals(0, cmp.compare(2f, "c"));
  }

  @Test
  void nullsFirst() {
    final List<String> shuffled = listOf("x", null, "a", null, null, "z", "b");
    Comparator<String> uut = new NullSafeComparator<>(NULLS_FIRST, COLLATOR_COMPARATOR);
    final List<String> sorted;

    assertEquals(-1, uut.compare(null, "a"));
    assertEquals( 1, uut.compare("a", null));
    assertEquals( 0, uut.compare(null, null));
    assertEquals( 0, uut.compare("a", "a"));
    assertTrue(uut.compare("a", "b") < 0);
    Collections.sort(sorted = new ArrayList<>(shuffled), uut);
    assertEquals(listOf(null, null, null, "a", "b", "x", "z"), sorted);
  }

  @Test
  void nullsLast() {
    final List<String> shuffled = listOf("x", null, "a", null, null, "z", "b");
    Comparator<String> uut = new NullSafeComparator<>(NULLS_LAST, COLLATOR_COMPARATOR);
    final List<String> sorted;

    assertEquals( 1, uut.compare(null, "a"));
    assertEquals(-1, uut.compare("a", null));
    assertEquals( 0, uut.compare(null, null));
    assertEquals( 0, uut.compare("a", "a"));
    assertTrue(uut.compare("a", "b") < 0);
    Collections.sort(sorted = new ArrayList<>(shuffled), uut);
    assertEquals(listOf("a", "b", "x", "z", null, null, null), sorted);
  }

  private <E> List<E> listOf(E... es) {
    return new ArrayList<>(Arrays.asList(es));
  }

}
