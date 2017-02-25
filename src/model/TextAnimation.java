package model;

import manager.Sketch;
import processing.core.PVector;

/**
 * This class implements a simple fade-in fade-out text animation.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class TextAnimation extends Animation {
	
	/** The text to draw */
	private String text;

	/**
	 * This constructs the animation according to the super constructor defined in the Animation class.
	 * @param fill The color to draw the text.
	 * @param fadeInTime The time (in frames) to fade in the text.
	 * @param fadeOutTime The time (in frames) to fade out the text.
	 * @param life The time (in frames) the text is displayed in between fades.
	 * @param position Where to draw the text (upper left corner).
	 * @param text The text to draw.
	 */
	public TextAnimation(int[] fill, int fadeInTime, int fadeOutTime, int life, PVector position, String text) {
		super(fill, fadeInTime, fadeOutTime, life, position);
		this.text = text;
	}
	
	/**
	 * This changes the text that will be drawn.
	 * @param text The new text to draw.
	 */
	public void setText(String text){
		this.text = text;
	}
	
	/*
	 * (non-Javadoc)
	 * @see model.Animation#draw(manager.Sketch)
	 */
	@Override
	public void draw(Sketch parent) {
		parent.pushMatrix();
		parent.translate(position.x, position.y);
		parent.scale(scale);
		parent.fill(fill[0], fill[1], fill[2], fill[3]);
		parent.text(text, 0, 0);
		parent.popMatrix();
		update();
	}

}
