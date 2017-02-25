package manager;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import graphs.Graph;
import graphs.Heuristic.H_MODE;
import graphs.Translator;
import model.*;
import thinking.*;
import thinking.NormalAI.PATHMODE;
import thinking.paths.Path;
import thinking.paths.PathFinding;
import thinking.trees.TreeLearner;
import thinking.trees.behaviortrees.MonsterBehaviorTree;
import thinking.trees.decisiontrees.LearnedDecisionTree;
import thinking.trees.decisiontrees.SimpleDecisionTree;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;

/**
 * This class manages game objects, AI, updating, and drawing for all the objects in the game.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Sketch extends PApplet{
	
	/**
	 * This type defines the modes the sketch can run in.
	 * @author Derek Batts - dsbatts@ncsu.edu
	 *
	 */
	public enum SKETCHMODE {
		KINEMATIC,
		STEERINGSEEK,
		STEERINGARRIVE,
		STEERINGWANDER,
		FLOCKING,
		PATHFOLLOWING,
		DECISIONTREE,
		BEHAVIORTREE,
		L_DECISIONTREE
	}
	
	/** The width of the sketch */
	public static final int WIDTH = 800;
	/** The height of the sketch */
	public static final int HEIGHT = 600;
	/** The width of the actor (generally) */
	public static final int ACTOR_WIDTH = 15;
	/** The height of the actor (generally) */
	public static final int ACTOR_HEIGHT = 24;
	/** The marker for object GUIDs */
	public int guidMarker = 0;
	/** The object GUID map for all objects */
	public HashMap<Integer, GameObject> objects = new HashMap<Integer, GameObject>();
	/** Active animation objects */
	public LinkedList<Animation> animations = new LinkedList<Animation>();
	/** Active paths to draw */
	public LinkedList<Path> activePaths = new LinkedList<Path>();
	/** The updater component this this game */
	public Updater u = new SimpleUpdater();
	/** The queue of objects to add to the game */
	private LinkedList<GameObject> objQueue = new LinkedList<GameObject>();
	/** The queue of objects to remove from the game */
	private LinkedList<GameObject> removeQueue = new LinkedList<GameObject>();
	/** A GameObject controlled by the mouse */
	private GameObject controlledCharacter = null;
	/** How often to re-target flock members */
	private static final int TIMER = 5;
	/** The mode set for this run of the game */
	SKETCHMODE mode = null;
	/** The current heuristic mode for path finding */
	public H_MODE heuristic = null;
	/** A simple counter timer */
	private int timer = 0;
	/** The background image */
	private PImage bg;
	/** The graph being used if in path-finding mode */
	private Graph graph = null;
	/** The translator being used if in path-finding mode */
	private Translator translator = null;
	/** An animation to play on player death */
	private TextAnimation deathScreen;
	/** A rectangle for backing the death screen text animation */
	private SimpleRectangle backdrop = new SimpleRectangle(this, guidMarker++, new PVector());
	/** A reference to the monster in the world */
	private Monster monster = null;
	/** The decision tree learning component for rebuilding the learned decision tree on restart */
	private TreeLearner learner;
	
	/*
	 * (non-Javadoc)
	 * @see processing.core.PApplet#settings()
	 */
	@Override
	public void settings(){
		heuristic = Runner.heuristic;
		bg = loadImage("backgrounds/room.jpg");
		mode = Runner.lastMode;
		size(WIDTH,  HEIGHT);
		// Run setup based on mode
		switch (mode){
			case KINEMATIC:			setupKinematic();
									break;
			case STEERINGSEEK:		setupSteeringSeek();
									break;
			case STEERINGARRIVE:	setupSteeringArrive();
									break;
			case STEERINGWANDER:	setupSteeringWander();
									break;
			case FLOCKING:			setupFlocking();
									break;
			case PATHFOLLOWING:		setupPathFollowing();
									break;
			case DECISIONTREE:		setupDecisionTree();
									break;
			case BEHAVIORTREE:		setupBehaviorTree();
									break;
			case L_DECISIONTREE:	setupLearningTree();
									break;
			default:				System.exit(1);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see processing.core.PApplet#draw()
	 */
	@Override
	public void draw(){
		if((mode == SKETCHMODE.PATHFOLLOWING) || (mode == SKETCHMODE.DECISIONTREE) || (mode == SKETCHMODE.BEHAVIORTREE) || (mode == SKETCHMODE.L_DECISIONTREE))
			// Set background
			image(bg, 0, 0);
		else
			background(50);
		// Add and remove objects to the game
		for(GameObject g : removeQueue)
			objects.remove(g);
		for(GameObject g : objQueue)
			objects.put(new Integer(g.guid), g);
		// Clear queues
		objQueue.clear();
		removeQueue.clear();
		//if(graph != null)
			//graph.draw(this, translator);
		// Loop through all the objects
		for(GameObject g : objects.values()){
			// Do AI stuff if we can
			if(g.hasAI()){
				// Re-target flock AI if there is one and it is time
				if(((timer % TIMER) == 0) && (g.thinker instanceof FlockAI))
					g.thinker.retarget();
				g.runBehaviors();
				if(g.thinker.behaviorsStepDone())
					g.thinker.stepNextBehaviors();
				SimpleUpdater.update(g, Blender.blend(g.thinker.getBehaviorOutputs(), g.blendingMode));
			}
			// Update and draw
			if(g.hasUpdater())
				g.update();
		}
		
		// Check if the character is eaten
		if((controlledCharacter != null) && !((Actor) controlledCharacter).isAlive){
			// Stop the game and play the death animation
			if(deathScreen == null){
				((NormalAI) monster.thinker).clearActiveBehaviors();
				((NormalAI) monster.thinker).clearQueuedBehaviors();
				monster.setAccel(0, 0);
				monster.setVel(0, 0);
				((NormalAI) monster.thinker).clearPaths();
				backdrop.setFill(50, 50, 50, 150);
				controlledCharacter.setAccel(0, 0);
				controlledCharacter.setVel(0, 0);
				controlledCharacter.o = 0;
				controlledCharacter.angV = 0;
				controlledCharacter.angA = 0;
				((NormalAI) controlledCharacter.thinker).clearActiveBehaviors();
				((NormalAI) controlledCharacter.thinker).clearQueuedBehaviors();
				int[] fill = {255, 255, 255, 255};
				deathScreen = new TextAnimation(fill ,60, 60, 150, new PVector(220, 450), "IT'S LEVIOSAAAAAA (HE GOT YOU)");
				deathScreen.setScale(2.0f);
				animations.add(deathScreen);
			}
			// Reset the game when the animation is over
			else if(deathScreen.isDone){
				resetgame();
				backdrop.setFill(0, 0, 0, 0);
			}
		}
		
		// Draw objects by layer
		boolean seen = true;
		for(int currentLayer = 0 ; seen ; currentLayer++){
			seen = false;
			for(GameObject g : objects.values()){
				if(g.layer == currentLayer){
					g.draw();
					seen = true;
				}
			}
		}
		
		for(Iterator<Path> iterator = activePaths.iterator() ; iterator.hasNext() ;){
			Path path = iterator.next();
			if(path == null){
				iterator.remove();
				continue;
			}
			if(path.draw(this))
				iterator.remove();
		}
		for(Iterator<Animation> iterator = animations.iterator() ; iterator.hasNext();){
			Animation a = iterator.next();
			a.draw(this);
			if(a.isDone)
				iterator.remove();
		}
		// Step timer
		timer++;
	}
	
	// This method resets the game in behavior tree mode
	private void resetgame() {
		((NormalAI) monster.thinker).clearActiveBehaviors();
		((NormalAI) monster.thinker).clearQueuedBehaviors();
		((NormalAI) monster.thinker).clearOtherBehvaiors();
		// Reset and rebuild trees
		if(mode == SKETCHMODE.BEHAVIORTREE)
			((NormalAI) monster.thinker).addOtherBehavior(new MonsterBehaviorTree((Monster) monster, (Actor) controlledCharacter, graph, translator, this));
		else
			((NormalAI) monster.thinker).addOtherBehavior(new LearnedDecisionTree(learner.buildTree()));
		monster.setAccel(0, 0);
		monster.setVel(0, 0);
		monster.o = 0;
		monster.angV = 0;
		monster.angA = 0;
		monster.setPos(25, 25);
		((Monster) monster).isDancing = false;
		controlledCharacter.setPos(226.0f, 279.0f);
		((Actor) controlledCharacter).isAlive = true;
		controlledCharacter.setPos(WIDTH / 2, HEIGHT / 2);
		deathScreen = null;
		activePaths.clear();
	}

	/**
	 * This method allow for the adding of GameObjects while looping through them.
	 * @param g The GameObejct to add to the master list.
	 */
	public void addObject(GameObject g){
		objQueue.add(g);
	}
	
	
	/**
	 * This method allow for the removing of GameObjects while looping through them.
	 * @param g The GameObejct to remove from the master list.
	 */
	public void removeObject(GameObject g){
		removeQueue.add(g);
	}
	
	/*
	 * (non-Javadoc)
	 * @see processing.core.PApplet#mouseClicked(processing.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent event){
		// Arrive at mouse if in appropriate mode
		if(mode == SKETCHMODE.STEERINGARRIVE){
			GameObject o = new Target();
			o.setPos(event.getX(), event.getY());
			if((controlledCharacter != null) && (controlledCharacter.thinker instanceof NormalAI))
				((NormalAI) controlledCharacter.thinker).enqueueSteeringArrive(o, 0);
		}
		// Seek to mouse if in appropriate mode
		else if((mode == SKETCHMODE.FLOCKING) || (mode == SKETCHMODE.STEERINGSEEK)){
			GameObject o = new Target();
			o.setPos(event.getX(), event.getY());
			if((controlledCharacter != null) && (controlledCharacter.thinker instanceof NormalAI))
				((NormalAI) controlledCharacter.thinker).enqueueSteeringSeek(o, 0);
		}
		// Set up a controlled path-following character for certain demo modes
		else if((mode == SKETCHMODE.PATHFOLLOWING) || (mode == SKETCHMODE.BEHAVIORTREE) || (mode == SKETCHMODE.L_DECISIONTREE)){
			if((controlledCharacter != null) && (controlledCharacter.thinker instanceof NormalAI) && (((Actor) controlledCharacter).isAlive)){
				activePaths.add(((NormalAI) controlledCharacter.thinker).pathFollowTo(new Target(new PVector(event.getX(), event.getY())), PATHMODE.FORGET));
				int[] filler = {255, 0, 0, 255};
				animations.add(new Circle(filler, 0, 30, 90, translator.localize(translator.quantize(new PVector(event.getX(), event.getY())))));
			}
		}
	}
	
	
	// This method sets up the kinematic motion demo
	private void setupKinematic(){
		GameObject g = new Actor(this, guidMarker++);
		g.updater = u;
		g.thinker = new NormalAI(g);
		g.blendingMode = Blender.SIMPLE;
		g.setPos(20, HEIGHT - 20);
		((Actor) g).poopsBreadcrumbs = true;
		objects.put(new Integer(g.guid), g);
		
		Target[] targets = new Target[4];
		for(int i = 0 ; i < targets.length ; i++)
			targets[i] = new Target();
		targets[0].setPos(20, 20);
		targets[1].setPos(WIDTH - 20, 20);
		targets[2].setPos(WIDTH - 20, HEIGHT - 20);
		targets[3].setPos(20, HEIGHT - 20);
		
		for(int i = 0 ; i < targets.length ; i++)
			((NormalAI) g.thinker).enqueueKinematicArrive(targets[i], i);
	}
	
	
	// This method sets up the steering seek demo
		private void setupSteeringSeek(){
			GameObject g = new Actor(this, guidMarker++);
			g.updater = u;
			g.thinker = new NormalAI(g);
			g.blendingMode = Blender.SIMPLE;
			g.setPos(WIDTH / 2, HEIGHT / 2);
			((Actor) g).poopsBreadcrumbs = true;
			objects.put(new Integer(g.guid), g);
			controlledCharacter = g;
		}
	
	
	// This method sets up the steering arrive demo
	private void setupSteeringArrive(){
		GameObject g = new Actor(this, guidMarker++);
		g.updater = u;
		g.thinker = new NormalAI(g);
		g.blendingMode = Blender.SIMPLE;
		g.setPos(20, HEIGHT - 20);
		((Actor) g).poopsBreadcrumbs = true;
		objects.put(new Integer(g.guid), g);
		
		Target[] targets = new Target[4];
		for(int i = 0 ; i < targets.length ; i++)
			targets[i] = new Target();
		targets[0].setPos(20, 20);
		targets[1].setPos(WIDTH - 20, 20);
		targets[2].setPos(WIDTH - 20, HEIGHT - 20);
		targets[3].setPos(20, HEIGHT - 20);
		
		for(int i = 0 ; i < targets.length ; i++)
			((NormalAI) g.thinker).enqueueSteeringArrive(targets[i], i);
		controlledCharacter = g;
	}
	
	
	// This method sets up the steering wander demo
	private void setupSteeringWander(){
		GameObject g = new Actor(this, guidMarker++);
		g.updater = u;
		g.thinker = new NormalAI(g);
		g.blendingMode = Blender.SIMPLE;
		g.setPos(WIDTH / 2, HEIGHT / 2);
		((Actor) g).poopsBreadcrumbs = true;
		((NormalAI) g.thinker).steeringWander(-1);
		objects.put(new Integer(g.guid), g);
	}
	
	
	// This method sets up the flocking demo
	private void setupFlocking(){
		Flock flock = new Flock(this, 11);
		for(GameObject g: flock.members)
			objects.put(new Integer(g.guid), g);
		objects.put(new Integer(flock.leader.guid), flock.leader);
		controlledCharacter = flock.leader;
		((NormalAI) controlledCharacter.thinker).steeringWander(600);
	}
	
	// This method sets up the pathfinding demo
	private void setupPathFollowing() {
		graph = new Graph(new File("graphfiles/custom.graph"), true);
		translator = new Translator(graph, new File("graphfiles/custom.map"));
		PathFinding p = new PathFinding(translator);
		GameObject g = new Actor(this, guidMarker++);
		g.updater = u;
		g.thinker = new NormalAI(g, p);
		g.blendingMode = Blender.SIMPLE;
		g.setPos(WIDTH / 2, HEIGHT / 2);
		((Actor) g).poopsBreadcrumbs = true;
		objects.put(new Integer(g.guid), g);
		controlledCharacter = g;
	}
	
	// This method sets up the decision tree demo
	private void setupDecisionTree(){
		graph = new Graph(new File("graphfiles/custom.graph"), true);
		translator = new Translator(graph, new File("graphfiles/custom.map"));
		PathFinding p = new PathFinding(translator);
		GameObject g = new Face(this, guidMarker++);
		g.updater = u;
		g.thinker = new NormalAI(g, p);
		((NormalAI) g.thinker).addOtherBehavior(new SimpleDecisionTree((Actor) g, graph, translator, this));
		g.blendingMode = Blender.SIMPLE;
		g.setPos(WIDTH / 2, HEIGHT / 2);
		((Actor) g).poopsBreadcrumbs = true;
		objects.put(new Integer(g.guid), g);
	}
	
	private void setupBehaviorTree(){
		graph = new Graph(new File("graphfiles/custom.graph"), true);
		translator = new Translator(graph, new File("graphfiles/custom.map"));
		PathFinding p = new PathFinding(translator);
		GameObject g = new Monster(this, guidMarker++);
		g.setFill(0, 255, 0, 255);
		g.layer = 1;
		g.updater = u;
		g.thinker = new NormalAI(g, p);
		g.blendingMode = Blender.SIMPLE;
		g.setPos(10, 10);
		((Actor) g).poopsBreadcrumbs = true;
		monster = (Monster) g;
		objects.put(new Integer(g.guid), g);
		
		GameObject c = new Actor(this, guidMarker++);
		c.updater = u;
		c.thinker = new NormalAI(c, p);
		c.blendingMode = Blender.SIMPLE;
		c.setPos(220f, 280f);
		((Actor) c).poopsBreadcrumbs = true;
		objects.put(new Integer(c.guid), c);
		controlledCharacter = c;
		
		((NormalAI) g.thinker).addOtherBehavior(new MonsterBehaviorTree((Monster) g, (Actor) c, graph, translator, this));
		
		backdrop.setPos(210, 420);
		backdrop.setSize(new PVector(420, 40));
		objects.put(new Integer(backdrop.guid), backdrop);
		//BehaviorLog.setLogState(true);
	}
	
	// This method sets up for loading and running a learned decision tree for the monster
	private void setupLearningTree(){
		graph = new Graph(new File("graphfiles/custom.graph"), true);
		translator = new Translator(graph, new File("graphfiles/custom.map"));
		PathFinding p = new PathFinding(translator);
		GameObject g = new Monster(this, guidMarker++);
		g.setFill(0, 255, 0, 255);
		g.layer = 1;
		g.updater = u;
		g.thinker = new NormalAI(g, p);
		g.blendingMode = Blender.SIMPLE;
		g.setPos(10, 10);
		((Actor) g).poopsBreadcrumbs = true;
		monster = (Monster) g;
		objects.put(new Integer(g.guid), g);
		
		GameObject c = new Actor(this, guidMarker++);
		c.updater = u;
		c.thinker = new NormalAI(c, p);
		c.blendingMode = Blender.SIMPLE;
		c.setPos(220f, 280f);
		((Actor) c).poopsBreadcrumbs = true;
		objects.put(new Integer(c.guid), c);
		controlledCharacter = c;

		backdrop.setPos(210, 420);
		backdrop.setSize(new PVector(420, 40));
		objects.put(new Integer(backdrop.guid), backdrop);
		learner = new TreeLearner(monster, (Actor) controlledCharacter, graph, translator);
		// Load all the logs to learn from
		learner.loadLogs();
		((NormalAI) monster.thinker).addOtherBehavior(new LearnedDecisionTree(learner.buildTree()));
	}
}
