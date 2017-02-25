package thinking;

import java.util.Collection;

/**
 * This interface lays out generic AI methods for varying AI implementations.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public interface AI {
	
	/**
	 * This method re-constructs all the current behaviors, making them recalculate.
	 */
	public void retarget();
	

	/**
	 * The method will run/step all the currently active behaviors.
	 * @return True if all the current behaviors are done (after a step if any are present).
	 */
	boolean runBehaviors();

	
	/**
	 * This method returns whether or not all the current behaviors are done.
	 * @return True if all the current behaviors are done.
	 */
	boolean behaviorsStepDone();
	
	
	/**
	 * This method steps the next sets of behaviors into the active map.
	 */
	void stepNextBehaviors();

	
	/**
	 * This method returns all the outputs from the past steps.
	 * YOU SHOULD BLEND AND APPLY OUTPUTS ON EACH STEP AND CLEAR THIS LIST!
	 * @return All the active outputs from behaviors
	 */
	Collection<Output> getBehaviorOutputs();
}
