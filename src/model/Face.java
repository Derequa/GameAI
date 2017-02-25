package model;

import manager.Sketch;
import processing.core.PImage;

/**
 * This class implements an actor that draws an image instead of a standard actor.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Face extends Actor {
	
	/** This is where the image we draw is stored */
	private static PImage face;
	
	/**
	 * This constructs the Face object using the constructor defined in Actor.
	 * It looks for the image in "assets/face.png"
	 * @param parent The parent sketch to draw to.
	 * @param guid The GUID for this game object.
	 */
	public Face(Sketch parent, int guid) {
		super(parent, guid);
		if(face == null){
			face = parent.loadImage("assets/face.png");
			face.resize(50, 50);
		}
	}

	/**
	 * This draws an actor with an image.
	 */
	@Override
	public void draw() {
		parent.pushMatrix();
		parent.fill(fill[0], fill[1], fill[2], fill[3]);
		parent.translate(position.x, position.y);
		parent.rotate((float) Math.toRadians(o));
		parent.image(face, -25, -25);
		parent.popMatrix();
	}

}
