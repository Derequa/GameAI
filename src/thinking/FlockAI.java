package thinking;

import java.util.Collection;
import java.util.LinkedList;

import model.Flock;
import model.GameObject;
import model.Target;
import model.SimpleUpdater;
import processing.core.PVector;
import thinking.steering.SteeringAlign;
import thinking.steering.SteeringArrive;
import thinking.steering.SteeringSeek;

/**
 * This class implements an AI component specific to flocking GameObjects.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class FlockAI implements AI{
	
	/** The flock this member is a part of */
	private Flock group;
	/** the other members of the flock */
	private Collection<GameObject> others = new LinkedList<GameObject>();
	/** The leader of the flock */
	private GameObject leader;
	/** The character this AI is attached to */
	private GameObject character;
	/** The outputs of behaviors to blend */
	private LinkedList<Output> outputs = new LinkedList<Output>();
	/** The current CollisionAvoid behavior */
	private CollisionAvoid b_Avoid;
	/** The current SteeringArrive behavior */
	private SteeringArrive b_Arrive;
	/** The current SteeringSeek behavior */
	private SteeringSeek b_Seek;
	/** The current SteeringAlign behavior */
	private SteeringAlign b_Align;
	
	
	/**
	 * This constructs a flock AI for a GameObject.
	 * @param group The flock to long to.
	 * @param character The character this AI is for.
	 */
	public FlockAI(Flock group, GameObject character){
		this.group = group;
		this.leader = group.leader;
		this.character = character;
		others.addAll(group.members);
		others.add(leader);
	}
	
	
	public void retarget(){
		// Create new behaviors
		b_Avoid = new CollisionAvoid(others, character);
		b_Arrive = new SteeringArrive(new Target(group.getCenterOfMass()), character);
		b_Seek = new SteeringSeek(leader, character);
		Target t = new Target();
		PVector direction = new PVector(character.velocity.x, character.velocity.y);
		if(direction.mag() == 0){
			t.o = character.o;
		}
		else{
			direction.normalize();
			float newO = (float) Math.toDegrees(-Math.atan(direction.x / direction.y));
			if(direction.y > 0)
				newO += 180;
			t.o = newO;
		}
		b_Align = new SteeringAlign(t, character);
	}

	
	@Override
	public boolean runBehaviors() {
		// Step behaviors
		Output o_Avoid = b_Avoid.step();
		Output o_Arrive = b_Arrive.step();
		Output o_Seek = b_Seek.step();
		Output o_Align = b_Align.step();
		
		// Set weights
		if((o_Avoid.deltaA == null) || ((o_Avoid.deltaA != null) && (o_Avoid.deltaA.mag() <= 0.001))){
			o_Avoid.weight = 0;
			o_Arrive.weight = (float) (1.5 / 3.0);
			o_Seek.weight = (float) (1.5 / 3.0);
		}
		else if((o_Avoid.deltaA != null) && (o_Avoid.deltaA.mag() <= (SimpleUpdater.MAX_ACCELERATION / 4))){
			o_Avoid.weight = (float) (.8 * (1.0 / 3.0));
			o_Arrive.weight = (float) (.1 * (1.0 / 3.0));
			o_Seek.weight = (float) (.1 * (1.0 / 3.0));
		}
		else {
			o_Avoid.weight = 1;
			o_Arrive.weight = 0;
			o_Seek.weight = 0;
		}

		// Match velocity to flock (deprecated)
		//character.velocity.normalize().mult(group.getAverageVelocity().mag());
		
		// Add to final list, ready for blending
		outputs.add(o_Avoid);
		outputs.add(o_Arrive);
		outputs.add(o_Seek);
		outputs.add(o_Align);
		return false;
	}
	
	
	/**
	 * This method does nothing for FlockAI.
	 * @param t No really.
	 * @param delay It does nothing.
	 */
	public void enqueueKineaticFace(GameObject t, int delay){}

	
	@Override
	public boolean behaviorsStepDone() {
		return false;
	}

	
	@Override
	public void stepNextBehaviors() {}

	
	@Override
	public Collection<Output> getBehaviorOutputs() {
		return outputs;
	}

}
