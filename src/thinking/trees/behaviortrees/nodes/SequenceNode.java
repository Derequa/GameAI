package thinking.trees.behaviortrees.nodes;

import model.Actor;

/**
 * This class implement a Sequence Node for a Behavior Tree.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class SequenceNode extends BehaviorTreeNode {

	/**
	 * This constructs a Sequence node with no parent and the given actor.
	 * @param a The actor this behavior tree controls.
	 */
	public SequenceNode(Actor a) {
		super(a);
	}
	
	/**
	 * This constructs a sequence node with the given parent and actor.
	 * @param a The actor this behavior tree controls.
	 * @param parent The parent node for this sequence node.
	 */
	public SequenceNode(Actor a, BehaviorTreeNode parent){
		super(a, parent);
	}

	/**
	 * The sequence node will return FAIL on the first child that fails, and SUCCESS if all
	 * children succeed. If all children so far in evaluation have succeeded and a child reports
	 * RUNNING, it will return RUNNING.
	 */
	@Override
	public NODE_STATE visit() {
		for(BehaviorTreeNode n : children){
			NODE_STATE childstate = n.visit();
			switch(childstate){
				case FAIL:		resetChildren();
								state = childstate;
								return state;
				case RUNNING:	state = childstate;
								return state;
				case SUCCESS:	continue;
			}
		}
		state = NODE_STATE.SUCCESS;
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
	
	private void resetChildren(){
		for(BehaviorTreeNode n : children)
			n.reset();
	}
}
