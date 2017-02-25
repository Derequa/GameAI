package thinking;

import processing.core.PVector;

/**
 * This class defines an object with field for storing behavior outputs for blending.
 * @author Derek Batts - dsbatt@ncsu.edu
 *
 */
public class Output {

	/** The difference in position to add/blend */
	public PVector deltaPos;
	/** The difference in velocity to add/blend */
	public PVector deltaV;
	/** The difference in acceleration to add/blend */
	public PVector deltaA;
	/** The difference in orientation to add/blend */
	public float deltaO;
	/** The difference in angular velocity to add/blend */
	public float deltaAV;
	/** The difference in angular acceleration to add/blend */
	public float deltaAA;
	/** The weight to give this output in blending */
	public float weight;
	
	
	/**
	 * This constructs an empty Output object.
	 */
	public Output(){
		this(null, null, null, 0, 0, 0, 1);
	}
	
	
	/**
	 * This constructs an Output object with null acceleration, 0 angular acceleration, and default weight.
	 * @param deltaPos The difference in position to add/blend.
	 * @param deltaV The difference in velocity to add/blend.
	 * @param deltaO The difference in orientation to add/blend.
	 * @param deltaAV The difference in angular velocity to add/blend.
	 */
	public Output(PVector deltaPos, PVector deltaV, float deltaO, float deltaAV){
		this(deltaPos, deltaV, null, deltaO, deltaAV, 0, 1.0f);
	}
	
	
	/**
	 * 	 This constructs an Output object with default weight.
	 * @param deltaPos The difference in position to add/blend.
	 * @param deltaV The difference in velocity to add/blend.
	 * @param deltaA The difference in acceleration to add/blend.
	 * @param deltaO The difference in orientation to add/blend.
	 * @param deltaAV The difference in angular velocity to add/blend.
	 * @param deltaAA The difference in angular acceleration to add/blend.
	 */
	public Output(PVector deltaPos, PVector deltaV, PVector deltaA, float deltaO, float deltaAV, float deltaAA){
		this(deltaPos, deltaV, deltaA, deltaO, deltaAV, deltaAA, 1.0f);
	}
	
	
	/**
	 * This constructs an Output object with null acceleration and 0 angular acceleration.
	 * @param deltaPos The difference in position to add/blend.
	 * @param deltaV The difference in velocity to add/blend.
	 * @param deltaO The difference in orientation to add/blend.
	 * @param deltaAV The difference in angular velocity to add/blend.
	 * @param weight The weight to give this output in blending.
	 */
	public Output(PVector deltaPos, PVector deltaV, float deltaO, float deltaAV, float weight){
		this(deltaPos, deltaV, null, deltaO, deltaAV, 0, weight);
	}
	
	
	/**
	 * This constructs an Output object according to the given parameters.
	 * @param deltaPos The difference in position to add/blend.
	 * @param deltaV The difference in velocity to add/blend.
	 * @param deltaA The difference in acceleration to add/blend.
	 * @param deltaO The difference in orientation to add/blend.
	 * @param deltaAV The difference in angular velocity to add/blend.
	 * @param deltaAA The difference in angular acceleration to add/blend.
	 * @param weight The weight to give this output in blending.
	 */
	public Output(PVector deltaPos, PVector deltaV, PVector deltaA, float deltaO, float deltaAV, float deltaAA, float weight){
		this.deltaPos = deltaPos;
		this.deltaV = deltaV;
		this.deltaA = deltaA;
		this.deltaO = deltaO;
		this.deltaAV = deltaAV;
		this.deltaAA = deltaAA;
		this.weight = weight;
	}
}
