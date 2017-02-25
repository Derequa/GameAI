package thinking.trees.behaviortrees.nodes;

import java.util.LinkedList;

import model.Actor;

/**
 * This class provides a generic implementation and guidelines for all types of nodes
 * in a BehaviorTree.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public abstract class BehaviorTreeNode {
	
	/**
	 * This define the different value nodes can evaluate to.
	 * @author Derek Batts- dsbatts@ncsu.edu
	 *
	 */
	public enum NODE_STATE{
		FAIL,
		SUCCESS,
		RUNNING
	}
	
	/** The parent node for this node */
	private BehaviorTreeNode parent;
	/** The actor this node is controlling */
	protected Actor actor;
	/** The children nodes for this node */
	protected LinkedList<BehaviorTreeNode> children = new LinkedList<BehaviorTreeNode>();
	/** The current state of this node */
	protected NODE_STATE state;
	
	/**
	 * This construct a BehaviorTreeNode for a given actor.
	 * @param a The actor this node will control.
	 */
	public BehaviorTreeNode(Actor a){
		this(a, null);
	}
	
	/**
	 * This constructs a BehaviorTreeNode for a given actor with a parent.
	 * @param a The actor controlled by this node.
	 * @param parent The parent of this node.
	 */
	public BehaviorTreeNode(Actor a, BehaviorTreeNode parent){
		actor = a;
		this.parent = parent;
	}
	
	/**
	 * This gets the parent node of this node.
	 * @return The parent node.
	 */
	public BehaviorTreeNode getParent(){
		return parent;
	}
	
	/**
	 * This adds a child to the end of the list of children nodes for this node.
	 * @param node The node too add to this node's list of children.
	 */
	public void addChild(BehaviorTreeNode node){
		children.addLast(node);
	}
	
	/**
	 * This gets the current state of the node.
	 * @return The current state of the node.
	 */
	public NODE_STATE getState(){
		return state;
	}
	
	/**
	 * This method will reset the node for evaluation.
	 */
	public abstract void reset();

	/**
	 * This will evaluate the node and return and set the new state.
	 * @return The result/state of the evaluation.
	 */
	public abstract NODE_STATE visit();
	
}
