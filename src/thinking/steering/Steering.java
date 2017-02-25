package thinking.steering;

import model.GameObject;
import thinking.Behavior;
import thinking.NormalAI;

/**
 * This class (sorta tries) to wrap steering behavior classes into a
 * simple call convention. It isn't as useful as the Kinematic wrapper and
 * isn't even used as the standard throughout the rest of the engine, but hey it does a little.
 * @author clone
 *
 */
public class Steering {

	/**
	 * This method creates a SteeringAlign behavior and returns it.
	 * @param target The target to align with.
	 * @param character The character to align.
	 * @return The behavior defining steering align.
	 */
	public static Behavior align(GameObject target, GameObject character){
		return new SteeringAlign(target, character);
	}
	
	
	/**
	 * This method creates a SteeringSeek behavior and returns it.
	 * If the character has a normal AI, it will be told to face the target.
	 * @param target The target to seek.
	 * @param character The character to adjust.
	 * @return The behavior defining steering seek.
	 */
	public static Behavior seek(GameObject target, GameObject character){
		if(character.thinker instanceof NormalAI)
			((NormalAI) character.thinker).kinematicFace(target);
		return new SteeringSeek(target, character);
	}
	
	
	/**
	 * This method creates a SteeringWander behavior and returns it.
	 * @param character The character to make wander.
	 * @param time How long the character should wander.
	 * @return The behavior defining steering wander.
	 */
	public static Behavior wander(GameObject character, int time){
		return new SteeringWander(character, time);
	}
	
	
	/**
	 * This method creates a SteeringArrive behavior and returns it.
	 * If the character has a normal AI, it will be told to face the target.
	 * @param target The target to arrive at.
	 * @param character The character that needs to move.
	 * @return The behavior defining steering arrive.
	 */
	public static Behavior arrive(GameObject target, GameObject character){
		if(character.thinker instanceof NormalAI)
			((NormalAI) character.thinker).kinematicFace(target);
		return new SteeringArrive(target, character);
	}
}
