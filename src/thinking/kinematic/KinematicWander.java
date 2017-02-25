package thinking.kinematic;

import model.GameObject;
import model.SimpleUpdater;
import processing.core.PVector;
import thinking.Behavior;
import thinking.NormalAI;
import thinking.Output;

/**
 * This class defines the Wander kinematic behavior.
 * @author Derek Batts - dsbatts@ncsu.edu
 * 
 */
public class KinematicWander extends Behavior {

	/** The max angle we can rotate the character by when we change direction */
	private static final float MAX_ROTATION = 30;
	/** The minimum angle we can rotate the character by when we change direction */
	private static final float MIN_ROTATION = 4;
	/** How long to keep changing directions */
	private static final int COUNTER = 600;
	/** How often to change directions */
	private static final int CHANGE = 90;
	/** What step of wander we are on */
	private int step = 0;
	/** The wandering character */
	private GameObject character;
	
	
	/**
	 * This creates a KinematicWander behavior.
	 * @param character The character to make wander.
	 */
	public KinematicWander(GameObject character){
		this.character = character;
	}

	
	/**
	 * This method steps the behavior until it finishes.
	 */
	@Override
	public Output step(){
		// If we are past the limit, we are done, do nothing
		if(step > COUNTER){
			finished = true;
			return new Output();
		}
		PVector newV = null;
		// If this is the first step, clip to max velocity in the direction orientation
		if(step == 0){
			newV = PVector.fromAngle((float) Math.toRadians(character.o - 90));
			SimpleUpdater.clipToMaxVelocity(newV);
		}
		// Otherwise pick a random angle (in bounds) and clip to max velocity in it's direction
		else if((step % CHANGE) == 0){
			float res = (float) (Math.random() - Math.random());
			float angle = (MAX_ROTATION * res);
			if((res > 0) && (Math.abs(angle) < MIN_ROTATION))
				angle += MIN_ROTATION;
			else if((res < 0) && (Math.abs(angle) < MIN_ROTATION))
				angle -= MIN_ROTATION;
			float newO = character.o + angle;
			((NormalAI) character.thinker).kinematicFace(newO);
			newV = PVector.fromAngle((float) Math.toRadians(newO - 90));
			SimpleUpdater.clipToMaxVelocity(newV);
		}
		step++;
		return new Output(null, newV, 0, 0);
	}

	
	@Override
	public GameObject getTarget(){
		return null;
	}
}
