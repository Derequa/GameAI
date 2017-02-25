package thinking.trees.behaviortrees.tasks;

import model.Actor;
import model.Monster;
import processing.core.PVector;
import thinking.trees.behaviortrees.nodes.BehaviorTreeNode;

/**
 * This class implements a check task where the monster looks for the player.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class MonsterLook extends MonsterTask {


	/**
	 * This constructs the look task for the monster.
	 * @param a The monster we are controlling.
	 * @param player The player we are hunting.
	 * @param parent The sketch this happens in.
	 */
	public MonsterLook(Actor a, Actor player, BehaviorTreeNode parent) {
		super(a, player, parent);
	}

	/**
	 * This checks if the player is within visible range and returns SUCCESS if it is.
	 */
	@Override
	public NODE_STATE visit() {
		if(PVector.dist(player.position, actor.position) <= Monster.LOOKRADIUS)
			state = NODE_STATE.SUCCESS;
		else
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
	}

}
