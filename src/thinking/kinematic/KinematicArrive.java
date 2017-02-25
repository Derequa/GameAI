package thinking.kinematic;

import model.GameObject;
import model.SimpleUpdater;
import processing.core.PVector;
import thinking.Behavior;
import thinking.Output;

/**
 * This class defines the Arrive kinematic behavior.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class KinematicArrive extends Behavior {
	
	/** The radius of satisfaction */
	private static final float RADIUS_OF_SAT = 24.0f;
	/** The target to arrive at */
	private GameObject target;
	/** The character to move */
	private GameObject character;
	
	
	/**
	 * This creates a KinematicArrive behavior.
	 * @param target The target to arrive at.
	 * @param character The character to move.
	 */
	public KinematicArrive(GameObject target, GameObject character){
		this.target = target;
		this.character = character;
	}

	
	/**
	 * This method steps the behavior until it finishes.
	 */
	@Override
	public Output step(){
		// The direction to move in
		PVector direction = PVector.sub(target.position, character.position);
		// The distance to target
		float dist = Math.abs(direction.mag());
		// Stop the character and set finished to true if we are within the raius of satisfaction
		if(dist < RADIUS_OF_SAT){
			finished = true;
			return new Output(null, PVector.sub(new PVector(0, 0), character.velocity), 0, 0);
		}
		// Otherwise, clip to max velocity in direction of the target
		SimpleUpdater.clipToMaxVelocity(direction);
		character.velocity = direction;
		return new Output(null, PVector.sub(direction, character.velocity), 0, 0);
	}

	
	@Override
	public GameObject getTarget(){
		return target;
	}
	
}
