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
import java.util.Set;

import com.bensler.decaf.util.CanceledException;
import com.bensler.decaf.util.NamedImpl;

/**
 * A Hierarchy forms a tree out of a collection of Hierarchicals. A synthetic root is used if there are more than one
 * nodes with an unknown or null parent ref. This is to make sure that there is always exactly one root.
 */
public class Hierarchy<E extends Hierarchical> extends Object implements Serializable {

    /**
     * this node is used as root if the members does not form a hierarchy (if there would be more than one root).
     */
//    private static final Hierarchical syntheticRoot_ = new Root();

    /**
     * Keys are all members of this hierarchy, values are the child nodes. null values are used for leaf nodes.
     */
    private final Map<E, Set<E>> children_;

    /**
     * root node of this hierarchy.
     */
    private E root_;

    /**
     * used by TreeModel *
     * Creates a new Hierarchy using all nodes of the given hierarchy.
     */
    public Hierarchy(final Hierarchy<E> hierarchy) {
        this();

        final Set<? extends E> members = hierarchy.getMembers();

        members.remove(hierarchy.getSyntheticRoot());
        addAll(members);
        if (hierarchy.hasSyntheticRoot() && (!hasSyntheticRoot())) {
            root_ = null;
            addChild(root_, null);
        }
    }

    /**
     * Creates a new empty hierarchy.
     */
    public Hierarchy() {
        children_ = new HashMap<E, Set<E>>();
        root_ = null;
        children_.put(root_, null);
    }

    /**
     * Creates a new hierarchy using the given members.
     */
    public Hierarchy(final Collection<? extends E> members) {
        this();
        addAll(members);
    }

