package model;

import java.util.LinkedList;
import manager.Sketch;
import processing.core.PVector;
import thinking.Blender;
import thinking.FlockAI;
import thinking.NormalAI;

/**
 * This class lets you create a flock of a Actor objects easily, manage
 * their AI, get properties of the flock, and directly access the leader.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Flock {

	/** How far apart to place the actors in X */
	private static final int X_OFFSET = Sketch.ACTOR_WIDTH + 7;
	/** How far apart to place the actors in Y */
	private static final int Y_OFFSET = Sketch.ACTOR_HEIGHT + 7;
	/** How many Actors to have per row */
	private static final int PER_ROW = 4;
	/** The list of members of the flock (LEADER EXCLUDED) */
	public LinkedList<GameObject> members = new LinkedList<GameObject>();
	/** The designated leader of the flock */
	public GameObject leader;
	
	
	/**
	 * This constructs a flock of a given size, tied to the given parent PApplet.
	 * @param parent The parent PApplet to tie the flock to.
	 * @param size How many members (leader included) to create the flock with.
	 */
	public Flock(Sketch parent, int size){
		// Create the leader, with normal AI, and the standard updater at the center
		leader = new Actor(parent, ((Sketch) parent).guidMarker++);
		leader.updater = ((Sketch) parent).u;
		leader.thinker = new NormalAI(leader);
		leader.setPos(Sketch.WIDTH / 2, Sketch.HEIGHT / 2);
		((Actor) leader).poopsBreadcrumbs = true;
		// Create variables to remember where the last Actor was placed
		int lastX = (Sketch.WIDTH / 2) + ((PER_ROW / 2) * X_OFFSET) + (X_OFFSET / 2);
		int lastY = Sketch.HEIGHT / 2;
		// Make the leader red
		((Actor) leader).setFill(255, 50, 50, 255);
		// Loop until size - 1 and place new flock Actors
		for(int i = 0 ; i < (size - 1) ; i++){
			Actor member = new Actor(parent, ((Sketch) parent).guidMarker++);
			// Move down and to the left on a new row
			if((i % PER_ROW) == 0){
				lastX -= (PER_ROW * X_OFFSET);
				lastY += Y_OFFSET;
			}
			// Set the new Actor's position
			member.setPos(lastX, lastY);
			// Update the last X posiiton we used
			lastX += X_OFFSET;
			member.updater = ((Sketch) parent).u;
			member.thinker = new FlockAI(this, member);
			member.blendingMode = Blender.WEIGHTED;
			((Actor) member).poopsBreadcrumbs = true;
			members.add(member);
		}
	}
	
	
	/**
	 * This method gets the center of mass of the flock as a position vector.
	 * @return The center of mass of the flock as a position vector, in PVector form.
	 */
	public PVector getCenterOfMass(){
		PVector avg = new PVector();
		for(GameObject g : members)
			avg.add(g.position);
		avg.add(leader.position);
		avg.div(members.size() + 1);
		return avg;
	}
	
	
	/**
	 * This method gets the average velocity of the flock as a velocity vector.
	 * @return The average velocity of the flock as a velocity vector, in PVector form.
	 */
	public PVector getAverageVelocity(){
		PVector avg = new PVector();
		for(GameObject g : members)
			avg.add(g.velocity);
		avg.add(leader.velocity);
		avg.div(members.size() + 1);
		return avg;
	}
	
	
	/**
	 * This method is an easy was to re-target every member's AI in one call.
	 */
	public void retargetAI(){
		for(GameObject g : members)
			((FlockAI) g.thinker).retarget();
	}
}
