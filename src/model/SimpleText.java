package model;

import manager.Sketch;

/**
 * This class implements a simple way to draw text in the game world.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class SimpleText extends GameObject {

	/** The text to draw */
	private String text;
	
	/**
	 * This constructs the simple text object with the given text.
	 * @param parent The parent sketch to draw to.
	 * @param guid the GUID for this game object.
	 * @param text The text to display.
	 */
	public SimpleText(Sketch parent, int guid, String text) {
		super(parent, guid);
		this.text = text;
		scale  = 1.0f;
	}
	
	/**
	 * This changes the text that is drawn.
	 * @param text The new text to draw.
	 */
	public void setText(String text){
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * @see model.GameObject#draw()
	 */
	@Override
	public void draw() {
		parent.pushMatrix();
		parent.translate(position.x, position.y);
		parent.scale(scale);
		parent.rotate(o);
		parent.fill(fill[0], fill[1], fill[2], fill[3]);
		parent.text(text, 0, 0);
		parent.popMatrix();
	}

}
