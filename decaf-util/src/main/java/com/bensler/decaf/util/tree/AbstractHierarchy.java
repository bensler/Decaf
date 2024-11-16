package com.bensler.decaf.util.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.bensler.decaf.util.CanceledException;

/**
 * A Hierarchy forms a tree out of a collection of {@link Hierarchical}s. A synthetic root is used if there are more than one
 * nodes with an unknown or null parent ref. This is to make sure that there is always exactly one root.
 */
public class AbstractHierarchy<H extends Hierarchical<H>, C extends Collection<H>> extends Object implements Serializable {

  private final ChildrenCollectionMaintainer<H, C> nanny_;
  /**
   * Keys are all members of this hierarchy, values are the child nodes. <code>null</code> values are leaf nodes,
   * <code>null</code> key is used as super root if there is no single root of all members.
   */
  private final Map<H, C> children_;

  /**
   * root node of this hierarchy.
   */
  private H root_;

  /**
   * Creates a new empty hierarchy.
   */
  public AbstractHierarchy(ChildrenCollectionMaintainer<H, C> nanny) {
    nanny_ = nanny;
    children_ = new HashMap<>();
    children_.put(root_ = null, null);
  }

  /**
   * used by TreeModel *
   *
   * Adds a new member to this Hierarchy. If node is already a part of this hierarchy, it will be removed silently
   * before adding again. If this hierarchy has a synthetic root the new node may be the new parent of formerly
   * unbound nodes. They will be no longer children of synthetic root. If synthetic root loses all of its children the
   * new node is the new root. If synthetic root keeps one child that will become the new root.
   */
  public void add(final H newNode) {
    final H oldNode;
    final H parent;

    Objects.requireNonNull(newNode, "Cannot add null");
    if ((oldNode = resolve(newNode)) != null) {
      // node is allready in this hierarchy
      final H oldParent = oldNode.getParent();
      final H newParent = newNode.getParent();

      if (((oldParent != null) && oldParent.equals(newParent)) || ((oldParent == null) && (newParent == null))) {
        // at the same position -> only replace the value
        children_.put(newNode, children_.get(oldNode));
        return;
      } else {
        remove(newNode, false);
      }
    }

    if (isEmpty()) {
      children_.remove(null);
      children_.put(newNode, null);
      root_ = newNode;
    } else {
      children_.put(newNode, null);
      parent = resolve(newNode.getParent());
      if (parent != null) {
        addChild(newNode, parent);
        if (hasNullRoot()) {
          final Collection<H> rootChildren = tryMoveSynthRootChildren(newNode);

          if (rootChildren.size() == 1) {
            root_ = rootChildren.iterator().next();
            children_.remove(null);
          }
        }
      } else {
        // no parent found
        if (!hasNullRoot()) {
          if (newNode.equals(root_.getParent())) {
            addChild(root_, newNode);
            root_ = newNode;
          } else {
            addChild(newNode, null);
            addChild(root_, null);
            root_ = null;
          }
        } else {
          // synthetic root
          final Collection<H> rootChildren = tryMoveSynthRootChildren(newNode);

          if (rootChildren.isEmpty()) {
            children_.remove(null);
            root_ = newNode;
          } else {
            addChild(newNode, root_);
          }
        }
      }
    }
  }

  /**
   * Should be called only from {@link #add(Hierarchical)}.
   *
   * @return  all (synth) roots children unable to move to the (im)possible new parent.
   */
  private Collection<H> tryMoveSynthRootChildren(final H possibleParent) {
    final Collection<H> rootChildren = getChildrenNoCopy(root_);

    for (H node : getChildren(root_)) {
      if (possibleParent.equals(node.getParent())) {
        addChild(node, possibleParent);
        rootChildren.remove(node);
      }
    }
    return rootChildren;
  }

  /**
   * Adds all collection member to this hierarchy.
   */
  public void addAll(final Collection<? extends H> nodes) {
    for (H newNode : nodes) {
      add(newNode);
    }
  }

  private void addChild(final H child, final H parent) {
    C children = children_.get(parent);

    if (children == null) {
      children_.put(parent, (children = nanny_.createCollection(List.of())));
    } else {
      children.remove(child);
    }
    nanny_.addChild(child, children);
  }

  public H resolve(final Object ref) {
    if (ref != null) {
      final int refHash = ref.hashCode();

      for (H node : children_.keySet()) {
        if ((node != null) && (refHash == node.hashCode()) && node.equals(ref)) {
          return node;
        }
      }
    }
    return null;
  }

