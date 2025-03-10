package com.bensler.decaf.util.tree;

import static com.bensler.decaf.util.function.ForEachMapperAdapter.forEachMapper;
import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.bensler.decaf.util.CanceledException;

/**
 * A Hierarchy forms a tree out of a collection of {@link Hierarchical}s. A null root is used if there are more than one
 * nodes with an unknown or null parent ref. This is to make sure that there is always exactly one root.
 */
public class AbstractHierarchy<H extends Hierarchical<H>, C extends Collection<H>> extends Object implements Serializable {

  private final Map<H, H> members_;
  private final ChildrenCollectionMaintainer<H, C> childCollectionMaintainer_;
  /**
   * Keys are all members of this hierarchy, values are the child nodes. <code>null</code> values are leaf nodes,
   * <code>null</code> key is used as super root if there is no single root of all members.
   */
  private final Map<H, C> children_;

  /**
   * Creates a new empty hierarchy.
   */
  public AbstractHierarchy(ChildrenCollectionMaintainer<H, C> childCollectionMaintainer) {
    childCollectionMaintainer_ = childCollectionMaintainer;
    members_ = new HashMap<>();
    children_ = new HashMap<>();
    children_.put(null, null);
    children_.put(null, childCollectionMaintainer_.createEmptyCollection());
  }

  /**
   * Adds a new member to this Hierarchy. If node is already a part of this hierarchy, it will be removed silently
   * before adding again. If this hierarchy has a null root the new node may be the new parent of formerly
   * unbound nodes. They will be no longer children of null root. If null root loses all of its children the
   * new node is the new root. If null root keeps one child that will become the new root.
   */
  public void add(final H newNode) {
    final List<H> oldRoots = new ArrayList<>(children_.get(null));
    final List<H> noLongerRoot;

    requireNonNull(newNode, "Cannot add null");
    remove(newNode, false);
    addChild(newNode);
    members_.put(newNode, newNode);
    children_.put(newNode, childCollectionMaintainer_.createEmptyCollection());

    oldRoots.remove(newNode);
    noLongerRoot = oldRoots.stream()
      .filter(oldRoot -> newNode == resolve(oldRoot.getParent()))
      .map(forEachMapper(this::addChild))
      .toList();

    children_.get(null).removeAll(noLongerRoot);
  }

  private H addChild(final H newNode) {
    final H parent = resolve(newNode.getParent());

    children_.put(parent, childCollectionMaintainer_.addChild(newNode, children_.get(parent)));
    return parent;
  }

  /**
   * Adds all collection member to this hierarchy.
   */
  public void addAll(final Collection<? extends H> nodes) {
    nodes.forEach(this::add);
  }

  public H resolve(final Object ref) {
    return members_.get(ref);
  }

  /**
   * used by TreeModel *
   * @return the root node of this Hierarchy or <code>null</code> if there is no single root of all members..
   */
  public C getRoots() {
    return childCollectionMaintainer_.createCopy(children_.get(null));
  }

  public H getSingleRoot() {
    return children_.get(null).iterator().next();
  }

  public boolean hasSingleRoot() {
    return (children_.get(null).size() == 1);
  }

  /**
   * used by TreeModel *
   * @return  true if this hierarchies only member is its own null root.
   */
  public boolean isEmpty() {
    return children_.get(null).isEmpty();
  }

  /**
   * @return  the member count of this hierarchy.
   */
  public int size() {
    return members_.size();
  }

  /**
   * @return  all members of this Hierarchy in undefined order
   */
  public Set<H> getMembers() {
    return members_.keySet().stream().filter(Objects::nonNull).collect(Collectors.toSet());
  }

  public void removeNode(final H member) {
    remove(member, false);
  }

  public void removeTree(final H member) {
    remove(member, true);
  }

  protected void remove(H toRemove, final boolean recursive) {
    toRemove = resolve(toRemove);

    if (toRemove != null) {
      final C orphans = children_.remove(toRemove);

      members_.remove(toRemove);
      if (recursive) {
        orphans.forEach(orphan -> remove(orphan, true));
      } else {
        orphans.forEach(orphan -> children_.put(null, childCollectionMaintainer_.addChild(orphan, children_.get(null))));
      }

      children_.get(resolve(toRemove.getParent())).remove(toRemove);
    }
  }

