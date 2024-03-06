package com.bensler.decaf.util.cmp;

import static com.bensler.decaf.util.cmp.CollatorComparator.COLLATOR_COMPARATOR;
import static com.bensler.decaf.util.cmp.NopComparator.NOP_COMPARATOR;
import static com.bensler.decaf.util.cmp.NullSafeComparator.NullPolicy.NULLS_FIRST;
import static com.bensler.decaf.util.cmp.NullSafeComparator.NullPolicy.NULLS_LAST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

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
  void nullSafe() {
    final List<String> shuffled = Lists.newArrayList("x", null, "a", null, null, "z", "b");
    Comparator<String> cmp;
    List<String> sorted;

    cmp = new NullSafeComparator<>(NULLS_FIRST, COLLATOR_COMPARATOR);
    assertEquals(-1, cmp.compare(null, "a"));
    assertEquals( 1, cmp.compare("a", null));
    assertEquals( 0, cmp.compare(null, null));
    assertEquals( 0, cmp.compare("a", "a"));
    assertTrue(cmp.compare("a", "b") < 0);
    Collections.sort(sorted = new ArrayList<>(shuffled), cmp);
    assertEquals(Lists.newArrayList(null, null, null, "a", "b", "x", "z"), sorted);

    cmp = new NullSafeComparator<>(NULLS_LAST, COLLATOR_COMPARATOR);
    assertEquals( 1, cmp.compare(null, "a"));
    assertEquals(-1, cmp.compare("a", null));
    assertEquals( 0, cmp.compare(null, null));
    assertEquals( 0, cmp.compare("a", "a"));
    assertTrue(cmp.compare("a", "b") < 0);
    Collections.sort(sorted = new ArrayList<>(shuffled), cmp);
    assertEquals(Lists.newArrayList("a", "b", "x", "z", null, null, null), sorted);
  }

}
