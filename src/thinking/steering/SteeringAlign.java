package thinking.steering;

import model.GameObject;
import model.SimpleUpdater;
import thinking.Behavior;
import thinking.Output;

/**
 * This class defines the Align steering behavior.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class SteeringAlign extends Behavior {
	
	/** The radius of deceleration */
	private static final float ROD = 20;
	/** The radius of satisfaction */
	private static final float ROS = 5;
	/** The time it should take to get to target */
	private static final float TIMETOTARGET = 15;
	/** The target to align to */
	private GameObject target;
	/** The character to align */
	private GameObject character;

	
	/**
	 * This constructs a SteeringAlign object.
	 * @param target The target to align to.
	 * @param character The character to align.
	 */
	public SteeringAlign(GameObject target, GameObject character){
		this.target = target;
		this.character = character;
	}
	
	
	/**
	 * This method steps the behavior until it reaches its goal.
	 */
	@Override
	public Output step() {
		// How much do we need to rotate by.
		float rotation = target.o - character.o;
		rotation = SimpleUpdater.mapAngleRange(rotation);
		float rotationSize = Math.abs(rotation);
		float goalRotation;
		// If we are in the radius of satisfaction, stop immediately
		if(rotationSize < ROS){
			finished = true;
			return new Output(null, null, null, 0, SimpleUpdater.mapAngleRange(0 - character.angV),  SimpleUpdater.mapAngleRange(0 - character.angA));
		}
		// If we are outside the radius of deceleration, rotate at max speed
		else if(rotationSize > ROD)
			goalRotation = SimpleUpdater.MAX_ROTATION;
		// Otherwise star slowing down
		else
			goalRotation = SimpleUpdater.MAX_ROTATION * (rotationSize / ROD);
		// Make sure it is singed correctly
		goalRotation *= (rotation) / rotationSize;
		// Calculate the appropriate angular velocity to add/subtract from the character
		float steeringAngular = goalRotation - character.angV;
		steeringAngular /= TIMETOTARGET;
		steeringAngular = SimpleUpdater.clipToMaxAngularAcceleration(steeringAngular);
		return new Output(null, null, null, 0, 0, steeringAngular - character.angA);
	}


	@Override
	public GameObject getTarget() {
		return target;
	}

}
