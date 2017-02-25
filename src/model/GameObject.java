 package model;

import manager.Sketch;
import processing.core.PVector;
import thinking.AI;
import thinking.Blender;

/**
 * This class defines the basic fields and behaviors of a generic
 * Game Object in this engine. It makes use of the component Game object model.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public abstract class GameObject {
	
	/** 
	 * This is the Globaly Unique Identifier for this game object.
	 * it is unique to this object alone
	 */
	public int guid;
	/** This is the object's acceleration represented as a PVector */
	public PVector acceleration = new PVector();;
	/** This is the object's velocity represented as a PVector */
	public PVector velocity = new PVector();
	/** This is the object's position represented as a PVector */
	public PVector position = new PVector();
	/** This is the object's current orientation */
	public float o;
	/** This is the object's current angular velocity */
	public float angV;
	/** This is the objects's current angular velocity */
	public float angA;
	/** This is the object's current scale (only implemented in Actor) */
	public float scale = 1.5f;
	/** The color to draw shapes for this game object */
	protected int[] fill = {255, 255, 255, 0};
	/** This specifies the mode to blend the object's behavior */
	public int blendingMode = Blender.SIMPLE;
	/** The PApple parent class this is tied to */
	public Sketch parent;
	/** The class responsible for physics/movement updates (Only one currently) */
	public Updater updater;
	/** The class responsible for AI behaviors */
	public AI thinker;
	/** The layer to draw this object on. Lower number layers are drawn first */
	public int layer = 0;
	
	/**
	 * This defines a generic constructor for a GameObject.
	 * @param parent The parent PApplet to tie the object to.
	 * @param guid The unique ID to give this object.
	 */
	public GameObject(Sketch parent, int guid){
		this(parent, guid, null);
	}
	
	
	/**
	 * This defines an alternate generic GameObject constructor where the Update is given.
	 * @param parent The parent PApplet to tie the object to.
	 * @param guid The unique ID to give this object.
	 * @param updater The updater class to tie to this object.
	 */
	public GameObject(Sketch parent, int guid, Updater updater){
		this.parent = parent;
		this.guid = guid;
		this.updater = updater;
	}
	
	
	/**
	 * This method returns whether this object has a specified AI component.
	 * @return True if this object has an AI component that is not null.
	 */
	public boolean hasAI(){
		return thinker != null;
	}
	
	
	/**
	 * This method returns whether or not the Updater component of this object is null.
	 * @return True if this object has an Updater component that is not null.
	 */
	public boolean hasUpdater(){
		return updater != null;
	}
	

	/**
	 * This method runs the behaviors in the given AI component, if there is one.
	 */
	public void runBehaviors(){
		if(hasAI()){
			thinker.runBehaviors();
		}
	}
	
	
	/**
	 * This method updates this object according to its Updater component (if one exists)
	 */
	public void update(){
		if(hasUpdater())
			updater.update(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + guid;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return ((obj != null) && 
				(obj instanceof GameObject) &&
				(((GameObject) obj).guid == guid));
	}

	
	/**
	 * This method defines the interface for how GameObjects draw themselves.
	 * The definition of this method should use PApplet's matrix drawing to appropriately draw itself.
	 */
	public abstract void draw();
	
	
	/**
	 * This method sets the acceleration vector for this object.
	 * @param x The X component of the acceleration to set.
	 * @param y The Y component of the acceleration to set.
	 */
	public void setAccel(float x, float y){
		acceleration.set(x, y);
	}
	
	
	/**
	 * This method sets the velocity vector for this object.
	 * @param x The X component of the velocity to set.
	 * @param y The Y component of the velocity to set.
	 */
	public void setVel(float x, float y){
		velocity.set(x, y);
	}
	
	
	/**
	 * This method sets the position vector for this object.
	 * @param x The X component of the position to set.
	 * @param y The Y component of the position to set.
	 */
	public void setPos(float x, float y){
		position.set(x, y);
	}
	
	/**
	 * This changes the fill for color of this object.
	 * @param r The red component of the fill (0-255)
	 * @param g The green component of the fill (0-255)
	 * @param b The blue component of the fill (0-255)
	 * @param alpha The alpha component of the fill (0-255)
	 */
	public void setFill(int r, int g, int b, int alpha){
		fill[0] = r;
		fill[1] = g;
		fill[2] = b;
		fill[3] = alpha;
	}
}
