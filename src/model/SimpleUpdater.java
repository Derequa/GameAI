package model;

import manager.Sketch;
import processing.core.PVector;
import thinking.NormalAI;

/**
 * This class is used to update GameObject of varying types.
 * It also provides methods for some common calculations.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class SimpleUpdater extends Updater {
	
	/**
	 * This method updates any GameObject based on known types, according to
	 * it's parameters.
	 * @param g The GameObject to updater.
	 */
	public void update(GameObject g){
		// Do breadcrumb specific updates if the object is a breadcrumb
		if(g instanceof BreadCrumb){
			updateBreadCrumb((BreadCrumb) g);
			return;
		}
		// Do Actor specific updates if the object is an actor
		else if(g instanceof Actor)
			updateActor((Actor) g);
		
		
		// Add acceleration to velocity
		g.velocity.add(g.acceleration);
		// Check if the velocity is past max and clip if so
		float vMag = (float) Math.sqrt((g.velocity.x * g.velocity.x) + (g.velocity.y * g.velocity.y));
		if(vMag > MAX_VELOCITY)
			SimpleUpdater.clipToMaxVelocity(g.velocity);
		// Add velocity to position
		g.position.add(g.velocity);
		g.angV += g.angA;
		g.o += g.angV;
		
		// Check if we hit walls in X and reverse velocity and direction if we do
		boolean bounced = false;
		if((g.position.x < 0) || (g.position.x > Sketch.WIDTH)){
			if(g.position.x < 0)
				g.position.x = 0;
			else if(g.position.x > Sketch.WIDTH)
				g.position.x = Sketch.WIDTH;
			g.velocity.x *= -1;
			g.acceleration.x *= -1;
			bounced = true;
		}
		// Check if we hit walls in Y and reverse velocity and direction if we do
		if((g.position.y < 0) || (g.position.y > Sketch.HEIGHT)){
			if(g.position.y < 0)
				g.position.y = 0;
			else if(g.position.y > Sketch.HEIGHT)
				g.position.y = Sketch.HEIGHT;
			g.velocity.y *= -1;
			g.acceleration.y *= -1;
			bounced = true;
		}
		// If we bounced off a wall, re-orient the character
		if((bounced) && (g.thinker instanceof NormalAI)){
			Target t = new Target();
			PVector newPos = PVector.add(g.position, PVector.add(g.velocity, g.acceleration));
			t.position = newPos;
			((NormalAI) g.thinker).kinematicFace(t);
		}
	}
	
	
	// PRIVATE HELPER METHODS
	
	
	/**
	 * This method handles the updates specific to the Actor class.
	 * @param a The Actor to update.
	 */
	private void updateActor(Actor a){
		// Check if we should drop a bread-crumb
		if(a.poopsBreadcrumbs && ((a.counter++ % Actor.POOPER_TIMER) == 0) && (a.velocity.mag() > 0)){
			// Drop a bread-crumb
			BreadCrumb b = new BreadCrumb(a.parent, ((Sketch) a.parent).guidMarker++, a.updater);
			b.setPos(a.position.x, a.position.y);
			((Sketch) a.parent).addObject(b);;
		}
	}
	
	
	/**
	 * This method handles the updates specific to the BreadCrumb class.
	 * @param b The BreadCrumb to update.
	 */
	private void updateBreadCrumb(BreadCrumb b){
		// Increment the age
		b.age++;
		// Check age
		if(b.age > BreadCrumb.MAX_AGE){
			((Sketch) b.parent).removeObject(b);
			return;
		}
		// Fade if past certain threshold
		else if(b.age >= (BreadCrumb.MAX_AGE - BreadCrumb.FADE_DURATION)){
			int scale = BreadCrumb.FADE_DURATION - (BreadCrumb.MAX_AGE - b.age);
			scale  = BreadCrumb.FADE_DURATION - scale;
			float res =  ((float) scale / BreadCrumb.FADE_DURATION);
			b.alpha = res * 255;
		}
	}
}
