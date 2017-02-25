package thinking;

import model.GameObject;
import thinking.Behavior;

/**
 * This class outlines the generic properties of a Behavior.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public abstract class Behavior {
	
	/** This determines if the behavior is finished */
	protected boolean finished = false;


	/**
	 * This method steps the behavior until it finishes.
	 * @return The output defining the result of the behavior.
	 */
	public abstract Output step();


	/**
	 * This gets the target attached to this behavior.
	 * @return The target GameObject.
	 */
	public abstract GameObject getTarget();

	
	/**
	 * This method determines whether or not the behavior has finished.
	 * @return True if the behavior is finished.
	 */
	public boolean isFinished() {
		return finished;
	}

}
