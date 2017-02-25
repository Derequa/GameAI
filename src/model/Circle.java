package model;

import manager.Sketch;
import processing.core.PVector;

/**
 * This class implements an animation for a circle/point.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Circle extends Animation {
	
	/** The diameter to draw circle animations with */
	public static final int DIAMETER = 12;
	
	/**
	 * This constructs a circle animation with the given fill, faders, and position.
	 * @param fill The fill for the animation.
	 * @param fadeInTime How long to fade in.
	 * @param fadeOutTime How long to fade out.
	 * @param life How long this animation lasts.
	 * @param position Where to draw this animation
	 */
	public Circle(int[] fill, int fadeInTime, int fadeOutTime, int life, PVector position){
		super(fill, fadeInTime, fadeOutTime, life, position);
	}

	/*
	 * (non-Javadoc)
	 * @see model.Animation#draw(manager.Sketch)
	 */
	@Override
	public void draw(Sketch parent) {
		parent.noStroke();
		parent.fill(fill[0], fill[1], fill[2], fill[3]);
		parent.ellipse(position.x, position.y, DIAMETER, DIAMETER);
		update();
	}

}
