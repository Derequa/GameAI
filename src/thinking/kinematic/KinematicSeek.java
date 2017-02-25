package thinking.kinematic;

import model.GameObject;
import model.SimpleUpdater;
import processing.core.PVector;
import thinking.Behavior;
import thinking.Output;

/**
 * This class defines the Seek kinematic behavior.
 * @author Derek Batts - dsbatts@ncsu.edu
 * 
 */
public class KinematicSeek extends Behavior {
	
	/** The target to seek */
	private GameObject target;
	/** The character to move */
	private GameObject character;
	
	
	/**
	 * This constructs a KinematicSeek behavior.
	 * @param target The target to seek.
	 * @param character The character to move.
	 */
	public KinematicSeek(GameObject target, GameObject character){
		this.target = target;
		this.character = character;
	}

	
	/**
	 * This method steps through the behavior until it finishes.
	 */
	@Override
	public Output step(){
		// Get the direction to seek and clip to max velocity in that direction
		PVector direction = PVector.sub(target.position, character.position);
		SimpleUpdater.clipToMaxVelocity(direction);
		finished = true;
		return new Output(null, direction.sub(character.velocity), 0, 0);
	}

	
	@Override
	public GameObject getTarget(){
		return target;
	}

}
