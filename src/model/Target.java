package model;

import processing.core.PVector;

/**
 * This class implements a very simple Game Object, a target.
 * The target has all the numerical properties of a Game Object, but
 * will always have null AI and Updater components, and it cannot be drawn.
 * Targets are useful for giving Actor AI something to go for without drawing anything extra.
 * All constructed targets, will have a GUID of -1 as of now, and are not meant to be stored
 * in the master object list.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Target extends GameObject {

	/**
	 * The super simple target constructor.
	 */
	public Target() {
		super(null, -1);
	}
	
	/**
	 * This lets you specify the position of the Target as a PVector
	 * when it is constructed.
	 * @param pos The position t give this target, as a PVector.
	 */
	public Target(PVector pos){
		super(null, -1);
		super.position = pos;
	}

	@Override
	public void draw() {}
	
	public boolean equals(Object o){
		if((o == null) || !(o instanceof Target))
			return false;
		return ((Target) o).position.equals(position);
	}
	
	public String toString(){
		return position.toString();
	}

}
