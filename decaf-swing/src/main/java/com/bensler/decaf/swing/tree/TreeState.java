package com.bensler.decaf.swing.tree;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.swing.tree.TreePath;

import com.bensler.decaf.util.tree.Hierarchical;

public class TreeState extends Object {

//  public static List<String> loadIdList(PrefKey prefKey) {
//    final List<String> result = new ArrayList<String>();
//    final String    string    = prefKey.loadString();
//    final String[]  ids       = string.split(",");
//
//    for (String id : ids) {
//      id = id.trim();
//      if (id.length() > 0) {
//        result.add(id);
//      } else {
//        result.add(null);
//      }
//    }
//    return result;
//  }

  private   final         Set<Hierarchical> expandedNodes_;

  private   final         Hierarchical      selectedNode_;

  TreeState(Enumeration<TreePath> expandedPaths, Hierarchical selectedNode) {
    super();

    expandedNodes_  = new HashSet<Hierarchical>();
    if (expandedPaths != null) {
      while (expandedPaths.hasMoreElements()) {
        expandedNodes_.add(
          (Hierarchical)(expandedPaths.nextElement()).getLastPathComponent()
        );
      }
    }
    selectedNode_ = selectedNode;
  }

  public TreeState(Collection<? extends Hierarchical> expandedNodes, Hierarchical selectedNode) {
    expandedNodes_  = new HashSet<Hierarchical>(expandedNodes);
    selectedNode_ = selectedNode;
  }

  void apply(EntityTree tree) {
    for (Hierarchical node : expandedNodes_) {
      tree.expandCollapse(node, true);
    }
    if (selectedNode_ != null) {
      tree.select(selectedNode_);
    }
  }

  public boolean hasSelection() {
    return (selectedNode_ != null);
  }

  public Set<Hierarchical> getExpandedNodes() {
    return expandedNodes_;
  }

//  void save(PrefKey prefKey) {
//    final StringBuilder builder   = new StringBuilder();
//          String        delimiter = "";
//
//    builder.append((selectedNode_ != null) ? selectedNode_.getKey() : "");
//    for (Hierarchical node : expandedNodes_) {
//      if (node instanceof Referrable) {
//        builder.append(delimiter).append(((Referrable)node).getKey());
//        delimiter = ",";
//      }
//    }
//    prefKey.saveString(builder.toString());
//  }

}
