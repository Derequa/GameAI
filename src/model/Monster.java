package model;

import manager.Sketch;
import processing.core.PImage;

/**
 * This class wraps and Actor to implement a simple Monster for the demo.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Monster extends Actor {
	
	/** A constant defining how close the monster has to get to the player to eat it */
	public static final int EATRADIUS = 30;
	/** A constant defining how far away from itself the monster can see */
	public static final int LOOKRADIUS = 115;
	/** A constant defining at what point the monster will seek the fridge instead of wandering */
	public static final int HUNGER_THRESHOLD = 480;
	/** A constant defining how much the hunger increases each call to step */
	public static final int HUNGER_STEP = 1;
	private static final int IMAGE_SWITCH = 15;
	private PImage[] danceSprites = new PImage[6];
	private PImage sprite;
	private int danceSpriteTimer = 0;
	private int currentSpriteIndex = 0;
	public boolean isDancing = false;
	/** The monster's current level of hunger */
	private int currenthunger = 0;

	/**
	 * This constructs the monster's actor object.
	 * @param parent The parent sketch to draw in.
	 * @param guid The GUID for this monster.
	 */
	public Monster(Sketch parent, int guid) {
		super(parent, guid);
		for(int i = 0 ; i < 6 ; i++){
			danceSprites[i] = parent.loadImage("assets/monster_vic" + i + ".png");
			danceSprites[i].resize(35, 35);
		}
		sprite = parent.loadImage("assets/monster.png");
		sprite.resize(35, 35);
		
	}
	
	/**
	 * This will report whether or not the monster needs food.
	 * @return True if the monster needs to eat.
	 */
	public boolean needsFood(){
		return currenthunger > HUNGER_THRESHOLD;
	}
	
	/**
	 * This will reset the monster's current hunger level.
	 */
	public void resetHunger(){
		currenthunger = 0;
	}
	
	/**
	 * This will increase the monster's hunger by the stepping amount.
	 */
	public void stepHunger(){
		currenthunger += HUNGER_STEP;
	}
	
	/*
	 * (non-Javadoc)
	 * @see model.Actor#draw()
	 */
	@Override
	public void draw(){
		float r = scale * scalefactor;
		parent.pushMatrix();
		parent.translate(position.x, position.y);
		
		// Draw the ellipse and orientation indicator
		parent.pushMatrix();
		parent.fill(255, 20, 20, 80);
		parent.rotate((float) Math.toRadians(o));
		parent.ellipse(0, 0, 2 * LOOKRADIUS, 2 * LOOKRADIUS);
		parent.fill(225, 125, 50, 255);
		parent.ellipse(0, 0, 2 * EATRADIUS, 2 * EATRADIUS);
		parent.translate(0, (float)-1.55 * r);
		parent.fill(20, 20, 255, 150);
		parent.triangle(-r, 0, r, 0, 0, -2 * r);
		parent.popMatrix();
		
		// Draw the sprite
		parent.pushMatrix();
		parent.rotate((float) Math.toRadians(o));
		parent.translate(-(sprite.width / 2), -(sprite.height / 2));
		
		parent.noStroke();
		// Change sprites if dancing
		if(isDancing){
			parent.image(danceSprites[currentSpriteIndex], 0, 0);
			if((danceSpriteTimer++ % IMAGE_SWITCH) == 0)
				currentSpriteIndex++;
			if(currentSpriteIndex >= danceSprites.length)
				currentSpriteIndex = 0;
		}
		else
			parent.image(sprite, 0, 0);
		
		parent.popMatrix();
	
		parent.popMatrix();
	}

}
