package com.bensler.decaf.swing.tree;

import com.bensler.decaf.swing.Viewable;


/**
 * A filter-class, with which one can specify, what part of an 
 * Hierachy should be displayed
 * 
 */
public interface TreeFilter {
	
	/**
	 * Specifies, which nodes should be displayed in the tree.
	 * If a node should not be shown, all of its children will automatically
	 * not be shown. 
	 * @param node The node to test
	 * @return if the node should be shown
	 */
	public boolean accept(Viewable node);

}
