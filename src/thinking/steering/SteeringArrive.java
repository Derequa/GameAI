package thinking.steering;

import model.GameObject;
import model.SimpleUpdater;
import processing.core.PVector;
import thinking.Behavior;
import thinking.Output;

/**
 * This class defines the Arrive steering behavior.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class SteeringArrive extends Behavior {
	
	/** The radius of deceleration */
	private static final float ROD = 110.0f;
	/** The radius of satisfaction */
	private static final float ROS = 24.0f;
	/** The time it should take to slow down */
	private static final float TIMETOTARGET = 5;
	/** The target to arrive at */
	private GameObject target;
	/** The character to move */
	private GameObject character;
	
	
	/**
	 * This creates the SteeringArrive behavior.
	 * @param target The target to arrive at.
	 * @param character The character to move.
	 */
	public SteeringArrive(GameObject target, GameObject character){
		this.target = target;
		this.character = character;
	}
	
	
	/**
	 * This method steps the behavior until it reaches its goal.
	 */
	@Override
	public Output step() {
		// Stop if finished
		if(finished)
			return new Output(null, null, null, 0, 0, 0);
		// Compute the direction vector
		PVector direction = PVector.sub(target.position, character.position);
		// Compute distance from the direction vector
		float dist = direction.mag();
		PVector linear;
		float goalSpeed;
		// If we are in the radius of satisfaction, stop the character
		if(dist < ROS){
			finished = true;
			return new Output(null, PVector.sub(new PVector(0, 0), character.velocity), PVector.sub(new PVector(0, 0), character.acceleration), 0, 0, 0);
		}
		// If we are outside of the radius of deceleration, go as fast as we can
		else if(dist > ROD)
			goalSpeed = SimpleUpdater.MAX_VELOCITY;
		// if we are inside the radius of deceleration, slow down
		else
			goalSpeed = SimpleUpdater.MAX_VELOCITY * (dist / ROD);
		// Calculate the acceleration to apply, based on the time to target and goal speed
		PVector goalVelocity = direction.normalize();
		goalVelocity.mult(goalSpeed);
		linear = PVector.sub(goalVelocity, character.velocity);
		linear.div(TIMETOTARGET);
		return new Output(null, null, PVector.sub(linear, character.acceleration), 0, 0, 0);
	}

	
	@Override
	public GameObject getTarget() {
		return target;
	}

}
