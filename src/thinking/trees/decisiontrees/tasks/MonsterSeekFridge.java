package thinking.trees.decisiontrees.tasks;

import graphs.Graph;
import graphs.Translator;
import model.Monster;
import model.Target;
import processing.core.PVector;
import thinking.NormalAI;
import thinking.NormalAI.PATHMODE;
import thinking.trees.decisiontrees.nodes.DecisionTreeNode;

/**
 * This class implements the seek-fridge action for the monster.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class MonsterSeekFridge extends DecisionTreeNode {

	/** The ID of the vertex where the fridge is at */
	private static final int FRIDGE = 42;
	/** The monster we are controlling */
	private Monster m;
	/** The vector representing the fridge's location */
	private PVector fridgeLocation;
	/** A flag for whether or not this action has run */
	public static boolean hasRun = false;
	
	/**
	 * This constructs the seek-fridge action.
	 * @param m The monster to control.
	 * @param g The graph the monster is on.
	 * @param t The translator for the graph.
	 */
	public MonsterSeekFridge(Monster m, Graph g, Translator t){
		this.m = m;
		fridgeLocation = t.localize(g.getVertex(FRIDGE));
	}

	/*
	 * (non-Javadoc)
	 * @see thinking.trees.decisiontrees.nodes.DecisionTreeNode#evaluateNode()
	 */
	@Override
	public int evaluateNode() {
		if(!hasRun){
			((NormalAI) m.thinker).pathFollowTo(new Target(fridgeLocation), PATHMODE.FORGET);
			hasRun = true;
		}
		return DONE;
	}

}
