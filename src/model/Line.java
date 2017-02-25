package model;

import manager.Sketch;
import processing.core.PVector;

/**
 * This implements a ssimple animation for a line.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Line extends Animation {

	/** The end point for this line */
	private PVector end;
	
	/**
	 * This constructs a line animation with the given endpoints and faders.
	 * @param fill The fill to draw this line with.
	 * @param fadeInTime How long the fade in should take.
	 * @param fadeOutTime How long the fade out should take.
	 * @param life How long the animation should last.
	 * @param start The starting point of this line.
	 * @param end The end point of this line.
	 */
	public Line(int[] fill, int fadeInTime, int fadeOutTime, int life, PVector start, PVector end) {
		super(fill, fadeInTime, fadeOutTime, life, start);
		this.end = end;
	}

	/*
	 * (non-Javadoc)
	 * @see model.Animation#draw(manager.Sketch)
	 */
	@Override
	public void draw(Sketch parent) {
		parent.stroke(fill[0], fill[1], fill[2], fill[3]);
		parent.line(position.x, position.y, end.x, end.y);
		update();
	}

}
