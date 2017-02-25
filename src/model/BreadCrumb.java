package model;

import manager.Sketch;

/**
 * This class represents a bread-crumb that can be dropped by Actors in
 * the game world to show where they have been.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class BreadCrumb extends GameObject {
	
	/** How wide the crumbs are */
	private static final float WIDTH = 5;
	/** How tall the crumbs are */
	private static final float HEIGHT = 5;
	/** How long the crumbs exist */
	public static final int MAX_AGE = 400;
	/** How long the crumbs take to fade out */
	public static final int FADE_DURATION = 90;
	/** A field to remember age */
	public int age = 0;
	/** A field to remember transparency */
	public float alpha = 255;
	
	
	/**
	 * This constructs a BreadCrumb as a normal GameObject.
	 * @param parent The PApplet parent to link to.
	 * @param guid The unique ID of this BreadCrumb.
	 * @param updater The updater component to tie to.
	 */
	public BreadCrumb(Sketch parent, int guid, Updater updater) {
		super(parent, guid, updater);
		if(parent == null)
			throw new IllegalArgumentException();
		setFill(255, 50, 50, 255);
	}
	
	/**
	 * This constructs a BreadCrumb as a normal GameObject.
	 * @param parent The PApplet parent to link to.
	 * @param guid The unique ID of this BreadCrumb.
	 * @param updater The updater component to tie to.
	 * @param fill The color of the bread-crumb in RGB (0-255).
	 */
	public BreadCrumb(Sketch parent, int guid, Updater updater, int[] fill) {
		super(parent, guid, updater);
		if(parent == null)
			throw new IllegalArgumentException();
		setFill(fill[0], fill[1], fill[2], 255);
	}

	
	/**
	 * This method defines how to draw a BreadCrumb.
	 */
	@Override
	public void draw() {
		parent.pushMatrix();
		parent.translate(position.x, position.y);
		parent.fill(fill[0], fill[1], fill[2], alpha);
		parent.rect(WIDTH / 2, HEIGHT / 2, WIDTH, HEIGHT);
		parent.popMatrix();
	}

}
