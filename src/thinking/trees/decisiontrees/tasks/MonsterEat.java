package thinking.trees.decisiontrees.tasks;

import model.Actor;
import model.Monster;
import thinking.trees.decisiontrees.nodes.DecisionTreeNode;

/**
 * This class implements a eating action for the monster.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class MonsterEat extends DecisionTreeNode {
	
	/** The monster we are controlling */
	private Monster m;
	/** The player we are hunting */
	private Actor player;
	
	/**
	 * This constructs the action for the monster eating the player.
	 * @param m The monster to control.
	 * @param player The player to eat.
	 */
	public MonsterEat(Monster m, Actor player){
		this.m = m;
		this.player = player;
	}

	/*
	 * (non-Javadoc)
	 * @see thinking.trees.decisiontrees.nodes.DecisionTreeNode#evaluateNode()
	 */
	@Override
	public int evaluateNode() {
		player.isAlive = false;
		m.isDancing = true;
		return DONE;
	}

}
