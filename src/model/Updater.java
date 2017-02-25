package model;

import processing.core.PVector;
import thinking.Output;

public abstract class Updater {

	/** The max velocity any object can have */
	public static final float MAX_VELOCITY = 3.2f;
	/** The max acceleration any object can have */
	public static final float MAX_ACCELERATION = 0.6f;
	/** The max angular velocity any object can have */
	public static final float MAX_ROTATION = 6.5f;
	/** The max angular acceleration any object can have */
	public static final float MAX_ANGULAR_ACCELERATION = 0.5f;
	
	public abstract void update(GameObject g);
	
	/**
	 * This method updates a specific GameObject according to an Output object.
	 * @param g The GameObject to update.
	 * @param o The Output object to update the GameObject with.
	 */
	public static void update(GameObject g, Output o){
		// Only add vectors that are not null
		if(o.deltaPos != null)
			g.position.add(o.deltaPos);
		if(o.deltaV != null)
			g.velocity.add(o.deltaV);
		if(o.deltaA != null)
			g.acceleration.add(o.deltaA);
		// Only add angles that are numbers
		if(o.deltaO != Float.NaN)
			g.o += o.deltaO;
		if(o.deltaAV != Float.NaN)
			g.angV += o.deltaAV;
		if(o.deltaAA != Float.NaN)
			g.angA += o.deltaAA;
	}
	
	
	/**
	 * This is a simple static helper method to
	 * clip angle values to between 0 and 360.
	 * @param angle The angle to clip.
	 * @return The angle such that 0 <= a < 360.
	 */
	public static float roundAngle(float angle){
		angle %= 360;
		if(angle < 0)
			return (angle += 360);
		else
			return angle;
	}
	
	
	/**
	 * This function maps an angle to the range [-179, 180].
	 * @param angle The angle to adjust.
	 * @return The adjusted angle.
	 */
	public static float mapAngleRange(float angle){
		angle = roundAngle(angle);
		if(angle > 180)
			angle -= 360;
		return angle;
	}
	
	
	/**
	 * This method normalizes a velocity vector AND SCALES IT to max velocity.
	 * @param v The vector to clip.
	 */
	public static void clipToMaxVelocity(PVector v){
		v.normalize();
		v.mult(MAX_VELOCITY);
	}
	
	
	/**
	 * This method normalizes an acceleration vector AND SCALES IT to max acceleration.
	 * @param v The vector to clip.
	 */
	public static void clipToMaxAcceleration(PVector v){
		v.normalize();
		v.mult(MAX_ACCELERATION);
	}
	
	
	/**
	 * This method ensures the magnitude of angular velocity is no more than
	 * the given maximum. If the given angle is greater than the maximum, it
	 * will be returned as the maximum. If it is less than the negative maximum,
	 * it will be returned as the negative maximum. Otherwise it will stay the same.
	 * @param a The angular velocity to clip.
	 * @return The clipped angular velocity.
	 */
	public static float clipToMaxRotation(float a){
		if(a > MAX_ROTATION)
			return MAX_ROTATION;
		else if(a < -MAX_ROTATION)
			return -MAX_ROTATION;
		else return a;
	}
	
	
	/**
	 * This method ensures the magnitude of angular acceleration is no more than
	 * the given maximum. If the given angle is greater than the maximum, it
	 * will be returned as the maximum. If it is less than the negative maximum,
	 * it will be returned as the negative maximum. Otherwise it will stay the same.
	 * @param a The angular acceleration to clip.
	 * @return The clipped angular acceleration.
	 */
	public static float clipToMaxAngularAcceleration(float a){
		if(a > MAX_ANGULAR_ACCELERATION)
			return MAX_ANGULAR_ACCELERATION;
		else if(a < -MAX_ANGULAR_ACCELERATION)
			return -MAX_ANGULAR_ACCELERATION;
		else return a;
	}
}
