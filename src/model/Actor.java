package model;

import manager.Sketch;

/**
 * This class implements an Actor from a generic GameObject.
 * Actors are player or AI controlled arrows that can move around the game world.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Actor extends GameObject {
	
	/** Does this actor drop BreadCrumbs? */
	public boolean poopsBreadcrumbs = false;
	/** A timer for how often to drop BreadCrumbs */
	public static final int POOPER_TIMER = 35;
	/** A counter for dropping BreadCrumbs */
	public int counter = 0;
	/** This translates the scale vector into usable values */
	protected static final int scalefactor = 5;
	/** Life variable for monster */
	public boolean isAlive = true;

	
	/**
	 * This constructs an Actor as a standard GameObejct.
	 * @param parent The parent PApplet to tie to.
	 * @param guid The unique ID of this object. 
	 */
	public Actor(Sketch parent, int guid) {
		super(parent, guid);
		if(parent == null)
			throw new IllegalArgumentException();
		fill[0] = 0;
		fill[1] = 100;
		fill[2] = 255;
		fill[3] = 255;
	}

	
	/**
	 * This method defines how to draw an Actor.
	 */
	@Override
	public void draw() {
		float r = scale * scalefactor;
		parent.pushMatrix();
		parent.fill(fill[0], fill[1], fill[2], fill[3]);
		parent.translate(position.x, position.y);
		parent.rotate((float) Math.toRadians(o));
		parent.noStroke();
		parent.ellipse(0, 0, 2 * r, 2 * r);
		parent.translate(0, (float)-.25 * r);
		parent.triangle(-r, 0, r, 0, 0, -2 * r);
		parent.popMatrix();
	}
}
