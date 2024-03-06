package com.bensler.decaf.util.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;


class HierarchyTest {

	@Test
	void testIsEmpty() {
    final Hierarchy<Folder> tree = new Hierarchy<>();

    final Folder root = new Folder(null, "/");
    final Folder home = new Folder(root, "home");
    final Folder bobsHome = new Folder(home, "bob");
    final Folder alicesHome = new Folder(home, "alice");

    assertTrue(tree.isEmpty(), "Freshly created Hierarchy is not empty!");

    tree.add(alicesHome);
    assertEquals(alicesHome, tree.getRoot(), "'alice' should be root");
    assertTrue(tree.getChildren(alicesHome).isEmpty(), "'alice' should be a leaf node");
    tree.add(bobsHome);
    assertTrue(tree.hasSyntheticRoot(), "syntheticRoot should be root");
    tree.add(home);
    assertEquals(home, tree.getRoot(), "'home' should be root");
    assertEquals(Set.of(alicesHome, bobsHome), tree.getChildren(home), "'alice' and 'bob' should be under 'home'");
    tree.add(root);
    assertEquals(List.of(root, home, alicesHome), tree.getPath(alicesHome), "'alice's path should be [/, home, alice]");
    assertEquals(Set.of(bobsHome,alicesHome), tree.getLeafNodes(), "leaf nodes should be [alice, bob]");
    tree.remove(home, false);
    assertTrue(tree.hasSyntheticRoot(), "syntheticRoot should be root");
    assertEquals(3, tree.getChildren(tree.getRoot()).size(), "there should be [/, alice, bob] under synth root");
    assertEquals(Set.of(root, bobsHome, alicesHome), tree.getLeafNodes(), "leaf nodes should be [/, alice, bob]");
	}

	@Test
	void equalsAndHashcode() {
	  final Hierarchy<Folder> tree1 = new Hierarchy<>();
	  final Hierarchy<Folder> tree2 = new Hierarchy<>();

	  final Folder root = new Folder(null, "/");
	  final Folder home = new Folder(root, "home");
	  final Folder bobsHome = new Folder(home, "bob");
	  final Folder alicesHome = new Folder(home, "alice");

	  tree1.add(alicesHome);
	  tree1.add(bobsHome);
	  tree1.add(root);
	  tree1.add(home);

	  Set<Folder> expectedMembers = Set.of(root, home, bobsHome, alicesHome);
	  assertEquals(expectedMembers.size(), tree1.size(), "tree1 should have a size of " + expectedMembers.size());

	  assertNotEquals(tree1, tree2, "tree1 and tree2 should NOT be equal");
	  tree2.add(bobsHome);
	  assertNotEquals(tree1, tree2, "tree1 and tree2 should NOT be equal");
	  tree2.add(alicesHome);
	  assertNotEquals(tree1, tree2, "tree1 and tree2 should NOT be equal");
	  tree2.add(root);
	  assertEquals(3, tree2.size(), "tree1 should have a size of 3");
	  expectedMembers = Set.of(root, bobsHome, alicesHome);
	  assertEquals(3, tree2.size(), "tree1 should have a size of 3");
	  List<Folder> bobsHomePath = List.of(bobsHome);
	  assertEquals(bobsHomePath, tree2.getPath(bobsHome), "path ob bobsHome should only consist of bobsHome");
	  assertNotEquals(tree1, tree2, "tree1 and tree2 should NOT be equal");
	  tree2.add(home);
	  assertEquals(tree1, tree2, "tree1 and tree2 should be equal");
	  bobsHomePath = List.of(root, home, bobsHome);
	  assertEquals(bobsHomePath, tree2.getPath(bobsHome), "path ob bobsHome should be " + bobsHomePath);
	}

}
