package com.bensler.decaf.util.cmp;

import static com.bensler.decaf.util.cmp.CollatorComparator.COLLATOR_COMPARATOR;
import static com.bensler.decaf.util.cmp.NopComparator.NOP_COMPARATOR;
import static com.bensler.decaf.util.cmp.NullSafeComparator.NullPolicy.NULLS_FIRST;
import static com.bensler.decaf.util.cmp.NullSafeComparator.NullPolicy.NULLS_LAST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ComparatorTest {

  @Test
  public void nop() {
    final NopComparator cmp = NOP_COMPARATOR;

    Assert.assertEquals(0, cmp.compare("a", null));
    Assert.assertEquals(0, cmp.compare(null, "b"));
    Assert.assertEquals(0, cmp.compare(null, null));
    Assert.assertEquals(0, cmp.compare("a", "b"));
    Assert.assertEquals(0, cmp.compare(2, new Object()));
    Assert.assertEquals(0, cmp.compare(2f, "c"));
  }

  @Test
  public void nullSafe() {
    final List<String> shuffled = Lists.newArrayList("x", null, "a", null, null, "z", "b");
    Comparator<String> cmp;
    List<String> sorted;

    cmp = new NullSafeComparator<>(NULLS_FIRST, COLLATOR_COMPARATOR);
    Assert.assertEquals(-1, cmp.compare(null, "a"));
    Assert.assertEquals( 1, cmp.compare("a", null));
    Assert.assertEquals( 0, cmp.compare(null, null));
    Assert.assertEquals( 0, cmp.compare("a", "a"));
    Assert.assertTrue(cmp.compare("a", "b") < 0);
    Collections.sort(sorted = new ArrayList<>(shuffled), cmp);
    Assert.assertEquals(Lists.newArrayList(null, null, null, "a", "b", "x", "z"), sorted);

    cmp = new NullSafeComparator<>(NULLS_LAST, COLLATOR_COMPARATOR);
    Assert.assertEquals( 1, cmp.compare(null, "a"));
    Assert.assertEquals(-1, cmp.compare("a", null));
    Assert.assertEquals( 0, cmp.compare(null, null));
    Assert.assertEquals( 0, cmp.compare("a", "a"));
    Assert.assertTrue(cmp.compare("a", "b") < 0);
    Collections.sort(sorted = new ArrayList<>(shuffled), cmp);
    Assert.assertEquals(Lists.newArrayList("a", "b", "x", "z", null, null, null), sorted);
  }

}
