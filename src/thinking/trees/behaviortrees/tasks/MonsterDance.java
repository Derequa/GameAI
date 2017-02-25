package thinking.trees.behaviortrees.tasks;

import model.Actor;
import model.Monster;
import processing.core.PVector;
import thinking.trees.behaviortrees.nodes.BehaviorTreeNode;

/**
 * This class implements a Dancing task for the monster
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class MonsterDance extends MonsterTask {

	/**
	 * This will construct a dancing task for the monster.
	 * @param a The monster we are controlling.
	 * @param player The player we are hunting.
	 * @param parent The parent sketch this happens in.
	 */
	public MonsterDance(Actor a, Actor player, BehaviorTreeNode parent) {
		super(a, player, parent);
	}

	/**
	 * This will (redundantly) check if the player is in eating range and if the player is alive or not.
	 * If the these conditions are met, the isDancign flag will be set and the task will report SUCCESS.
	 * Otherwise, it will report FAIL.
	 */
	@Override
	public NODE_STATE visit() {
		if((PVector.dist(player.position, actor.position) <= Monster.EATRADIUS) && !player.isAlive){
			state = NODE_STATE.SUCCESS;
			((Monster) actor).isDancing = true;
		}
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
		((Monster) actor).isDancing = false;
	}

}
