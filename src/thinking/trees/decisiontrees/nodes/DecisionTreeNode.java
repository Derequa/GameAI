package thinking.trees.decisiontrees.nodes;

import java.util.ArrayList;

/**
 * This class models a generic Decision tree node that can be customized.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public abstract class DecisionTreeNode {
	
	/** A flag for when a leaf node finishes evaluation */
	public static final int DONE = -1;
	/** The parent node to this node */
	protected DecisionTreeNode parent;
	/** The list of children this node has */
	protected ArrayList<DecisionTreeNode> children = new ArrayList<DecisionTreeNode>();
	
	/**
	 * This will construct a new node with the given parent.
	 * @param parent The parent node to this one.
	 */
	public DecisionTreeNode(DecisionTreeNode parent){
		this.parent = parent;
	}
	
	/**
	 * This constructs a node with no parent.
	 */
	public DecisionTreeNode(){
		this(null);
	}
	
	/**
	 * This will ad a given child node to the end of the array of children.
	 * @param n The node to add as a child.
	 */
	public void addChild(DecisionTreeNode n){
		children.add(n);
	}
	
	/**
	 * This will add a child node to the given evaluation
	 * value. (recommend you add children in order first)
	 * @param n The node to add as a child.
	 * @param evalCode The evaluation value this child maps to.
	 */
	public void addChild(DecisionTreeNode n, int evalCode){
		if(evalCode > children.size())
			children.ensureCapacity(evalCode);
		children.add(evalCode, n);
	}
	
	/**
	 * This will evaluate this node and evaluate the child node corresponding
	 * to the value of evaluation, and recursively call the child's recursive evaluation
	 * method. If this node is a leaf the recursion will stop.
	 */
	public void evaluateRecursively(){
		int evaluation = evaluateNode();
		if(evaluation == DONE)
			return;
		else
			children.get(evaluation).evaluateRecursively();
	}
	
	/**
	 * This method will evaluate this node to an integer value/flag.
	 * This value should be mapped to a child node for this node.
	 * if this node is a leaf, this method should always evaluate to the DONE flag.
	 * @return The evaluation code for this node.
	 */
	public abstract int evaluateNode();
}
