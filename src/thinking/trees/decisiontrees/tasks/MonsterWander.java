package thinking.trees.decisiontrees.tasks;

import java.util.Random;

import graphs.Graph;
import graphs.Translator;
import model.Monster;
import model.Target;
import thinking.NormalAI;
import thinking.NormalAI.PATHMODE;
import thinking.trees.decisiontrees.nodes.DecisionTreeNode;

/**
 * This class implement a monster wandering action.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class MonsterWander extends DecisionTreeNode {
	
	/** The monster we are controlling */
	private Monster m;
	/** The graph the monster is on */
	private Graph g;
	/** The translator for the graph */
	private Translator t;
	/** A RNG for picking random vertices */
	private Random r = new Random();
	
	/**
	 * This constructs the wandering action.
	 * @param m The monster to control.
	 * @param g The graph the monster is on.
	 * @param t The translator for the graph.
	 */
	public MonsterWander(Monster m, Graph g, Translator t){
		this.m = m;
		this.g = g;
		this.t = t;
	}

	/*
	 * (non-Javadoc)
	 * @see thinking.trees.decisiontrees.nodes.DecisionTreeNode#evaluateNode()
	 */
	@Override
	public int evaluateNode() {
		if(((NormalAI) m.thinker).getActivePath() == null)
			((NormalAI) m.thinker).pathFollowTo(new Target(t.localize(g.getVertex(r.nextInt(g.getNumberOfVertices())))), PATHMODE.FORGET);
		return DONE;
	}

}
