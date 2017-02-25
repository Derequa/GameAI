package thinking;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import graphs.Vertex;
import thinking.kinematic.Kinematic;
import thinking.kinematic.KinematicArrive;
import thinking.kinematic.KinematicFace;
import thinking.kinematic.KinematicSeek;
import thinking.kinematic.KinematicWander;
import thinking.paths.Path;
import thinking.paths.PathFinding;
import thinking.steering.Steering;
import thinking.steering.SteeringAlign;
import thinking.steering.SteeringArrive;
import thinking.steering.SteeringSeek;
import thinking.steering.SteeringWander;
import model.GameObject;
import model.Monster;
import model.Target;

/**
 * This class models a generic AI component for GameObjects.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class NormalAI implements AI{
	
	public enum PATHMODE{
		FORGET,
		PREEMPT,
		QUEUE
	}
	
	/** The number of implemented behaviors */
	private static final int NUM_BEHAVIORS = 8;
	/** ID flag for Kinematic Face */
	private static final int K_FACE = 0;
	/** ID flag for Kinematic Seek */
	private static final int K_SEEK = 1;
	/** ID flag for Kinematic Wander */
	private static final int K_WANDER = 2;
	/** ID flag for Kinematic Arrive */
	private static final int K_ARRIVE = 3;
	/** ID flag for Steering Align */
	private static final int S_ALIGN = 4;
	/** ID flag for Steering Seek */
	private static final int S_SEEK = 5;
	/** ID flag for Steering Wander */
	private static final int S_WANDER = 6;
	/** ID flag for Steering Arrive */
	private static final int S_ARRIVE = 7;
	
	/** This maps target objects to behaviors for each behavior type */
	@SuppressWarnings("unchecked")
	private HashMap<GameObject, Behavior>[] targetMaps = new HashMap[NUM_BEHAVIORS];
	/** An array of queues of sets of waiting behaviors for each type for behavior */
	@SuppressWarnings("unchecked")
	private LinkedList<LinkedList<GameObject>>[] bigQueue = new LinkedList[NUM_BEHAVIORS];
	/** A list of outputs for all behaviors to blend */
	private LinkedList<Output> outputs = new LinkedList<Output>();
	/** The character this AI is tied to */
	protected GameObject g;
	/** The queue of targets to path-follow to */
	protected LinkedList<Target> queuedPathTargets = new LinkedList<Target>();
	/** The current path node/target */
	private Target currentPathNode = null;
	/** The current path we are on */
	private Path currentPath = null;
	/** The path finding component tied to this AI component */
	protected PathFinding pathFinder = null;
	/** A list of irregular behaviors the AI should be running */
	private LinkedList<Behavior> otherBehaviors = new LinkedList<Behavior>();
	
	
	/**
	 * This constructs a NormalAi component for a given GameObject.
	 * @param g The character this AI is tied to.
	 */
	public NormalAI(GameObject g){
		this(g, null);
	}
	
	/**
	 * This constructs a NormalAi component for a given GameObject.
	 * @param g The character this AI is tied to.
	 * @param p The PathFinding component to use for this AI instance.
	 */
	public NormalAI(GameObject g, PathFinding p){
		pathFinder = p;
		// Setup maps and queues
		for(int i = 0 ; i < NUM_BEHAVIORS ; i++){
			targetMaps[i] = new HashMap<GameObject, Behavior>();
			bigQueue[i] = new LinkedList<LinkedList<GameObject>>();
		}
		this.g = g;
	}
	
	/**
	 * This method will tell this AI instance to find a path to the
	 * given target and follow it. It also gives you the option to forget
	 * the current path, preempt it, or queue the new path.
	 * (forget mode is the only supported mode as of now)
	 * @param t The Target to find a path to.
	 * @param queueMode How should we handle the new path and the current path?
	 * @return The new path made (if any).
	 */
	public Path pathFollowTo(Target t, PATHMODE queueMode){
		if(pathFinder == null)
			return null;
		switch(queueMode){
			case FORGET:	if(currentPath != null){
								// Fade away the current path
								if(!(g instanceof Monster)){
									g.parent.animations.addAll(currentPath.fadePath());
									g.parent.activePaths.remove(currentPath);
								}
							}
							// Clear any queued paths
							queuedPathTargets.clear();
							// Immediately find a path to the target and follow it
							doPathNow(t);
							break;
			case PREEMPT:	/**if(currentPathNode != null)
								queuedPathTargets.addFirst(currentPathNode);
							doPathNow(t);*/
							break;
			case QUEUE:		//queuedPathTargets.addLast(t);
							break;
			default:		break;
		}
		// Re-target all active behaviors
		retarget();
		return currentPath;
	}
	
	/**
	 * This gets the current path the character is following.
	 * @return The current path.
	 */
	public Path getActivePath(){
		return currentPath;
	}
	
	/**
	 * This clears all behaviors queued for this character.
	 */
	public void clearQueuedBehaviors(){
		for(int i = 0 ; i < bigQueue.length ; i++){
			for(LinkedList<GameObject> list : bigQueue[i])
				list.clear();
		}
	}
	
	/**
	 * This clears all active behaviors for this character.
	 */
	public void clearActiveBehaviors(){
		for(int i = 0 ; i < targetMaps.length ; i++)
			targetMaps[i].clear();
	}
	
	/**
	 * This clears the active behaviors tree stuffs.
	 */
	public void clearOtherBehvaiors(){
		otherBehaviors.clear();
	}
	
	/* (non-Javadoc)
	 * @see thinking.AI#retarget()
	 */
	@Override
	public void retarget(){
		// Update behavior for each active target
		for(int i = 0 ; i < NUM_BEHAVIORS ; i++)
			retargetList(targetMaps[i].keySet(), i);
		
	}
	
	/* (non-Javadoc)
	 * @see thinking.AI#runBehaviors()
	 */
	@Override
	public boolean runBehaviors(){
		for(int i = 0 ; i < NUM_BEHAVIORS ; i++)
			doBehaviorFor(targetMaps[i].values(), i);
		doBehaviorFor(otherBehaviors, -1);
		return behaviorsStepDone();
	}
	
	/* (non-Javadoc)
	 * @see thinking.AI#behaviorsStepDone()
	 */
	@Override
	public boolean behaviorsStepDone(){
		boolean res = true;
		for(int i = 0 ; i < NUM_BEHAVIORS ; i++)
			res = res && targetMaps[i].isEmpty();
		return res;
	}
	
	/* (non-Javadoc)
	 * @see thinking.AI#stepNextBehaviors()
	 */
	@Override
	public void stepNextBehaviors(){
		for(int i = 0 ; i < NUM_BEHAVIORS ; i++){
			if(!bigQueue[i].isEmpty())
				retargetList(bigQueue[i].removeFirst(), i);
		}
	}
	
	/* (non-Javadoc)
	 * @see thinking.AI#getBehaviorOutputs()
	 */
	@Override
	public Collection<Output> getBehaviorOutputs(){
		return outputs;
	}

	
	/**
	 * This method removes a behavior from the active targets.
	 * @param b The behavior to remove.
	 */
	public void removeBehavior(Behavior b){
		if(b instanceof KinematicFace)
			targetMaps[K_FACE].remove(b.getTarget());
		else if(b instanceof KinematicSeek)
			targetMaps[K_SEEK].remove(b.getTarget());
		else if(b instanceof KinematicWander)
			targetMaps[K_WANDER].remove(b.getTarget());
		else if(b instanceof KinematicArrive)
			targetMaps[K_ARRIVE].remove(b.getTarget());
		else if(b instanceof SteeringAlign)
			targetMaps[S_ALIGN].remove(b.getTarget());
		else if(b instanceof SteeringSeek)
			targetMaps[S_SEEK].remove(b.getTarget());
		else if(b instanceof SteeringWander)
			targetMaps[S_WANDER].remove(b.getTarget());
		else if(b instanceof SteeringArrive)
			targetMaps[S_ARRIVE].remove(b.getTarget());
		else{
			// TODO test dis (dis hacky stuff)
			for(Iterator<Behavior> i = otherBehaviors.iterator() ; i.hasNext();){
				Behavior b0 = i.next();
				if(b == b0)
					i.remove();
			}
		}
	}
	
	public void addOtherBehavior(Behavior b){
		otherBehaviors.add(b);
	}

	
	// ------------------------------------------------
	// -------------| Behavior Requests |--------------
	// ------------------------------------------------
	
	
	// Kinematic Behavior Requests
	
	
	/**
	 * This method adds a KinematicFace behavior to the active behavior list.
	 * @param target The target to face.
	 */
	public void kinematicFace(GameObject target){
		Behavior b = Kinematic.face(target, g);
		targetMaps[K_FACE].put(target, b);
	}
	
	
	/**
	 * This method adds a KinematicFace behavior to the active behavior list.
	 * @param angle The angle to face.
	 */
	public void kinematicFace(float angle){
		Behavior b = Kinematic.face(g, angle);
		targetMaps[K_FACE].put(g, b);
	}
	
	
	/**
	 * This method adds a KinematicSeek behavior to the active behavior list.
	 * @param target The target to seek.
	 */
	public void kinematicSeek(GameObject target){
		Behavior b = Kinematic.seek(target, g);
		targetMaps[K_SEEK].put(target, b);
	}
	
	
	/**
	 * This method adds a KinematicWander behavior to the active behavior list.
	 */
	public void kinematicWander(){
		Behavior b = Kinematic.wander(g);
		targetMaps[K_WANDER].put(g, b);
	}
	
	public void kinematicArrive(GameObject target){
		Behavior b = Kinematic.arrive(target, g);
		targetMaps[K_ARRIVE].put(target, b);
	}
	
	
	// Steering Behavior Requests
	
	
	/**
	 * This method adds a SteeringAlign behavior to the active behavior list.
	 * @param target The target to align to.
	 */
	public void steeringAlign(GameObject target){
		Behavior b = Steering.align(target, g);
		targetMaps[S_ALIGN].put(target, b);
	}
	
	
	/**
	 * This method adds a SteeringSeek behavior to the active behavior list.
	 * @param target The target to seek.
	 */
	public void steeringSeek(GameObject target){
		Behavior b = Steering.seek(target, g);
		targetMaps[S_SEEK].put(target, b);
	}
	
	
	/**
	 * This method adds a SteeringSeek behavior to the active behavior list.
	 * @param s The pre-defined SteeringSeek behavior to add.
	 */
	public void steeringSeek(SteeringSeek s){
		targetMaps[S_SEEK].put(s.getTarget(), s);
	}
	
	
	/**
	 * This method adds a SteeringWander behavior to the active behavior list.
	 * @param time The time to Wander.
	 */
	public void steeringWander(int time){
		Behavior b = Steering.wander(g, time);
		targetMaps[S_WANDER].put(g, b);
	}
	
	
	/**
	 * This method adds a SteeringArrive behavior to the active behavior list.
	 * @param target The target to arrive at.
	 */
	public void steeringArrive(GameObject target){
		Behavior b = Steering.arrive(target, g);
		targetMaps[S_ARRIVE].put(target, b);
	}
	
	
	// ------------------------------------------------
	// --------------| Behavior Queuing |--------------
	// ------------------------------------------------
	
	
	// Kinematic Behavior Queuing
	
	
	/**
	 * This method adds a KinematicFace behavior to the behavior set queue.
	 * @param target The target to face.
	 * @param delay How many steps ahead to queue this behavior.
	 */
	public void enqueueKinematicFace(GameObject target, int delay){
		if(delay < 1) kinematicFace(target);
		else enqueueTarget(bigQueue[K_FACE], target, delay);
	}
	
	
	/**
	 * This method adds a KinematicSeek behavior to the behavior set queue.
	 * @param target The target to seek.
	 * @param delay How many steps ahead to queue this behavior.
	 */
	public void enqueueKinematicSeek(GameObject target, int delay){
		if(delay < 1) kinematicSeek(target);
		else enqueueTarget(bigQueue[K_SEEK], target, delay);
	}
	
	
	/**
	 * This method adds a KinematicWander behavior to the behavior set queue.
	 * @param target Useless parameter.
	 * @param delay How many steps ahead to queue this behavior.
	 */
	public void enqueueKinematicWander(GameObject target, int delay){
		if(delay < 1) kinematicWander();
		else enqueueTarget(bigQueue[K_WANDER], target, delay);
	}
	
	
	/**
	 * This method adds a KinematicArrive behavior to the behavior set queue.
	 * @param target The target to arrive at.
	 * @param delay How many steps ahead to queue this behavior.
	 */
	public void enqueueKinematicArrive(GameObject target, int delay){
		if(delay < 1) kinematicArrive(target);
		else enqueueTarget(bigQueue[K_ARRIVE], target, delay);
	}
	
	
	// Steering Behavior Queuing
	
	
	/**
	 * This method adds a SteeringAlign behavior to the behavior set queue.
	 * @param target The target to align with.
	 * @param delay How many steps ahead to queue this behavior.
	 */
	public void enqueueSteeringAlign(GameObject target, int delay){
		if(delay < 1) steeringAlign(target);
		else enqueueTarget(bigQueue[S_ALIGN], target, delay);
	}
	
	
	/**
	 * This method adds a SteeringSeek behavior to the behavior set queue.
	 * @param target The target to seek.
	 * @param delay How many steps ahead to queue this behavior.
	 */
	public void enqueueSteeringSeek(GameObject target, int delay){
		if(delay < 1) steeringSeek(target);
		else enqueueTarget(bigQueue[S_SEEK], target, delay);
	}
	
	
	/**
	 * This method adds a SteeringWander behavior to the behavior set queue.
	 * @param target Useless parameter.
	 * @param delay How many steps ahead to queue this behavior.
	 */
	public void enqueueSteeringWander(GameObject target, int time, int delay){
		if(delay < 1) steeringWander(time);
		else enqueueTarget(bigQueue[S_WANDER], target, delay);
	}
	
	
	/**
	 * This method adds a SteeringArrive behavior to the behavior set queue.
	 * @param target The target to arrive at.
	 * @param delay How many steps ahead to queue this behavior.
	 */
	public void enqueueSteeringArrive(GameObject target, int delay){
		if(delay < 1) steeringArrive(target);
		else enqueueTarget(bigQueue[S_ARRIVE], target, delay);
	}
	
	
	// ------------------------------------------------
	// ---------------| Helper Methods |---------------
	// ------------------------------------------------
	
	
	// A method for re-targeting all the targets in a given list and key
	private void retargetList(Collection<GameObject> keys, int switcher){
		for(GameObject obj : keys){
			switch (switcher){
				case K_FACE:	kinematicFace(obj);
								break;
				case K_SEEK:	kinematicSeek(obj);
								break;
				case K_WANDER:	kinematicWander();
								break;
				case K_ARRIVE:	kinematicArrive(obj);
								break;
				case S_ALIGN:	steeringAlign(obj);
								break;
				case S_SEEK:	steeringSeek(obj);
								break;
				case S_WANDER:	steeringWander(-1);
								break;
				case S_ARRIVE:	steeringArrive(obj);
								break;
			}
		}
	}
	
	
	// A method for stepping all the behaviors in a list
	private void doBehaviorFor(Collection<Behavior> list, int behavior){
		boolean reachedPathNode = false;
		for(Iterator<Behavior> it = list.iterator() ; it.hasNext(); ){
			Behavior b = it.next();
			if(b.isFinished()){
				// Check if a seek or arrive behavior for the current target finished
				if((b != null) && b.getTarget().equals(currentPathNode) && ((behavior == S_SEEK) || (behavior == S_ARRIVE)))
					reachedPathNode = true;
				it.remove();
			}
			else
				outputs.add(b.step());
		}
		if(reachedPathNode){
			// Check if the current target is the last in this path
			if(currentPath.isLast(currentPathNode)){
				currentPath.removeFirst();
				// Compute the next path if there is one
				if(!queuedPathTargets.isEmpty()){
					Vertex start = pathFinder.translator.quantize(g.position);
					Vertex end = pathFinder.translator.quantize(queuedPathTargets.removeFirst().position);
					currentPath = pathFinder.aStar(start, end, g.parent.heuristic);
					currentPathNode = currentPath.getFirst();
				}
				// Otherwise we are done path following
				else{
					currentPath = null;
					currentPathNode = null;
				}
			}
			// Get the next target
			else{
				currentPath.removeFirst();
				currentPathNode = currentPath.getFirst();
			}
			// Check if there are no more targets
			if((currentPathNode == null) || (currentPath == null))
				return;
			// Arrive if this is the last target in the path
			if(currentPath.isLast(currentPathNode))
				steeringArrive(currentPathNode);
			else
				steeringSeek(currentPathNode);
			
		}
	}
	
	
	// A method for en-queuing a target in a given queue set
	private void enqueueTarget(LinkedList<LinkedList<GameObject>> queueSet, GameObject target, int delay){
		try{
			LinkedList<GameObject> set = queueSet.get(delay - 1);
			set.add(target);
		} catch(IndexOutOfBoundsException e){
			int start = queueSet.size();
			for(; start < delay ; start++){
				queueSet.add(new LinkedList<GameObject>());
			}
			queueSet.getLast().add(target);
		}
	}
	
	// This will compute a path to the given target and immediately follow it
	private void doPathNow(Target t){
		// Quantize the target
		Vertex quantizedTarget = pathFinder.translator.quantize(t.position);
		// Compute a path to the target
		Path newPath = pathFinder.aStar(pathFinder.translator.quantize(g.position), quantizedTarget, g.parent.heuristic);
		// Set the active path and target
		currentPath = newPath;
		currentPathNode = newPath.getFirst();
		// Clear active behaviors and generate new ones
		clearQueuedBehaviors();
		clearActiveBehaviors();
		if(currentPath.isLast(currentPathNode))
			steeringArrive(currentPathNode);
		else
			steeringSeek(currentPathNode);
	}

	public void clearPaths() {
		this.currentPath = null;
		this.currentPathNode = null;
		this.queuedPathTargets.clear();
	}
}
