package com.bensler.decaf.util.tree;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;

public class HierarchyTest {

    @Test
    public void testIsEmpty() {
        final Hierarchy<Folder> tree = new Hierarchy<Folder>();

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
        tree.add(root);
        Assert.assertEquals("'alice's path should be [/, home, alice]", Arrays.asList(root, home, alicesHome), tree.getPath(alicesHome));
        Assert.assertEquals("leaf nodes should be [alice, bob]", Sets.newHashSet(bobsHome,alicesHome), tree.getLeafNodes());
        tree.remove(home, false);
        Assert.assertTrue("syntheticRoot should be root", tree.hasSyntheticRoot());
        Assert.assertEquals("there should be [/, alice, bob] under synth root", 3, tree.getChildren(tree.getRoot()).size());
        Assert.assertEquals("leaf nodes should be [/, alice, bob]", Sets.newHashSet(root, bobsHome,alicesHome), tree.getLeafNodes());
    }

    @Test
    public void equalsAndHashcode() {
      final Hierarchy<Folder> tree1 = new Hierarchy<Folder>();
      final Hierarchy<Folder> tree2 = new Hierarchy<Folder>();

      final Folder root = new Folder(null, "/");
      final Folder home = new Folder(root, "home");
      final Folder bobsHome = new Folder(home, "bob");
      final Folder alicesHome = new Folder(home, "alice");

      tree1.add(alicesHome);
      tree1.add(bobsHome);
      tree1.add(root);
      tree1.add(home);

      Assert.assertFalse("tree1 and tree2 should NOT be equal", tree1.equals(tree2));
      tree2.add(home);
      Assert.assertFalse("tree1 and tree2 should NOT be equal", tree1.equals(tree2));
      tree2.add(bobsHome);
      Assert.assertFalse("tree1 and tree2 should NOT be equal", tree1.equals(tree2));
      tree2.add(alicesHome);
      Assert.assertFalse("tree1 and tree2 should NOT be equal", tree1.equals(tree2));
      tree2.add(root);
      Assert.assertTrue("tree1 and tree2 should be equal", tree1.equals(tree2) & tree2.equals(tree1));
    }

}
