package thinking.trees.behaviortrees;

import graphs.Graph;
import graphs.Translator;
import manager.Sketch;
import model.Actor;
import model.GameObject;
import model.Monster;
import thinking.Behavior;
import thinking.Output;
import thinking.trees.behaviortrees.nodes.BehaviorTreeNode;
import thinking.trees.behaviortrees.nodes.SelectorNode;
import thinking.trees.behaviortrees.nodes.SequenceNode;
import thinking.trees.behaviortrees.nodes.UntilFailDecorator;
import thinking.trees.behaviortrees.tasks.MonsterChase;
import thinking.trees.behaviortrees.tasks.MonsterDance;
import thinking.trees.behaviortrees.tasks.MonsterEat;
import thinking.trees.behaviortrees.tasks.MonsterLook;
import thinking.trees.behaviortrees.tasks.MonsterSeekFridge;
import thinking.trees.behaviortrees.tasks.MonsterWander;

/**
 * This class implements a fairly simple behavior tree for a monster in this demo.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class MonsterBehaviorTree extends Behavior {
	
	/** The node to visit first */
	private BehaviorTreeNode root;
	private Actor player;

	/**
	 * This constructs the behavior modeling the monster's behavior tree.
	 * @param g The monster this behavior is for.
	 * @param player The player the monster is hunting.
	 * @param graph The graph for the game world.
	 * @param translator The translator for the graph.
	 * @param sketch The sketch this is all drawn in.
	 */
	public MonsterBehaviorTree(Monster g, Actor player, Graph graph, Translator translator, Sketch sketch) {
		
		this.player = player;
		
		// Create the manager selector
		SelectorNode sel_manager = new SelectorNode(g);
		
		// Create a sequence where the monster tries to hunt the player
		SequenceNode seq_hunt = new SequenceNode(g, sel_manager);
		MonsterLook m_look = new MonsterLook(g, player, seq_hunt);
		MonsterChase m_chase = new MonsterChase(g, player, seq_hunt);
		MonsterEat m_eat = new MonsterEat(g, player, seq_hunt);
		MonsterDance m_dance = new MonsterDance(g, player, seq_hunt);
		
		// Create a sequence where the monster will wander until it gets hunger, and then will seek the fridge on the map
		SequenceNode seq_idle = new SequenceNode(g, sel_manager);
		UntilFailDecorator dec_wander = new UntilFailDecorator(g, seq_idle);
		MonsterWander m_wander = new MonsterWander(g, player, dec_wander, graph, translator);
		MonsterSeekFridge m_fridge = new MonsterSeekFridge(g, player, seq_idle, graph, translator);
		
		// Add nodes to the managing selector
		sel_manager.addChild(seq_hunt);
		sel_manager.addChild(seq_idle);
		// Add nodes to the hunt sequence
		seq_hunt.addChild(m_look);
		seq_hunt.addChild(m_chase);
		seq_hunt.addChild(m_eat);
		seq_hunt.addChild(m_dance);
		// Add nodes to the idle sequence
		seq_idle.addChild(dec_wander);
		dec_wander.addChild(m_wander);
		seq_idle.addChild(m_fridge);
		
		// Set the root
		root = sel_manager;
	}

	/*
	 * (non-Javadoc)
	 * @see thinking.Behavior#step()
	 */
	@Override
	public Output step() {
		if(player.isAlive)
			root.visit();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see thinking.Behavior#getTarget()
	 */
	@Override
	public GameObject getTarget() {
		return null;
	}

}
