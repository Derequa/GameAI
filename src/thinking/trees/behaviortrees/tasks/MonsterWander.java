package thinking.trees.behaviortrees.tasks;

import java.util.Random;

import graphs.Graph;
import graphs.Translator;
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
 * This class implements a specialized wander mechanic for the Monster.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class MonsterWander extends MonsterTask {
	
	/** The graph to get vertices from */
	private Graph graph;
	/** The graph translator */
	private Translator translator;
	/** An RNG for randomly picking vertices */
	private Random r = new Random();

	/**
	 * This constructs the Monster's wander task.
	 * @param a The monster's actor object.
	 * @param player The player the monster is hunting.
	 * @param parent The parent node to this task.
	 * @param g The graph for the game world.
	 * @param t The translator for the graph.
	 */
	public MonsterWander(Actor a, Actor player, BehaviorTreeNode parent, Graph g, Translator t) {
		super(a, player, parent);
		graph = g;
		translator = t;
	}

	/**
	 * This will return SUCCESS in between finding random paths, and RUNNING when seeking a path.
	 * Each call will increase the monster's hunger level. Once the hunger level passes a threshold
	 * the task will FAIL.
	 */
	@Override
	public NODE_STATE visit() {
		((Monster) actor).stepHunger();
		if(((NormalAI) actor.thinker).getActivePath() != null){
			state = NODE_STATE.RUNNING;
			return state;
		}
		if(((Monster) actor).needsFood()){
			state = NODE_STATE.FAIL;
			return state;
		}
		((NormalAI) actor.thinker).pathFollowTo(new Target(translator.localize(graph.getVertex(r.nextInt(graph.getNumberOfVertices())))), PATHMODE.FORGET);
		if(BehaviorLog.isLogging())
			BehaviorLog.logState(PVector.dist(player.position, actor.position), ((Monster) actor).needsFood(), ACTION.WANDER);
		state = NODE_STATE.SUCCESS;
		return state;
	}

	/*
	 * (non-Javadoc)
	 * @see thinking.trees.nodes.BehaviorTreeNode#reset()
	 */
	@Override
	public void reset() {
		state = null;
		((Monster) actor).resetHunger();
	}

}
