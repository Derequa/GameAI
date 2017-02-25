package thinking.kinematic;

import model.GameObject;
import thinking.Behavior;
import thinking.Output;

/**
 * This class defines the Face/Orient kinematic behavior.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class KinematicFace extends Behavior{
	
	/** The character to turn (unused) */
	@SuppressWarnings("unused")
	private GameObject character;
	/** Target to look at */
	private GameObject target;
	/** The step we are on */
	private int step = 0;
	/** The number of steps to take */
	private int n;
	/** The amount of degrees to rotate [-179, 180] */
	private float amount;
	
	
	/**
	 * This constructs a KinematicFace behavior.
	 * @param target The target to look at.
	 * @param character The character to turn.
	 * @param n The number of steps to take.
	 * @param amount The amount of degrees to rotate [-179, 180]
	 */
	public KinematicFace(GameObject target, GameObject character, int n, float amount){
		this.character = character;
		this.target = target;
		this.n = n;
		this.amount = amount;
	}
	
	
	/**
	 * This method steps the behavior until it finishes.
	 */
	@Override
	public Output step(){
		// If we are on or past the last step, we are done, do nothing
		if(step >= n){
			finished = true;
			return new Output();
		}
		// Otherwise, increment the current step and change by 1 / n
		step++;
		return new Output(null, null, (amount / n), 0);
	}

	
	@Override
	public GameObject getTarget(){
		return target;
	}
}
