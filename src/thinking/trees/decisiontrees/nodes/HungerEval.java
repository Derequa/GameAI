package thinking.trees.decisiontrees.nodes;

import model.Monster;
import thinking.trees.decisiontrees.tasks.MonsterChase;
import thinking.trees.decisiontrees.tasks.MonsterSeekFridge;

/**
 * This constructs the node that evaluates the monster's hunger in its
 * learned decision tree.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class HungerEval extends DecisionTreeNode {
	
	/** This evaluation code will be returned when the monster should seek the fridge */
	public static final int FRIDGE_ACTION = 0;
	/** This evaluation code will be returned when the monster should wander */
	public static final int WANDER_ACTION = 1;
	/** The monster we control */
	private Monster m;
	
	/**
	 * This constructs the evaluation node for hunger.
	 * @param m The monster to examine.
	 */
	public HungerEval(Monster m){
		this.m = m;
	}

	/*
	 * (non-Javadoc)
	 * @see thinking.trees.decisiontrees.nodes.DecisionTreeNode#evaluateNode()
	 */
	@Override
	public int evaluateNode() {
		if(m.needsFood()){
			MonsterChase.hasRun = false;
			return FRIDGE_ACTION;
		}
		else{
			MonsterSeekFridge.hasRun = false;
			MonsterChase.hasRun = false;
			return WANDER_ACTION;
		}
	}

}
