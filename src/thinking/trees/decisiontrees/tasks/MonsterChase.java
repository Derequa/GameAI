package thinking.trees.decisiontrees.tasks;

import model.Actor;
import model.Monster;
import model.Target;
import thinking.NormalAI;
import thinking.NormalAI.PATHMODE;
import thinking.trees.decisiontrees.nodes.DecisionTreeNode;

/**
 * This class implements a chase action for the monster's learned
 * decision tree.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class MonsterChase extends DecisionTreeNode {
	
	/** The monster we are controlling */
	private Monster m;
	/** The player we are chasing */
	private Actor player;
	/** A flag for where or not this node has run */
	public static boolean hasRun = false;
	
	/**
	 * This constructs a chase action for the monster.
	 * @param m The monster to control.
	 * @param player The player to chase.
	 */
	public MonsterChase(Monster m, Actor player){
		this.m = m;
		this.player = player;
	}

	/*
	 * (non-Javadoc)
	 * @see thinking.trees.decisiontrees.nodes.DecisionTreeNode#evaluateNode()
	 */
	@Override
	public int evaluateNode() {
		if((((NormalAI) m.thinker).getActivePath() == null) || !hasRun){
			((NormalAI)m.thinker).pathFollowTo(new Target(player.position), PATHMODE.FORGET);
			hasRun = true;
		}
		return DONE;
	}

}
