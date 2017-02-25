package thinking.trees.decisiontrees;

import model.GameObject;
import thinking.Behavior;
import thinking.Output;
import thinking.trees.decisiontrees.nodes.DecisionTreeNode;

/**
 * This class allows a decision tree to be run as a behavior as a
 * part of the AI component.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class LearnedDecisionTree extends Behavior {
	
	/** The root node of the decision tree */
	private DecisionTreeNode root;
	
	/**
	 * This will setup a behavior for the AI component to run
	 * with the given decision tree.
	 * @param root The root node of the decision tree.
	 */
	public LearnedDecisionTree(DecisionTreeNode root){
		this.root = root;
	}

	/**
	 * This will traverse and evaluate the given decision tree.
	 */
	@Override
	public Output step() {
		root.evaluateRecursively();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see thinking.Behavior#getTarget()
	 */
	@Override
	public GameObject getTarget() {
		return null;
	}

}
