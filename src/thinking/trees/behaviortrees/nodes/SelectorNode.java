package thinking.trees.behaviortrees.nodes;

import model.Actor;

/**
 * This class implements a Selector node for the a Behavior Tree.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class SelectorNode extends BehaviorTreeNode {

	/**
	 * This constructs a Selector node with no parent and the given actor.
	 * @param a The actor this behavior tree controls.
	 */
	public SelectorNode(Actor a) {
		super(a);
	}
	
	/**
	 * This constructs a Selector node with the given parent and actor.
	 * @param a The actor this behavior tree controls.
	 * @param parent The parent node for this selector.
	 */
	public SelectorNode(Actor a, BehaviorTreeNode parent){
		super(a, parent);
	}

	/**
	 * The selector node will return SUCCESS on the first child that succeeds,
	 * and FAIL if all the children fail. It will return RUNNING only if no children have been
	 * found that have succeeded and a RUNNING child is encountered.
	 */
	@Override
	public NODE_STATE visit() {
		for(BehaviorTreeNode n : children){
			NODE_STATE childstate = n.visit();
			switch(childstate){
				case FAIL:		continue;
				// Immediately return for RUNNINg and SUCCESS children
				case RUNNING:	state = childstate;
								return state;
				case SUCCESS:	state = childstate;
								return state;
			}
		}
		state = NODE_STATE.FAIL;
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
