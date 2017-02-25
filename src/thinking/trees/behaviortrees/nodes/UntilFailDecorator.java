package thinking.trees.behaviortrees.nodes;

import model.Actor;

/**
 * This class implements a Decorator node what will visit its children
 * until they all return FAIL.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class UntilFailDecorator extends BehaviorTreeNode {
	
	/**
	 * This constructs the Decorator with no parent and the given actor.
	 * @param a The actor this behavior tree controls.
	 */
	public UntilFailDecorator(Actor a) {
		super(a);
	}

	/**
	 * This contructs the Decorator with the given parent node and actor.
	 * @param a The actor this behavior tree controls.
	 * @param parent The parent node for this decorator.
	 */
	public UntilFailDecorator(Actor a, BehaviorTreeNode parent) {
		super(a, parent);
	}

	/**
	 * This will visit all the children nodes and return SUCCESS when all of them
	 * report FAIL. It will return RUNNIGN if it encounters a child that reports RUNNING.
	 * It will never report FAIL.
	 */
	@Override
	public NODE_STATE visit() {
		boolean allFail = true;
		for(BehaviorTreeNode n : children){
			NODE_STATE childstate = n.visit();
			switch(childstate){
				case FAIL:		break;
				case RUNNING:	allFail = false;
								break;
				case SUCCESS:	allFail = false;
								break;
			}
		}
		if(allFail)
			state = NODE_STATE.SUCCESS;
		else
			state = NODE_STATE.RUNNING;
		return state;
	}
	
	/*
	 * (non-Javadoc)
	 * @see thinking.trees.nodes.BehaviorTreeNode#reset()
	 */
	@Override
	public void reset() {
		state = null;
		for(BehaviorTreeNode n : children)
			n.reset();
	}

}
