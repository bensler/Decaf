package com.bensler.decaf.util.tree;

import org.junit.Assert;
import org.junit.Test;

public class HierarchyTest {

    @Test
    public void testIsEmpty() {
        Assert.assertTrue("Freshly created Hierarchy is not empty!", new Hierarchy().isEmpty());
    }

}
