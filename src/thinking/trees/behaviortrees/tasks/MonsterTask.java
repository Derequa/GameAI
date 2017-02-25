package thinking.trees.behaviortrees.tasks;

import model.Actor;
import thinking.trees.behaviortrees.nodes.BehaviorTreeNode;

/**
 * This class provides a base implementation and guideline for all of the monster's tasks.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public abstract class MonsterTask extends BehaviorTreeNode {
	
	/** The player the monster is hunting */
	protected Actor player;
	
	/**
	 * This defines a generic Monster task using the BehaviorTreeNode super constructor.
	 * @param a The monster's Actor object.
	 * @param player The player the monster is hunting.
	 * @param parent The parent node to this task.
	 */
	public MonsterTask(Actor a, Actor player, BehaviorTreeNode parent){
		super(a, parent);
		this.player = player;
	}
	
	/*
	 * (non-Javadoc)
	 * @see thinking.trees.nodes.BehaviorTreeNode#addChild(thinking.trees.nodes.BehaviorTreeNode)
	 */
	@Override
	public void addChild(BehaviorTreeNode node){}

}
