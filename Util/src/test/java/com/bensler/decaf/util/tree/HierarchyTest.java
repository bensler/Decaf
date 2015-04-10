package com.bensler.decaf.util.tree;

import org.junit.Assert;
import org.junit.Test;

public class HierarchyTest {

    @Test
    public void testIsEmpty() {
        final Hierarchy tree = new Hierarchy();

        final Folder root = new Folder(null, "/");
        final Folder home = new Folder(root, "home");
        final Folder bobsHome = new Folder(home, "bob");
        final Folder alicesHome = new Folder(home, "alice");

        Assert.assertTrue("Freshly created Hierarchy is not empty!", tree.isEmpty());

        tree.add(alicesHome);
        Assert.assertEquals("'alice' should be root", alicesHome, tree.getRoot());
        tree.add(bobsHome);
        Assert.assertTrue("syntheticRoot should be root", tree.hasSyntheticRoot());
        tree.add(home);
        Assert.assertEquals("'home' should be root", home, tree.getRoot());
    }

}