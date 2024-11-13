package com.bensler.decaf.util.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


class HierarchyTest {

  final Folder root = new Folder(null, "/");
  final Folder home = new Folder(root, "home");
  final Folder bobsHome = new Folder(home, "bob");
  final Folder alicesHome = new Folder(home, "alice");

  @Test
  @Disabled
  void testIsEmpty() {
    final Hierarchy<Folder> uut = new Hierarchy<>();

    uut.addAll(List.of(alicesHome, bobsHome, home, root));
    uut.remove(root, true);
    assertTrue(uut.isEmpty());
  }

	@Test
	void testAddRemove() {
    final Hierarchy<Folder> uut = new Hierarchy<>();

    assertTrue(uut.isEmpty(), "Freshly created Hierarchy is not empty!");
    uut.add(alicesHome);
    assertEquals(alicesHome, uut.getRoot(), "'alice' should be root");
    assertTrue(uut.getChildren(alicesHome).isEmpty(), "'alice' should be a leaf node");
    uut.add(bobsHome);
    assertTrue(uut.hasSyntheticRoot(), "syntheticRoot should be root");
    uut.add(home);
    assertEquals(home, uut.getRoot(), "'home' should be root");
    assertEquals(Set.of(alicesHome, bobsHome), uut.getChildren(home), "'alice' and 'bob' should be under 'home'");
    uut.add(root);
    assertEquals(List.of(root, home, alicesHome), uut.getPath(alicesHome), "'alice's path should be [/, home, alice]");
    assertEquals(Set.of(bobsHome,alicesHome), uut.getLeafNodes(), "leaf nodes should be [alice, bob]");
    uut.remove(home, false);
    assertTrue(uut.hasSyntheticRoot(), "syntheticRoot should be root");
    assertEquals(3, uut.getChildren(uut.getRoot()).size(), "there should be [/, alice, bob] under synth root");
    assertEquals(Set.of(root, bobsHome, alicesHome), uut.getLeafNodes(), "leaf nodes should be [/, alice, bob]");
	}

	@Test
	void equalsAndHashcode() {
	  final Hierarchy<Folder> tree1 = new Hierarchy<>();
	  final Hierarchy<Folder> tree2 = new Hierarchy<>();

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
