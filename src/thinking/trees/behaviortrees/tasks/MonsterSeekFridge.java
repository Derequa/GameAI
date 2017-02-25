package thinking.trees.behaviortrees.tasks;

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
 * This class implements a seeking task for the monster.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class MonsterSeekFridge extends MonsterTask {
	
	/** The ID of the vertex where the fridge is at */
	private static final int FRIDGE = 42;
	/** The graph for the game world */
	private Graph graph;
	/** The translator for the graph */
	private Translator translator;

	/**
	 * This constructs the fridge seeking task for the monster.
	 * @param a The monster's actor object.
	 * @param player The player the monster is hunting.
	 * @param parent The parent node for this task.
	 * @param g The graph of the game world.
	 * @param t The translator for the graph.
	 */
	public MonsterSeekFridge(Actor a, Actor player, BehaviorTreeNode parent, Graph g, Translator t) {
		super(a, player, parent);
		graph = g;
		translator = t;
	}

	/**
	 * This will check if it has requested a seek to the fridge, and seek the fridge if it hasn't.
	 * If it is seeking or will start seeking the fridge the task will report RUNNING. Once the monster
	 * finishes seeking, its hunger will reset and the task will report SUCCESS.
	 */
	@Override
	public NODE_STATE visit() {
		if(((Monster) actor).needsFood()){
			((NormalAI) actor.thinker).pathFollowTo(new Target(translator.localize(graph.getVertex(FRIDGE))), PATHMODE.FORGET);
			if(BehaviorLog.isLogging())
				BehaviorLog.logState(PVector.dist(player.position, actor.position), ((Monster) actor).needsFood(), ACTION.SEEK_FRIDGE);
			state = NODE_STATE.SUCCESS;
			((Monster) actor).resetHunger();
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
		((Monster) actor).resetHunger();
	}

}
