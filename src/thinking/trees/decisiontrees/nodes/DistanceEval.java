package thinking.trees.decisiontrees.nodes;

import model.Actor;
import model.Monster;
import processing.core.PVector;
import thinking.trees.decisiontrees.tasks.MonsterChase;
import thinking.trees.decisiontrees.tasks.MonsterSeekFridge;

/**
 * This class implements a decision tree node for checking the distance
 * the monster is from the player.
 * @author Derek Batts - dsbatt@ncsu.edu
 *
 */
public class DistanceEval extends DecisionTreeNode {
	
	/** This constant signals what eval code should correspond to the Eat action for the monster */
	public static final int EAT_ACTION = 0;
	/** This constant signals what eval code should correspond to the chase action for the monster */
	public static final int CHASE_ACTION = 1;
	/** This constant signals what eval code should correspond to another action for the monster */
	public static final int OTHER_ACTION = 2;
	/** The monster being evaluated */
	private Monster monster;
	/** The player the monster is chasing */
	private Actor player;
	
	/**
	 * This constructs the distance evaluator node.
	 * @param monster The monster to look at.
	 * @param player The player the monster is chasing.
	 */
	public DistanceEval(Monster monster, Actor player){
		this.monster = monster;
		this.player = player;
	}

	/*
	 * (non-Javadoc)
	 * @see thinking.trees.decisiontrees.nodes.DecisionTreeNode#evaluateNode()
	 */
	@Override
	public int evaluateNode() {
		float distance = PVector.dist(monster.position, player.position);
		if(distance <= Monster.EATRADIUS){
			// I know this is a hacky way of resetting node... plz don't judge
			MonsterChase.hasRun = false;
			MonsterSeekFridge.hasRun = false;
			return EAT_ACTION;
		}
		else if((distance > Monster.EATRADIUS) && (distance <= Monster.LOOKRADIUS)){
			return CHASE_ACTION;
		}
		else{
			MonsterChase.hasRun = false;
			MonsterSeekFridge.hasRun = false;
			return OTHER_ACTION;
		}
	}

}
