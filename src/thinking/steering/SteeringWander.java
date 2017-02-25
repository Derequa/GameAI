package thinking.steering;

import java.util.Random;

import model.GameObject;
import model.Target;
import processing.core.PVector;
import thinking.Behavior;
import thinking.NormalAI;
import thinking.Output;

/**
 * This class defines the Wander steering behavior.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class SteeringWander extends Behavior {
	
	/** The distance to project a point in front of the character */
	private float PROJ_POINT_DIST = 10;
	/** The radius around the projected point to pick a seek point */
	private float SEEK_POINT_RAD = 4;
	/** How often to change direction */
	private static final int CHANGETIMER = 75;
	/** A counter for determining when to change */
	private int counter = -1;
	/** How long to keep picking new targets */
	private int life = 600;
	/** The character to make wander */
	private GameObject character;
	/** The last SteeringSeek operation we made */
	private SteeringSeek lastSeekOp = null;
	
	
	/**
	 * This creates a new SteeringWander behavior.
	 * @param character The character to make wander.
	 * @param time How long the character should wander for. (-1 for infinite)
	 */
	public SteeringWander(GameObject character, int time){
		this.character = character;
		if(time > 0)
			life = time;
		else
			life = -1;
	}

	
	/**
	 * This method steps the behavior until it finishes.
	 */
	@Override
	public Output step() {
		// Increment counter and check if we are done.
		counter++;
		if((life != -1) && (counter > life)){
			finished = true;
			return new Output();
		}
		// If it's time to change direction, do so
		else if((counter % CHANGETIMER) == 0){
			// Project a point in front of us
			float x = (float) Math.sin(Math.toRadians(character.o)) * PROJ_POINT_DIST;
			float y = (float) -Math.cos(Math.toRadians(character.o)) * PROJ_POINT_DIST;
			PVector projectedPoint = (new PVector(x, y)).add(character.position);
			// Pick a random angle
			Random r = new Random();
			float angle = r.nextInt(360);
			// Project a second point a given distance away from the first point at the random angle
			PVector seekPoint = (new PVector((float) Math.sin(Math.toRadians(angle)), (float) -Math.cos(Math.toRadians(angle)))).mult(SEEK_POINT_RAD);
			seekPoint.add(projectedPoint);
			// Seek the second point
			Target t = new Target();
			t.position = seekPoint;
			((NormalAI) character.thinker).removeBehavior(lastSeekOp);
			lastSeekOp = (SteeringSeek) Steering.seek(t, character);
			((NormalAI) character.thinker).steeringSeek(lastSeekOp);
			return new Output();
		}
		// Otherwise do nothing
		else
			return new Output();
	}

	
	@Override
	public GameObject getTarget() {
		return null;
	}

}
