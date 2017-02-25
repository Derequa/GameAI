package thinking.trees.decisiontrees;

import graphs.Graph;
import graphs.Translator;
import manager.Sketch;
import model.Actor;
import model.GameObject;
import model.SimpleRectangle;
import model.SimpleText;
import model.Target;
import model.TextAnimation;
import processing.core.PVector;
import thinking.Behavior;
import thinking.NormalAI;
import thinking.Output;
import thinking.NormalAI.PATHMODE;
import thinking.paths.Path;

/**
 * This class implements a very simple decision tree for a character's AI.
 * The character roughly simulates a computer science student at his/her home. It tries to go to it's computer, but
 * accrues levels of needs for sleeping, eating, and going to the bathroom. It will path-find to
 * different places in the game world to satisfy those needs.
 * @author Derek Batts
 *
 */
public class SimpleDecisionTree extends Behavior {
	
	/** The vertex ID for where the toilet is */
	private static final int ID_TOILET = 97;
	/** The vertex ID for where the computer is */
	private static final int ID_COMPUTER = 5;
	/** The vertex ID for where the refrigerator is */
	private static final int ID_FRIDGE = 42;
	/** The vertex ID for where the Bed is */
	private static final int ID_BED = 19;
	/** The point at which the character will try to get food */
	private static final int HUNGER_THRESHOLD = 50;
	/** The point at which the character will try to go to the bathroom */
	private static final int BATHROOM_THRESHOLD = 70;
	/** The point at which the character will try to go to bed */
	private static final int SLEEP_THRESHOLD = 110;
	/** The cool down for how much to decrease the sleep need while the character is sleeping */
	private static final int SLEEP_STEPDOWN = 1;
	/** The cool down for how much to decrease the hunger need while the character is eating */
	private static final int HUNGER_STEPDOWN = 1;
	/** The cool down for how much to decrease the bathroom need while the character is using the bathroom */
	private static final int BATHROOM_STEPDOWN = 1;
	/** A notification string for when the character is going to work at the computer */
	private static final String msg_Computer = "Going to work at computer...";
	/** A notification string for when the character is going to get food */
	private static final String msg_Fridge = "Getting food...";
	/** A notification string for when the character is going to the restroom */
	private static final String msg_Bathroom = "Using the restroom...";
	/** A notification string for when the character is going to bed */
	private static final String msg_Sleep = "Going to bed...";
	
	/** The position to draw messages */
	private static final PVector msg_Position = new PVector(15, 150);
	/** The position for the hunger bar */
	private static final PVector hungerBarPosition = new PVector(15, 40);
	/** The position for the sleep bar */
	private static final PVector sleepBarPosition = new PVector(15, 80);
	/** The position for the bathroom bar */
	private static final PVector bathroomBarPosition = new PVector(15, 120);
	/** The max size for each bar */
	private static final PVector barMaxSize = new PVector(100, 10);
	
	/** A text header for the hunger bar */
	private static SimpleText hungerText;
	/** A text header for the sleep bar */
	private static SimpleText sleepText;
	/** A text header for the bathroom bar */
	private static SimpleText bathroomText;
	/** A bar to backdrop the hunger bar and show when it's empty */
	private static SimpleRectangle hungerBackdrop;
	/** A bar to backdrop the sleep bar and show when it's empty */
	private static SimpleRectangle sleepBackdrop;
	/** A bar to backdrop the bathroom bar and show when it's empty */
	private static SimpleRectangle bathroomBackdrop;
	/** A bar to display the current hunger level */
	private static SimpleRectangle hungerBar;
	/** A bar to display the current sleepy level */
	private static SimpleRectangle sleepBar;
	/** A bar to display the current potty level */
	private static SimpleRectangle bathroomBar;
	
	/** The actor this tree is controlling */
	private Actor actor;
	/** The translator for the given graph */
	private Translator translator;
	/** The parent sketch to draw to */
	private Sketch parent;
	
	/** The target object representing the where the toilet is in the game world */
	private Target t_Toilet;
	/** The target object representing where the computer is in the game world */
	private Target t_Computer;
	/** The target object representing where the refrigerator is in the game world */
	private Target t_Fridge;
	/** The target object representing where the bed is in the game world */
	private Target t_Bed;
	
	/** The path the character is currently following */
	private Path currentPath;
	
	/** The level of hunger for the character */
	private int hunger = 0;
	/** The level of bathroom need for the character */
	private int bathroom = 0;
	/** The level of sleepiness for the character */
	private int sleep = 0;
	