  /**
   * used by TreeModel *
   * @return the root node of this Hierarchy or <code>null</code> if there is no single root of all members..
   */
  public H getRoot() {
    return root_;
  }

  /**
   * used by TreeModel *
   * @return  true if this hierarchies only member is its own synthetic root.
   */
  public boolean isEmpty() {
    return (children_.size() == 1) && children_.containsKey(null);
  }

  /**
   * @return  the member count of this hierarchy.
   */
  public int size() {
    return children_.size() - (children_.containsKey(null) ? 1 : 0);
  }

  /**
   * @return  all members of this Hierarchy in undefined order
   */
  public Set<H> getMembers() {
    final Set<H> result = (isEmpty() ? Collections.emptySet() : new HashSet<>(children_.keySet()));

    if (hasNullRoot()) {
      result.remove(getRoot());
    }
    return result;
  }

  /** used by TreeModel */
  public void remove(final H member, final boolean recursive) {
    final boolean synthRoot = hasNullRoot();

    Objects.requireNonNull(member, "Cannot remove null");
    if (contains(member)) {
      final boolean removingRoot = member.equals(root_);
      final Collection<H> children = getChildrenNoCopy(member);
      final boolean hadChildren = !children.isEmpty();

      // handle children
      if (hadChildren) {
        if (recursive) {
          for (H child : List.copyOf(children)) {
            remove(child, recursive);
          }
        } else {
          for (H child : children) {
            addChild(child, null);
          }
          if (!synthRoot) {
            addChild(root_, null);
          }
          root_ = null;
        }
      }

      children_.remove(member);

      // handle parent
      if (removingRoot) {
        root_ = null;
        if (!hadChildren) {
          children_.put(null, null);
        }
      } else {
        final H parent = member.getParent();
        final Collection<H> siblings = getChildrenNoCopy(parent);

        siblings.remove(member);
        if (siblings.isEmpty()) {
          children_.put(parent, null);
        }
      }
    }
  }

  /** used by TreeModel */
  public C getChildren(final Hierarchical<?> member) {
    if ((member == null) && (root_ != null)) {
      return nanny_.createCollection(List.of(root_));
    } else {
      checkMember(member);
      final C children = getChildrenNoCopy(member);

      return (children.isEmpty() ? children : nanny_.createCollection(children));
    }
  }

  /** used by TreeModel */
  public int getChildCount(final Hierarchical<?> parent) {
    checkMember(parent);
    return getChildrenNoCopy(parent).size();
  }

  protected C getChildrenNoCopy(final Hierarchical<?> member) {
    final C children = children_.get(member);

    return ((children != null) ? children : nanny_.createEmptyCollection());
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
  public boolean contains(final H node) {
    return children_.containsKey(node);
  }

  /**
   * Makes this hierarchy empty.
   */
  public void clear() {
    children_.clear();
    children_.put(root_ = null, null);
  }

  /** used by TreeModel */
  public boolean hasNullRoot() {
    return (root_ == null);
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
    return String.valueOf(getRoot()) + children_;
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
  public <V extends Visitor<H>> V visitAll(final V visitor) {
    visitDown(visitor, getRoot());
    return visitor;
  }

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
      try {
        visitDown_(visitor, member);
      } catch (CanceledException ce) { /* flow control */ }

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

  private void visitDown_(final Visitor<H> visitor, final H member) throws CanceledException {
    visitor.visit(member);
    for (H child : getChildrenNoCopy(member)) {
      visitDown_(visitor, child);
    }
  }

  public List<H> getPath(final H hierarchical, final List<H> target) {
    final H parent = hierarchical.getParent();

    target.add(0, hierarchical);
    if (parent == null) {
      if (hasNullRoot()) {
        target.add(0, root_);
      }
    } else {
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
      if (contains(subRoot)) {
        return visitDown(new Collector<H>(), subRoot).getList();
      } else {
        throw new IllegalArgumentException(subRoot + " is not member of this Hierarchy");
      }
    }

  /**
   * @return  all leaf nodes of this hierarchy (nodes having no child nodes in <b>this</b> hierarchy).
   */
  public Set<H> getLeafNodes() {
    if (isEmpty()) {
        return Collections.emptySet();
    } else {
      final Set<H> leafs = new HashSet<>();

      for (Entry<H, C> entry : children_.entrySet()) {
        if (entry.getValue() == null) {
          leafs.add(entry.getKey());
        }
      }
      return leafs;
    }
  }

  public boolean isLeaf(final Hierarchical<?> node) {
    return ((!isEmpty()) && children_.containsKey(node) && (children_.get(node) == null));
  }

}
