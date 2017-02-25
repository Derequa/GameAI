package thinking.trees.behaviortrees.tasks;

import model.Actor;
import model.Monster;
import processing.core.PVector;
import thinking.trees.BehaviorLog;
import thinking.trees.BehaviorLog.ACTION;
import thinking.trees.behaviortrees.nodes.BehaviorTreeNode;

/**
 * This class implements an eating task for the monster.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class MonsterEat extends MonsterTask {

	/**
	 * This constructs eating task for the monster.
	 * @param a The monster we are controlling.
	 * @param player The player we are hunting.
	 * @param parent The sketch this happens in.
	 */
	public MonsterEat(Actor a, Actor player, BehaviorTreeNode parent) {
		super(a, player, parent);
	}

	/**
	 * This will (redundantly) check if the player is in eating range.
	 * If so, it will kill the player by changing its isAlive field and report SUCCESS.
	 * Otherwise, it will report FAIl.
	 */
	@Override
	public NODE_STATE visit() {
		float dist = PVector.dist(player.position, actor.position);
		if((dist <= Monster.EATRADIUS)){
			if(BehaviorLog.isLogging())
				BehaviorLog.logState(dist, ((Monster) actor).needsFood(), ACTION.EAT_PLAYER);
			player.isAlive = false;
			state = NODE_STATE.SUCCESS;
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
	}

}
