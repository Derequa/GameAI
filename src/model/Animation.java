package model;

import java.util.Arrays;

import manager.Settings;
import manager.Sketch;
import processing.core.PVector;

/**
 * This abstract class gives a framework for an object that needs a generic animation,
 * but isn't really a game object.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public abstract class Animation {
	
	/** The current fill of this animation */
	protected int[] fill;
	/** The set/goal alpha level for this animation */
	protected int alpha;
	/** The fade-in timer */
	protected int fadeIn;
	/** How much to change alpha on each step of fade-in */
	protected int fadeInDelta;
	/** The fade-out timer */
	protected int fadeOut;
	/** How much to change alpha on each step of fade-out */
	protected int fadeOutDelta;
	/** How long to display this animation */
	protected int life;
	/** The current step/age */
	protected int currentTime = 0;
	/** Where to draw the animation */
	protected PVector position;
	/** A flag for when the animation is finished */
	public boolean isDone = false;
	/** The scale factor for the animation */
	protected float scale = 1.0f;
	
	/**
	 * This outlines the generic animation constructor.
	 * @param fill The fill to draw this animation with.
	 * @param fadeInTime How long to fade in the animation.
	 * @param fadeOutTime How long to fade out the animation.
	 * @param life How long the animation should display between fades.
	 * @param position Where to draw the animation.
	 */
	public Animation(int[] fill, int fadeInTime, int fadeOutTime, int life, PVector position){
		// Check for the right number of elements in fill
		if(fill.length != 4){
			Settings.fail("Invalid fill array for circle!");
			throw new IllegalArgumentException();
		}
		// Check for valid time parameters
		if((fadeInTime < 0) || (fadeOutTime < 0) || (life < 0)){
			Settings.fail("Invalid timer parameter!");
			throw new IllegalArgumentException();
		}
		// Check the range of values in fill
		for(int i = 0 ; i < fill.length ; i++){
			if((fill[i] < 0) || (fill[i] > 255)){
				Settings.fail("Invalid fill values!");
				throw new IllegalArgumentException();
			}
		}
		// Copy the fill array
		this.fill = Arrays.copyOf(fill, fill.length);
		// Set the goal alpha and timers
		alpha = fill[3];
		fadeIn = fadeInTime;
		fadeOut = fadeOutTime;
		// Compute fade deltas
		if(fadeInTime > 0){
			this.fill[3] = 0;
			fadeInDelta = alpha / fadeIn;
		}
		if(fadeOutTime > 0)
			fadeOutDelta = alpha / fadeOut;
		// Set life and position
		this.life = life + fadeInTime;
		this.position = position;
	}
	
	/**
	 * This set the scale for an animation.
	 * @param scale The new scale for this animation.
	 */
	public void setScale(float scale){
		this.scale = scale;
	}
	
	/**
	 * This method is reponsible for drawing this animation to the given sketch.
	 * @param parent The sketch to draw to.
	 */
	public abstract void draw(Sketch parent);
	
	/**
	 * This updates the fades and flag of this animation on each frame.
	 * This should be called by each animation in its draw method.
	 */
	protected void update(){
		// Do nothing if the flag is set
		if(isDone)
			return;
		// Check if we should fade in
		if(currentTime <= fadeIn)
			fill[3] += fadeInDelta;
		// Check if we should fade out
		else if((currentTime > life) && (currentTime <= (life + fadeOut)))
			fill[3] -= fadeOutDelta;
		// Check if we are finished
		else if(currentTime > (life + fadeOut))
			isDone = true;
		currentTime++;
	}
}
