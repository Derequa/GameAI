package thinking.trees.behaviortrees.tasks;

import model.Actor;
import model.Monster;
import model.Target;
import processing.core.PVector;
import thinking.NormalAI;
import thinking.NormalAI.PATHMODE;
import thinking.trees.BehaviorLog;
import thinking.trees.BehaviorLog.ACTION;
import thinking.trees.behaviortrees.nodes.BehaviorTreeNode;

/**
 * This class implements a chasing task for the monster.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class MonsterChase extends MonsterTask {
	
	/** A flag for whether or not this has been run yet */
	private boolean hasRun = false;

	/**
	 * This constructs the chasing task for the monster.
	 * @param a The monster we are controlling.
	 * @param player The player we are hunting.
	 * @param parent The sketch this happens in.
	 */
	public MonsterChase(Actor a, Actor player, BehaviorTreeNode parent) {
		super(a, player, parent);
	}

	/**
	 * This checks if the player is still close enough and attempts to path find to the player
	 * if so. It reports FAIL if the player is out of range, SUCCESS if the player comes into eating range,
	 * and RUNNING if actively path finding to the player.
	 */
	@Override
	public NODE_STATE visit() {
		float dist = PVector.dist(player.position, actor.position);
		if( dist <= Monster.EATRADIUS){
			state = NODE_STATE.SUCCESS;
		}
		else if(PVector.dist(player.position, actor.position) > Monster.LOOKRADIUS)
			state = NODE_STATE.FAIL;
		else{
			if((((NormalAI) actor.thinker).getActivePath() == null) || !hasRun){
				((NormalAI) actor.thinker).pathFollowTo(new Target(player.position), PATHMODE.FORGET);
				if(BehaviorLog.isLogging())
					BehaviorLog.logState(dist, ((Monster) actor).needsFood(), ACTION.SEEK_PLAYER);
				hasRun = true;
			}
			state = NODE_STATE.RUNNING;
		}
		return state;
	}

	/*
	 * (non-Javadoc)
	 * @see thinking.trees.nodes.BehaviorTreeNode#reset()
	 */
	@Override
	public void reset() {
		state = null;
		hasRun = false;
	}

}
