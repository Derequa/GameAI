package thinking.trees;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import graphs.Graph;
import graphs.Translator;
import model.Actor;
import model.Monster;
import thinking.trees.decisiontrees.nodes.DecisionTreeNode;
import thinking.trees.decisiontrees.nodes.DistanceEval;
import thinking.trees.decisiontrees.nodes.HungerEval;
import thinking.trees.decisiontrees.tasks.MonsterChase;
import thinking.trees.decisiontrees.tasks.MonsterEat;
import thinking.trees.decisiontrees.tasks.MonsterSeekFridge;
import thinking.trees.decisiontrees.tasks.MonsterWander;

/**
 * This class reads in data from log files and processes them to
 * help construct a decision tree for the monster.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class TreeLearner {

	/** A constant for the token indicating the seek player action */
	private static final String seekPlayer = "SEEK_PLAYER";
	/** A constant for the token indicating the seek fridge action */
	private static final String seekFridge = "SEEK_FRIDGE";
	/** A constant for the token indicating the eat player action */
	private static final String eatPlayer = "EAT_PLAYER";
	/** A constant for the token indicating the wander action */
	private static final String wander = "WANDER";
	/** A list of all the recorded distance values in order */
	private LinkedList<Float> distances = new LinkedList<Float>();
	/** A list of all the recorded hunger values in order */
	private LinkedList<Boolean> hungry = new LinkedList<Boolean>();
	/** A list of all the recorded actions in order */
	private LinkedList<String> action = new LinkedList<String>();
	/** The monster we will construct nodes with */
	private Monster monster;
	/** The player we will construct nodes with */
	private Actor player;
	/** The graph we will construct nodes with */
	private Graph graph;
	/** The translator we will construct nodes with */
	private Translator translator;
	
	/**
	 * This construct the learning object with the given objects to facilitate node
	 * and tree construction.
	 * @param monster The monster to build nodes with.
	 * @param player The player to build nodes.
	 * @param graph The graph to build nodes with.
	 * @param translator The translator to build nodes with.
	 */
	public TreeLearner(Monster monster, Actor player, Graph graph, Translator translator){
		this.monster = monster;
		this.player = player;
		this.graph = graph;
		this.translator = translator;
	}
	
	/**
	 * This loads all the available log files for processing.
	 */
	public void loadLogs(){
		boolean found = true;
		for(int i = 0 ; found ; i++){
			File f = new File(BehaviorLog.filename + i + ".txt");
			found = f.exists();
			if(found)
				readLog(f);
		}
	}
	
	/**
	 * This method processes all the examples arranges a decision tree
	 * according to the results of the information gaining process.
	 * @return The root node of the assembled decision tree.
	 */
	public DecisionTreeNode buildTree(){
		// Create a crap load of counters for doing the math on information gain
		int n_SeekPlayer = 0;
		int n_SeekFridge = 0;
		int n_EatPlayer = 0;
		int n_Wander = 0;
		
		int n_EatRange_SeekPlayer = 0;
		int n_EatRange_SeekFridge = 0;
		int n_EatRange_EatPlayer = 0;
		int n_EatRange_Wander = 0;
		
		int n_LookRange_SeekPlayer = 0;
		int n_LookRange_SeekFridge = 0;
		int n_LookRange_EatPlayer = 0;
		int n_LookRange_Wander = 0;
		
		int n_OutOfRange_SeekPlayer = 0;
		int n_OutOfRange_SeekFridge = 0;
		int n_OutOfRange_EatPlayer = 0;
		int n_OutOfRange_Wander = 0;
		
		int n_IsHungry_SeekPlayer = 0;
		int n_IsHungry_SeekFridge = 0;
		int n_IsHungry_EatPlayer = 0;
		int n_IsHungry_Wander = 0;
		
		int n_NotHungry_SeekPlayer = 0;
		int n_NotHungry_SeekFridge = 0;
		int n_NotHungry_EatPlayer = 0;
		int n_NotHungry_Wander = 0;
		
		int total = 0;
		
		int n_EatRange = 0;
		int n_LookRange = 0;
		int n_OutOfRange = 0;
		
		int n_IsHungry = 0;
		int n_NotHungry = 0;
		
		Iterator<Float> distanceIterator = distances.iterator();
		Iterator<Boolean> hungerIterator = hungry.iterator();
		Iterator<String> actionIterator = action.iterator();
		
		// Loop through all the data and count occurrences
		while(distanceIterator.hasNext() && hungerIterator.hasNext() && actionIterator.hasNext()){
			float currentDist = distanceIterator.next();
			boolean currentHunger = hungerIterator.next();
			String currentAction = actionIterator.next();
			
			if(currentDist <= Monster.EATRADIUS)
				n_EatRange++;
			else if(currentDist <= Monster.LOOKRADIUS)
				n_LookRange++;
			else
				n_OutOfRange++;
			
			if(currentHunger)
				n_IsHungry++;
			else
				n_NotHungry++;
			
			switch(currentAction){
				case seekPlayer:	if(currentDist <= Monster.EATRADIUS)
										n_EatRange_SeekPlayer++;
									else if(currentDist <= Monster.LOOKRADIUS)
										n_LookRange_SeekPlayer++;
									else
										n_OutOfRange_SeekPlayer++;
				
									if(currentHunger)
										n_IsHungry_SeekPlayer++;
									else
										n_NotHungry_SeekPlayer++;
				
									n_SeekPlayer++;
									break;
				case seekFridge:	if(currentDist <= Monster.EATRADIUS)
										n_EatRange_SeekFridge++;
									else if(currentDist <= Monster.LOOKRADIUS)
										n_LookRange_SeekFridge++;
									else
										n_OutOfRange_SeekFridge++;
				
									if(currentHunger)
										n_IsHungry_SeekFridge++;
									else
										n_NotHungry_SeekFridge++;
				
									n_SeekFridge++;
									break;
				case wander:		if(currentDist <= Monster.EATRADIUS)
										n_EatRange_Wander++;
									else if(currentDist <= Monster.LOOKRADIUS)
										n_LookRange_Wander++;
									else
										n_OutOfRange_Wander++;
				
									if(currentHunger)
										n_IsHungry_Wander++;
									else
										n_NotHungry_Wander++;		
				
									n_Wander++;
									break;
				case eatPlayer:		if(currentDist <= Monster.EATRADIUS)
										n_EatRange_EatPlayer++;
									else if(currentDist <= Monster.LOOKRADIUS)
										n_LookRange_EatPlayer++;
									else
										n_OutOfRange_EatPlayer++;
				
									if(currentHunger)
										n_IsHungry_EatPlayer++;
									else
										n_NotHungry_EatPlayer++;
				
									n_EatPlayer++;
									break;
				default:			break;
			}
			
			total++;
		}
		
		// Calculate the entropy before
		double e_Before = (-((double) n_SeekPlayer / total) * ((Math.log10((double) n_SeekPlayer / total) / Math.log(2.0))));
		e_Before += (-((double) n_SeekFridge / total) * ((Math.log10((double) n_SeekFridge / total) / Math.log(2.0))));
		e_Before += (-((double) n_EatPlayer / total) * ((Math.log10((double) n_EatPlayer / total) / Math.log(2.0))));
		e_Before += (-((double) n_Wander / total) * ((Math.log10((double) n_Wander / total) / Math.log(2.0))));
		
		// Calculate the entropy related to the eat-range distance value
		double e_EatDist = eCalc(n_EatRange_SeekPlayer, n_EatRange);
		e_EatDist += eCalc(n_EatRange_SeekFridge, n_EatRange);
		e_EatDist += eCalc(n_EatRange_EatPlayer, n_EatRange);
		e_EatDist += eCalc(n_EatRange_Wander, n_EatRange);
		
		// Calculate the entropy related to the chasing-range distance value
		double e_LookDist = eCalc(n_LookRange_SeekPlayer, n_LookRange);
		e_LookDist += eCalc(n_LookRange_SeekFridge, n_LookRange);
		e_LookDist += eCalc(n_LookRange_EatPlayer, n_LookRange);
		e_LookDist += eCalc(n_LookRange_Wander, n_LookRange);
		
		// Calculate the entropy related to the out-of-range distance value
		double e_FarDist = eCalc(n_OutOfRange_SeekPlayer, n_OutOfRange);
		e_FarDist += eCalc(n_OutOfRange_SeekFridge, n_OutOfRange);
		e_FarDist += eCalc(n_OutOfRange_EatPlayer, n_OutOfRange);
		e_FarDist += eCalc(n_OutOfRange_Wander, n_OutOfRange);
		
		// Calculate the entropy related to a true hunger value
		double e_Hungry = eCalc(n_IsHungry_SeekPlayer, n_IsHungry);
		e_Hungry += eCalc(n_IsHungry_SeekFridge, n_IsHungry);
		e_Hungry += eCalc(n_IsHungry_EatPlayer, n_IsHungry);
		e_Hungry += eCalc(n_IsHungry_Wander, n_IsHungry);
		
		// Calculate the entropy related to a false hunger value
		double e_NotHungry = eCalc(n_NotHungry_SeekPlayer, n_NotHungry);
		e_NotHungry += eCalc(n_NotHungry_SeekFridge, n_NotHungry);
		e_NotHungry += eCalc(n_NotHungry_EatPlayer, n_NotHungry);
		e_NotHungry += eCalc(n_NotHungry_Wander, n_NotHungry);
		
		// Calculate the entropy after for distance and hunger
		double e_DistAfter = e_EatDist + e_LookDist + e_FarDist;
		double e_HungryAfter = e_Hungry + e_NotHungry;
		
		// Calculate the information gain for distance and hunger
		double ig_Dist = e_Before - e_DistAfter;
		double ig_Hungry = e_Before - e_HungryAfter;
		
		// Assemble basic decision tree components
		DecisionTreeNode root = null;
		DistanceEval distNode = new DistanceEval(monster, player);
		HungerEval hungerNode = new HungerEval(monster);
		
		distNode.addChild(new MonsterEat(monster, player), DistanceEval.EAT_ACTION);
		distNode.addChild(new MonsterChase(monster, player), DistanceEval.CHASE_ACTION);
		
		hungerNode.addChild(new MonsterSeekFridge(monster, graph, translator), HungerEval.FRIDGE_ACTION);
		hungerNode.addChild(new MonsterWander(monster, graph, translator), HungerEval.WANDER_ACTION);
		
		// Sort the root and ordering based on information gain
		if(ig_Dist > ig_Hungry){
			distNode.addChild(hungerNode);
			root = distNode;
		}
		else{
			hungerNode.addChild(distNode);
			root = hungerNode;
		}
		
		return root;
	}
	
	/**
	 * This helper method performs the math related to entropy calculation.
	 * @param n1 The numerator for the probability.
	 * @param n2 The denominator for the probability.
	 * @return The entropy calculated.
	 */
	private double eCalc(int n1, int n2){
		double res = (-((double) n1 / n2) * ((Math.log10((double) n1 / n2) / Math.log(2.0))));
		if(Double.isNaN(res) || Double.isInfinite(res) || (res == -0.0))
			return 0.0;
		return res;
	}
	
	/**
	 * This helper method lets us read in a given log file.
	 * @param f
	 */
	private void readLog(File f){
		Scanner s = null;
		try {
			s = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(s.hasNextLine()){
			if(!s.hasNext()){
				s.close();
				return;
			}
			if(!s.hasNextFloat()){
				s.close();
				throw new InvalidLogException("Expected float!");
			}
			float loggedDistance = s.nextFloat();
			
			if(!s.hasNextBoolean()){
				s.close();
				throw new InvalidLogException("Expected boolean!");
			}
			boolean isHungry = s.nextBoolean();
			
			if(!s.hasNext()){
				s.close();
				throw new InvalidLogException("Expected action token!");
			}
			String actionToken = s.next();
			
			switch(actionToken){
				case seekPlayer:	break;
				case seekFridge:	break;
				case wander:		break;
				case eatPlayer:		break;
				default:			s.close();
									throw new InvalidLogException("Invalid action token!");
			}
			
			distances.addLast(loggedDistance);
			hungry.addLast(isHungry);
			action.addLast(actionToken);
		}
	}
}
