package thinking.kinematic;

import model.GameObject;
import model.SimpleUpdater;
import thinking.Behavior;
import thinking.NormalAI;

/**
 * This class wraps Kinematic behaviors into an easy to call form.
 * It does some amount of work to construct the face behavior, and
 * ensures the character is oriented correctly on a call to arrive or seek.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Kinematic {
	
	/** The number of steps to put face through */
	private static final int FACE_STEPFACTOR = 9;

	
	/**
	 * This method orients the character towards another object.
	 * @param target The target to look at/face.
	 * @param character The character to turn.
	 * @return The Behavior defining the face/orient function.
	 */
	public static Behavior face(GameObject target, GameObject character){
		float x = target.position.x - character.position.x;
		float y = target.position.y - character.position.y;
		float newO = (float) Math.toDegrees(-Math.atan(x / y));
		if(y > 0)
			newO += 180;
		float dif = SimpleUpdater.mapAngleRange(SimpleUpdater.roundAngle(newO) - SimpleUpdater.roundAngle(character.o));
		return new KinematicFace(target, character, FACE_STEPFACTOR, dif);
	}
	
	
	/**
	 * This method orients the character towards another object.
	 * @param character The character to turn.
	 * @param angle The angle to make the character turn to.
	 * @return The Behavior defining the face/orient function.
	 */
	public static Behavior face(GameObject character, float angle){
		float dif = SimpleUpdater.mapAngleRange(SimpleUpdater.roundAngle(angle) - character.o);
		return new KinematicFace(null, character, FACE_STEPFACTOR, dif);
	}
	
	
	/**
	 * This method makes the character seek a given target.
	 * @param target The target to seek.
	 * @param character The character to make seek.
	 * @return The Behavior defining the seek function.
	 */
	public static Behavior seek(GameObject target, GameObject character){
		((NormalAI) character.thinker).enqueueKinematicFace(target, 0);
		return new KinematicSeek(target, character);
	}
	
	
	/**
	 * This method makes the character wander.
	 * @param character The character to make wander.
	 * @return The Behavior defining the wander function.
	 */
	public static Behavior wander(GameObject character){
		return new KinematicWander(character);
	}
	
	
	/**
	 * This method makes the character arrive at a given target.
	 * @param target The target to arrive at.
	 * @param character The character to make move.
	 * @return The Behavior defining the arrive function.
	 */
	public static Behavior arrive(GameObject target, GameObject character){
		((NormalAI) character.thinker).enqueueKinematicFace(target, 0);
		return new KinematicArrive(target, character);
	}
}