    /**
     * used by TreeModel *
     *
     * Adds a new member to this Hierarchy. If node is already a part of this hierarchy, it will be removed silently
     * before adding again. If this hierarchy has a synthetic root the new node may be the new parent of formerly
     * unbound nodes. They will be no longer children of synthetic root. If synthetic root loses all of its children the
     * new node is the new root. If synthetic root keeps one child that will become the new root.
     */
    public void add(final E newNode) {
        final E oldNode;
        final E parent;

        if (newNode == null) {
            throw new IllegalArgumentException("Cannot add null to a Hierarchy.");
        }

        if ((oldNode = resolve(newNode)) != null) {

            // node is allready in this hierarchy
            final Hierarchical oldParent = resolveParent(oldNode);
            final Hierarchical newParent = resolveParent(newNode);

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
                if (hasSyntheticRoot()) {
                    final Set<E> rootChildren = tryMoveSynthRootChildren(newNode);

                    if (rootChildren.size() == 1) {
                        root_ = rootChildren.iterator().next();
                        children_.remove(null);
                    }
                }
            } else {

                // no parent found
                if (!hasSyntheticRoot()) {
                    if (newNode.equals(resolveParent(root_))) {
                        addChild(root_, newNode);
                        root_ = newNode;
                    } else {
                        addChild(newNode, null);
                        addChild(root_, null);
                        root_ = null;
                    }
                } else {

                    // synthetic root
                    final Set<E> rootChildren = tryMoveSynthRootChildren(newNode);

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
    private Set<E> tryMoveSynthRootChildren(final E possibleParent) {
        final Set<E> rootChildren = getChildren_(root_);

        for (E node : getChildren(root_)) {
            if (possibleParent.equals(resolveParent(node))) {
                addChild(node, possibleParent);
                rootChildren.remove(node);
            }
        }

        return rootChildren;
    }

    /**
     * Adds all collection member to this hierarchy.
     */
    public void addAll(final Collection<? extends E> nodes) {
        for (E newNode : nodes) {
            add(newNode);
        }
    }

    private void addChild(final E child, final E parent) {
        Set<E> children = children_.get(parent);

        if (children == null) {
            children = new HashSet<E>(2);
            children_.put(parent, children);
        } else {
            children.remove(child);
        }
        children.add(child);
    }

    private E resolveParent(final E node) {
        final E parent = resolve(node.getParent());

        return ((node == null) ? null : (((parent == null) && hasSyntheticRoot()) ? null : parent));
    }

    /** used by TreeModel */
    public E resolve(final Object ref) {
        if (ref != null) {
            final int refHash = ref.hashCode();

            for (E node : children_.keySet()) {
                if ((node != null) && (refHash == node.hashCode()) && node.equals(ref)) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * used by TreeModel *
     * @return  the root node of this Hierarchy.
     */
    public Hierarchical getRoot() {
        return root_;
    }

    /**
     * used by TreeModel *
     * @return  true if this hierarchies only member is its own synthetic root.
     */
    public boolean isEmpty() {
        return ((children_.size() == 1) && children_.containsKey(null));
    }

    /**
     * @return  the member count of this hierarchy.
     */
    public int getSize() {
        return (isEmpty() ? 0 : children_.size());
    }

    /**
     * @return  all members of this Hierarchy in undefined order
     */
    public Set<E> getMembers() {
        final Set<E> result = (isEmpty() ? (Set<E>) Collections.<E>emptySet() : new HashSet<E>(children_.keySet()));

        if (hasSyntheticRoot()) {
            result.remove(getRoot());
        }

        return result;
    }

    /** used by TreeModel */
    public void remove(final E member, final boolean recursive) {
        final boolean synthRoot = hasSyntheticRoot();

        if (synthRoot && (member == null)) {
            throw new IllegalArgumentException("Want not remove synthetic root.");
        }

        if (contains(member)) {
            final boolean removingRoot = member.equals(root_);
            final boolean hadChildren = !getChildren_(member).isEmpty();

            // handle children
            if (recursive) {
                for (E child : getChildren(member)) {
                    remove(child, recursive);
                }
            } else {
                final Set<? extends E> children = getChildren(member);

                if (!children.isEmpty()) {
                    for (E child : children) {
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
                final E parent = resolveParent(member);
                final Set<E> siblings = getChildren_(parent);

                siblings.remove(member);
                if (siblings.isEmpty()) {
                    children_.put(parent, null);
                }
            }
        }
    }

    /** used by TreeModel */
    @SuppressWarnings("unchecked")
    public Set<? extends E> getChildren(final Hierarchical member) {
        final Set<E> children = children_.get(member);

        return ((children != null) ? new HashSet<E>(children) : Collections.EMPTY_SET);
    }

    @SuppressWarnings("unchecked")
    private Set<E> getChildren_(final Hierarchical member) {
        final Set<E> children = children_.get(member);

        return ((children != null) ? children : Collections.EMPTY_SET);
    }

    /**
     * used by TreeModel *
     * Checks if a node is a member of this hierarchy.
     */
    public boolean contains(final Hierarchical node) {
        return children_.containsKey(node);
    }

    /**
     * Makes this hierarchy empty.
     */
    public void clear() {
        children_.clear();
        root_ = null;
        children_.put(root_, null);
    }

    /** used by TreeModel */
    public boolean hasSyntheticRoot() {
        return (root_ == null);
    }

    public static final class Root extends NamedImpl implements Hierarchical {

        public Root() {
            super("SynthRoot");
        }

        @Override
        public Hierarchical getParent() {
            return null;
        }

    }

    /** used by TreeModel */
    public List<E> getPath(E node) {
        final List<E> list = new ArrayList<E>(4);

        while (node != null) {
            list.add(0, node);
            node = resolveParent(node);
        }

        return list;
    }

    /** used by TreeModel */
    public E getSyntheticRoot() {
        return null;
    }

    public static class Collector extends Object implements Visitor {

        private final List<Hierarchical> collector_;

        public Collector() {
            collector_ = new ArrayList<Hierarchical>();
        }

        @Override
        public void visit(final Hierarchical member) {
            collector_.add(member);
        }

        public List<? extends Hierarchical> getList() {
            return collector_;
        }

    }

    public static interface Visitor {

        void visit(Hierarchical member) throws CanceledException;

    }

    /**
     * @see  java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.valueOf(getRoot()) + getMembers();
    }

    /**
     * Visits the whole forest beginning with the root nodes.
     */
    public Hierarchy.Visitor visitAll(final Hierarchy.Visitor visitor) {
        visitDown(visitor, getRoot());
        return visitor;
    }

    /**
     * visits the subtree having member as root.
     */
    public Hierarchy.Visitor visitUp(final Hierarchy.Visitor visitor, final Hierarchical startNode) {
        final Hierarchical member = resolve(startNode);

        if (member != null) {
            try {
                visitUp_(visitor, member);
            } catch (CanceledException ce) { /* flow control */
            }

            return visitor;
        } else {
            throw new IllegalStateException(member + " is not part of this hierarchy.");
        }
    }

    /**
     * visits the subtree having member as root.
     */
    public Hierarchy.Visitor visitDown(final Hierarchy.Visitor visitor, final Hierarchical startNode) {
        final Hierarchical member = resolve(startNode);

        if (member != null) {
            try {
                visitDown_(visitor, member);
            } catch (CanceledException ce) { /* flow control */
            }

            return visitor;
        } else {
            throw new IllegalArgumentException(member + " is not part of this hierarchy.");
        }
    }

    private void visitUp_(final Hierarchy.Visitor visitor, final Hierarchical member) throws CanceledException {
        if (member != null) {
            visitor.visit(member);
            if (member.getParent() != null) {
                visitUp_(visitor, resolve(member.getParent()));
            }
        }
    }

    private void visitDown_(final Hierarchy.Visitor visitor, final Hierarchical member) throws CanceledException {
        visitor.visit(member);
        for (Hierarchical child : getChildren_(member)) {
            visitDown_(visitor, child);
        }
    }

    public List<Hierarchical> getPath(final Hierarchical hierarchical, final List<Hierarchical> target) {
        final Hierarchical parent = resolve(hierarchical.getParent());

        target.add(0, hierarchical);
        if (parent == null) {
            if (hasSyntheticRoot()) {
                target.add(0, root_);
            }
        } else {
            getPath(parent, target);
        }

        return target;
    }

    public boolean isDescendantOf(final E parent, final E child) {
        return ((!child.equals(parent)) && getPath(child).contains(parent));
    }

    /**
     * Checks if a Set of nodes is a single path within this hierarchy.
     *
     * @param  nodes  List of Referrable objects
     */
    public boolean isSinglePath(final Set nodes) {
        throw new UnsupportedOperationException("to be implemented");
// List lastPath = null;
//
// for (Iterator iter = nodes.iterator(); iter.hasNext();) {
// final List currentPath = getPath((Entity)iter.next());
//
// if (lastPath == null) {
// lastPath = currentPath;
// } else {
// if (lastPath.size() < currentPath.size()) {
// if (currentPath.containsAll(lastPath)) {
// lastPath = currentPath;
// } else {
// return false;
// }
// } else {
// if (!lastPath.containsAll(currentPath)) {
// return false;
// }
// }
// }
// }
// return true;
    }

    /**
     * @return  a sub tree of a given node.
     */
    public Hierarchy<E> getSubHierarchy(final E subRoot) {
        if (!contains(subRoot)) {
            return new Hierarchy<E>();
        } else {
            return new Hierarchy(((Hierarchy.Collector) visitDown(new Hierarchy.Collector(), subRoot)).getList());
        }
    }

    /**
     * @return  all leaf nodes of this hierarchy (nodes having no child nodes in <b>this</b> hierarchy).
     */
    public Set<? extends E> getLeafNodes() {
        final Set<E> leafs = new HashSet<E>();

        if (isEmpty()) {
            return Collections.emptySet();
        }

        for (Entry<E, Set<E>> entry : children_.entrySet()) {
            if (entry.getValue() == null) {
                leafs.add(entry.getKey());
            }
        }

        return leafs;
    }

    /**
     * @return  all members of this hierarchy in depth first order.
     */
    public List<? extends Hierarchical> getMembersList() {
        return ((Hierarchy.Collector) visitAll(new Hierarchy.Collector())).getList();
    }

    public boolean isLeaf(final Hierarchical node) {
        return ((!isEmpty()) && children_.containsKey(node) && (children_.get(node) == null));
    }

}