	/** A state flag for whether or not the character is sleeping */
	private boolean isSleeping = false;
	/** A state flag for whether or not the character is using the bathroom */
	private boolean usingBathroom = false;
	/** A state flag for whether or not the character is eating */
	private boolean isEating = false;
	/** A state flag for whether or not the character is headed to bed */
	private boolean goingToBed = false;
	/** A state flag for whether or not the character is going to the bathroom */
	private boolean goingToBathroom = false;
	/** A state flag for whether or not the character is getting food */
	private boolean gettingFood = false;
	
	/**
	 * This constructs a simple decision tree behavior.
	 * @param a The actor to control.
	 * @param g The graph to use.
	 * @param t The translator for the given graph.
	 * @param parent The parent sketch to draw in.
	 */
	public SimpleDecisionTree(Actor a, Graph g, Translator t, Sketch parent){
		// Check for null parameters
		if((a == null) || (g == null) || (t == null) || (parent == null))
			throw new IllegalArgumentException("NULL arguments in SimpleDecisionTree construction");
		// Check for the supported AI type
		if(!a.hasAI() || !(((GameObject)a).thinker instanceof NormalAI))
			throw new IllegalArgumentException("Actor requires NormalAI component for SimpleDecisionTree");
		// Set fields
		actor = a;
		translator = t;
		this.parent = parent;
		
		// Get targets from vertices
		t_Toilet = new Target(translator.localize(g.getVertex(ID_TOILET)));
		t_Computer = new Target(translator.localize(g.getVertex(ID_COMPUTER)));
		t_Fridge = new Target(translator.localize(g.getVertex(ID_FRIDGE)));
		t_Bed = new Target(translator.localize(g.getVertex(ID_BED)));
		
		// Construct and setup text headers
		hungerText = new SimpleText(parent, parent.guidMarker++, "Hunger Level:");
		sleepText = new SimpleText(parent, parent.guidMarker++, "Sleepiness Level:");
		bathroomText = new SimpleText(parent, parent.guidMarker++, "Bathroom-need Level:");
		hungerText.setPos(hungerBarPosition.x, hungerBarPosition.y - 10);
		sleepText.setPos(sleepBarPosition.x, sleepBarPosition.y - 10);
		bathroomText.setPos(bathroomBarPosition.x, bathroomBarPosition.y - 10);
		// Set fills for text
		hungerText.setFill(255, 255, 255, 255);
		sleepText.setFill(255, 255, 255, 255);
		bathroomText.setFill(255, 255, 255, 255);
		
		// Construct all bars
		hungerBackdrop = new SimpleRectangle(parent, parent.guidMarker++, barMaxSize);
		sleepBackdrop = new SimpleRectangle(parent, parent.guidMarker++, barMaxSize);
		bathroomBackdrop = new SimpleRectangle(parent, parent.guidMarker++, barMaxSize);
		hungerBar = new SimpleRectangle(parent, parent.guidMarker++, new PVector(100 * ((float) hunger / HUNGER_THRESHOLD), barMaxSize.y));
		sleepBar = new SimpleRectangle(parent, parent.guidMarker++, new PVector(100 * ((float) sleep / SLEEP_THRESHOLD), barMaxSize.y));
		bathroomBar = new SimpleRectangle(parent, parent.guidMarker++, new PVector(100 * ((float) bathroom / BATHROOM_THRESHOLD), barMaxSize.y));
		// Set positions for bars
		hungerBackdrop.setPos(hungerBarPosition.x, hungerBarPosition.y);
		sleepBackdrop.setPos(sleepBarPosition.x, sleepBarPosition.y);
		bathroomBackdrop.setPos(bathroomBarPosition.x, bathroomBarPosition.y);
		hungerBar.setPos(hungerBarPosition.x, hungerBarPosition.y);
		sleepBar.setPos(sleepBarPosition.x, sleepBarPosition.y);
		bathroomBar.setPos(bathroomBarPosition.x, bathroomBarPosition.y);
		// Set fills for bars
		hungerBackdrop.setFill(255, 0, 0, 255);
		sleepBackdrop.setFill(255, 0, 0, 255);
		bathroomBackdrop.setFill(255, 0, 0, 255);
		hungerBar.setFill(0, 255, 0, 255);
		sleepBar.setFill(0, 255, 0, 255);
		bathroomBar.setFill(0, 255, 0, 255);
		
		// Set layers
		hungerText.layer = 1;
		sleepText.layer = 1;
		bathroomText.layer = 1;
		hungerBackdrop.layer = 1;
		sleepBackdrop.layer = 1;
		bathroomBackdrop.layer = 1;
		hungerBar.layer = 2;
		sleepBar.layer = 2;
		bathroomBar.layer = 2;
		
		// Create backdrop to make bars and text more visible
		SimpleRectangle backdrop = new SimpleRectangle(parent, parent.guidMarker++, new PVector((barMaxSize.x + 60), (6 * (barMaxSize.y + 10) + 25)));
		backdrop.setFill(50, 50, 50, 150);
		backdrop.setPos(hungerText.position.x - 5, hungerText.position.y - 15);
		
		// Add all objects to sketch
		parent.addObject(backdrop);
		parent.addObject(hungerText);
		parent.addObject(sleepText);
		parent.addObject(bathroomText);
		parent.addObject(hungerBackdrop);
		parent.addObject(sleepBackdrop);
		parent.addObject(bathroomBackdrop);
		parent.addObject(hungerBar);
		parent.addObject(sleepBar);
		parent.addObject(bathroomBar);
	}