  public C getChildren(final H member) {
    return Optional.ofNullable(children_.get(member))
      .map(childCollectionMaintainer_::createCopy)
      .orElseGet(childCollectionMaintainer_::createEmptyCollection);
  }

  /** used by TreeModel */
  public int getChildCount(final Hierarchical<?> parent) {
    checkMember(parent);
    return getChildrenNoCopy(parent).size();
  }

  protected C getChildrenNoCopy(final Hierarchical<?> member) {
    final C children = children_.get(member);

    return ((children != null) ? children : childCollectionMaintainer_.createEmptyCollection());
  }

  protected void checkMember(final Hierarchical<?> member) {
    if (!children_.containsKey(member)) {
      throw new IllegalArgumentException(member + " is not member of this Hierarchy");
    }
  }

  /**
   * used by TreeModel <p>
   * Checks if a node is a member of this hierarchy.
   */
  public Optional<H> contains(final Object node) {
    return Optional.ofNullable(members_.get(node));
  }

  /**
   * Makes this hierarchy empty.
   */
  public void clear() {
    members_.clear();
    members_.put(null, null);
    children_.clear();
    children_.put(null, childCollectionMaintainer_.createEmptyCollection());
  }

  /** used by TreeModel */
  public List<H> getPath(H node) {
    final List<H> list = new ArrayList<>(4);

    if (children_.containsKey(node)) {
      while (node != null) {
        list.add(0, resolve(node));
        node = resolve(node.getParent());
      }
    }
    return list;
  }

  @Override
  public String toString() {
    return children_.toString();
  }

  @Override
  public int hashCode() {
    return children_.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    } else {
      return (
        (obj instanceof AbstractHierarchy)
        && children_.equals(((AbstractHierarchy<?, ?>) obj).children_)
      );
    }
  }

  /**
   * Visits the whole forest beginning with the root nodes.
   */
//  public <V extends Visitor<H>> V visitAll(final V visitor) {
//    visitDown(visitor, getRoot());
//    return visitor;
//  }

  /**
   * visits the subtree having member as root.
   */
  public <V extends Visitor<H>> V visitUp(final V visitor, final H startNode) {
    final H member = resolve(startNode);

    if (member != null) {
      try {
        visitUp_(visitor, member);
      } catch (CanceledException ce) { /* flow control */ }

      return visitor;
    } else {
      throw new IllegalStateException(member + " is not part of this hierarchy.");
    }
  }

  /**
   * visits the subtree having member as root.
   */
  public  <V extends Visitor<H>> V visitDown(final V visitor, final H startNode) {
    final H member = resolve(startNode);

    if (member != null) {
      visitDown_(visitor, member);
      return visitor;
    } else {
      throw new IllegalArgumentException(member + " is not part of this hierarchy.");
    }
  }

  private void visitUp_(final Visitor<H> visitor, final H member) throws CanceledException {
    if (member != null) {
      final H parent = member.getParent();

      visitor.visit(member);
      if (parent != null) {
        visitUp_(visitor, resolve(parent));
      }
    }
  }

  private void visitDown_(final Visitor<H> visitor, final H member) {
    visitor.visit(member);
    for (H child : getChildrenNoCopy(member)) {
      visitDown_(visitor, child);
    }
  }

  public List<H> getPath(final H hierarchical, final List<H> target) {
    final H parent = resolve(hierarchical.getParent());

    target.add(0, hierarchical);
    if (parent != null) {
      getPath(parent, target);
    }
    return target;
  }

  public boolean isDescendantOf(final H parent, final H child) {
    return ((!child.equals(parent)) && getPath(child).contains(parent));
  }

  /**
   * @return  a sub tree of a given node.
   */
  public List<H> getSubHierarchyMembers(final H subRoot) {
    return contains(subRoot)
      .map(node -> visitDown(new Collector<H>(), subRoot).getList())
      .orElseThrow(() -> new IllegalArgumentException(subRoot + " is not member of this Hierarchy"));
  }

  /**
   * @return  all leaf nodes of this hierarchy (nodes having no child nodes in <b>this</b> hierarchy).
   */
  public Set<H> getLeafNodes() {
    return children_.entrySet().stream()
    .filter(entry -> (entry.getKey() != null))
    .filter(entry -> entry.getValue().isEmpty())
    .map(Map.Entry::getKey)
    .collect(Collectors.toSet());
  }

  public boolean isLeaf(final Hierarchical<?> node) {
    return children_.get(node).isEmpty();
  }

}
