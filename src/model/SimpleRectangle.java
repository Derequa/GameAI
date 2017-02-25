package model;

import manager.Sketch;
import processing.core.PVector;

/**
 * This class implements a simple rectangle game object.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class SimpleRectangle extends GameObject {
	
	/** The size of the rectangle */
	private PVector size;

	/**
	 * This constructs a rectangle with the given size.
	 * @param parent The parent sketch to draw in.
	 * @param guid The GUID for this game object.
	 * @param size The size of the rectangle.
	 */
	public SimpleRectangle(Sketch parent, int guid, PVector size) {
		super(parent, guid);
		this.size = size;
		scale = 1.0f;
	}
	
	/**
	 * This changes the size of the rectangle.
	 * @param v The size to set the rectangle as a vector.
	 */
	public void setSize(PVector v){
		size = v;
	}

	/*
	 * (non-Javadoc)
	 * @see model.GameObject#draw()
	 */
	@Override
	public void draw() {
		parent.pushMatrix();
		parent.translate(position.x, position.y);
		parent.rotate(o);
		parent.scale(scale);
		parent.noStroke();
		parent.fill(fill[0], fill[1], fill[2], fill[3]);
		parent.rect(0, 0, size.x, size.y);
		parent.popMatrix();
	}

}