	/*
	 * (non-Javadoc)
	 * @see thinking.Behavior#step()
	 */
	@Override
	public Output step() {
		// Resize status bars
		hungerBar.setSize(new PVector(100 * ((float) hunger / HUNGER_THRESHOLD), barMaxSize.y));
		sleepBar.setSize(new PVector(100 * ((float) sleep / SLEEP_THRESHOLD), barMaxSize.y));
		bathroomBar.setSize(new PVector(100 * ((float) bathroom / BATHROOM_THRESHOLD), barMaxSize.y));
		// Check if we are at the end of the path
		if(currentPath != null){
			if(!(((NormalAI) actor.thinker).getActivePath() == null) || (actor.velocity.mag() > .1f))
				return null;
			// Change flags if we finished a path
			else {
				if(goingToBed){
					goingToBed = false;
					isSleeping = true;
				}
				else if(gettingFood){
					gettingFood = false;
					isEating = true;
				}
				else if(goingToBathroom){
					goingToBathroom = false;
					usingBathroom = true;
				}
				currentPath = null;
			}
		}
		// Decrement the sleeper timer if we are sleeping
		else if(isSleeping){
			sleep -= SLEEP_STEPDOWN;
			if(sleep <= 0)
				isSleeping = false;
		}
		// Decrement the eating timer if we are eating
		else if(isEating){
			hunger -= HUNGER_STEPDOWN;
			if(hunger <= 0)
				isEating = false;
		}
		// Decrement the bathroom timer if we are on the toilet
		else if(usingBathroom){
			bathroom -= BATHROOM_STEPDOWN;
			if(bathroom <= 0)
				usingBathroom = false;
		}
		// Check if we need to go eat
		else if(hunger >= HUNGER_THRESHOLD){
			currentPath = ((NormalAI) actor.thinker).pathFollowTo(t_Fridge, PATHMODE.FORGET);
			gettingFood = true;
			playMessage(msg_Fridge);
		}
		// Check if we need to sleep
		else if(sleep >= SLEEP_THRESHOLD){
			currentPath = ((NormalAI) actor.thinker).pathFollowTo(t_Bed, PATHMODE.FORGET);
			goingToBed = true;
			playMessage(msg_Sleep);
		}
		// Check if we need to use the restroom
		else if(bathroom >= BATHROOM_THRESHOLD){
			currentPath = ((NormalAI) actor.thinker).pathFollowTo(t_Toilet, PATHMODE.FORGET);
			goingToBathroom = true;
			playMessage(msg_Bathroom);
		}
		// If nothing else, check if we are at the computer, if not head there
		else if(PVector.dist(actor.position, t_Computer.position) > 75.0f){
			currentPath = ((NormalAI) actor.thinker).pathFollowTo(t_Computer, PATHMODE.FORGET);
			playMessage(msg_Computer);
		}
		// Increment needs
		else{
			hunger++;
			sleep++;
			bathroom++;
		}
		return null;
	}

	/**
	 * A helper method to make it easier to play text animations in the parent sketch.
	 * @param msg The text message to play.
	 */
	private void playMessage(String msg) {
		int[] fill = {255, 255, 255, 255};
		parent.animations.add(new TextAnimation(fill, 15, 15, 50, msg_Position, msg));
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
