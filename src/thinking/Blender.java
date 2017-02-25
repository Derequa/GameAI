package thinking;

import java.util.Collection;

import processing.core.PVector;

/**
 * This class provides methods for blending lists of outputs.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Blender {

	/** The mode flag for simple/additive blending */
	public static final int SIMPLE = 0;
	/** The mode flag for average blending */
	public static final int AVERAGE = 1;
	/** The mode flag for weighted blending */
	public static final int WEIGHTED = 2;
	
	
	/**
	 * This method wraps the other methods with a mode flag.
	 * This lets objects get blended according to mode easily.
	 * @param list The list of outputs to blend.
	 * @param mode The mode to blend in.
	 * @return A single output resulting from blending, or null if an invalid mode was given.
	 */
	public static Output blend(Collection<Output> list, int mode){
		switch (mode){
			case SIMPLE:	return additive(list);
			case AVERAGE: 	return average(list);
			case WEIGHTED:	return weightedAverage(list);
		}
		return null;
	}
	
	
	/**
	 * This method blends a list of outputs to a single output in an additive manner.
	 * @param list The list of outputs to blend.
	 * @return The blended outputs as a single output.
	 */
	public static Output additive(Collection<Output> list){
		// Make an output to add to
		Output blended = new Output(new PVector(), new PVector(), new PVector(), 0, 0, 0, 1);
		// Add each valid field to the output
		for(Output k : list){
			if(k == null)
				continue;
			if(k.deltaPos != null)
				blended.deltaPos.add(k.deltaPos);
			if(k.deltaV != null)
				blended.deltaV.add(k.deltaV);
			if(k.deltaA != null)
				blended.deltaA.add(k.deltaA);
			blended.deltaO += k.deltaO;
			blended.deltaAV += k.deltaAV;
			blended.deltaAA += k.deltaAA;
		}
		// Clear the list we blended and return
		list.clear();
		return blended;
	}
	
	
	/**
	 * This method blends a list of outputs to a single output by averaging
	 * linear and angular accelerations. This is meant for use with steering behaviors.
	 * @param list The list of outputs to blend.
	 * @return The blended outputs as a single output.
	 */
	public static Output average(Collection<Output> list){
		// Count outputs that change linear and angular acceleration
		int nLinear = 0;
		int nAngular = 0;
		Output blended = new Output(new PVector(), new PVector(), new PVector(), 0, 0, 0, 1);
		// Add and count all the angular and linear accelerations
		for(Output o : list){
			if(o == null)
				continue;
			if(o.deltaA != null){
				nLinear++;
				blended.deltaA.add(o.deltaA);
			}
			if(o.deltaAA != 0.0f){
				nAngular++;
				blended.deltaAA += o.deltaAA;
			}
		}
		// Average angular and linear accelerations
		blended.deltaA.div(nLinear);
		blended.deltaAA /= nAngular;
		// Clear list and return
		list.clear();
		return blended;
	}
	
	
	/**
	 * This method blends a list of outputs to a single output by weighted averaging
	 * linear and angular accelerations. This is meant for use with steering behaviors.
	 * @param list The list of outputs to blend.
	 * @return The blended outputs as a single output.
	 */
	public static Output weightedAverage(Collection<Output> list){
		// Count outputs that change linear and angular acceleration
		int nLinear = 0;
		int nAngular = 0;
		Output blended = new Output(new PVector(), new PVector(), new PVector(), 0, 0, 0, 1);
		// Multiply linear and angular accelerations by their weight, add them, and count them
		for(Output o : list){
			if(o == null)
				continue;
			if(o.deltaA != null){
				nLinear++;
				blended.deltaA.add(o.deltaA.mult(o.weight));
			}
			if(o.deltaAA != 0.0f){
				nAngular++;
				blended.deltaAA += (o.deltaAA * o.weight);
			}
		}
		// Average the weighted sums
		if(nLinear > 0)
			blended.deltaA.div(nLinear);
		if(nAngular > 0)
			blended.deltaAA /= nAngular;
		// Clear list and return
		list.clear();
		return blended;
	}
}
