package thinking.steering;

import model.GameObject;
import model.SimpleUpdater;
import processing.core.PVector;
import thinking.Behavior;
import thinking.Output;

/**
 * This class defines the Seek steering behavior.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class SteeringSeek extends Behavior {

	/** The radius of satisfaction */
	private float ROS = 25;
	/** The target to seek */
	private GameObject target;
	/** The character to move */
	private GameObject character;
	
	
	/**
	 * This constructs the SteeringSeek behavior.
	 * @param target The target to seek.
	 * @param character The character to move.
	 */
	public SteeringSeek(GameObject target, GameObject character){
		this.target = target;
		this.character = character;
	}
	
	
	/**
	 * This method steps through the behavior until it finishes.
	 */
	@Override
	public Output step(){
		// Get my current direction
		PVector characterDirection = new PVector(character.velocity.x, character.velocity.y);
		characterDirection.normalize();
		// Get the direction to seek
		PVector direction = PVector.sub(target.position, character.position);
		PVector directionDirection = new PVector(direction.x, direction.y);
		directionDirection.normalize();
		// If I am headed in the direction I need to seek, or I have crossed through my target's radius of satisfaction
		// I should finish seeking
		if(characterDirection.equals(directionDirection) || (PVector.dist(character.position, target.position) < ROS))
			finished = true;
		// Adjust direction to max acceleration
		SimpleUpdater.clipToMaxAcceleration(direction);
		return new Output(null, null, direction.sub(character.acceleration), 0, 0, 0);
	}

	
	@Override
	public GameObject getTarget() {
		return target;
	}

}
