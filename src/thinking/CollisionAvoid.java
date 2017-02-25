package thinking;

import java.util.Collection;
import model.GameObject;
import model.SimpleUpdater;
import processing.core.PVector;

/**
 * This class implements a Behavior for avoiding collisions.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class CollisionAvoid extends Behavior{
	
	/** The radius outside of which we ignore collisions */
	private static final float SAFE_DIST = 35;
	/** The farthest distance to look ahead */
	private static final float LOOKAHEAD = 30;
	/** The list of targets to avoid */
	private Collection<GameObject> targets;
	/** The character who needs to avoid collisions */
	private GameObject character;

	
	/**
	 * This method creates a CollsionAvoid behavior.
	 * @param targets The list of targets to avoid.
	 * @param character The character who needs to avoid collisions.
	 */
	public CollisionAvoid(Collection<GameObject> targets, GameObject character){
		this.targets = targets;
		this.character = character;
	}

	
	/**
	 * This method steps through the behavior until it finished.
	 */
	@Override
	public Output step() {
		// Initialize the minimum time to a super huge value
		// Set all the values we are looking for to null
		int minT = Integer.MAX_VALUE;
		GameObject minT_Target = null;
		PVector pointOfIntersect = null;
		PVector d_PosStore = null;
		PVector d_VelStore = null;
		
		// Look through every target in the list
		for(GameObject g: targets){
			// Compute relative position and velocity
			PVector d_Pos = PVector.sub(character.position, g.position);
			PVector d_Vel = PVector.sub(character.velocity, g.velocity);
			// Move to next target if the target is moving away, or will not collide outside our lookahead window
			if(d_Vel.mag() == 0) continue;
			float timeAtClosest = (float) (-(PVector.dot(d_Pos, d_Vel)) / Math.pow(d_Vel.mag(), 2));
			if((timeAtClosest < 0) || (timeAtClosest > LOOKAHEAD)) continue;
			// Set a deltaT for one step in the future
			int deltaT = 1;
			PVector c_PredictedPos = PVector.add(character.position, PVector.mult(character.velocity, 1));
			PVector t_PredictedPos = PVector.add(g.position, PVector.mult(g.velocity, 1));
			// Keep looking ahead in the future until we are outside the lookahead window or we find an important collision
			for(int t = 2 ; (PVector.sub(c_PredictedPos, t_PredictedPos).mag() > SAFE_DIST) && (t < LOOKAHEAD); t++){
				c_PredictedPos.add(PVector.mult(character.velocity, t));
				t_PredictedPos.add(PVector.mult(g.velocity, t));
				deltaT = t;
			}
			// If we found an important collision remember things about it
			if((deltaT < minT) && (PVector.sub(c_PredictedPos, t_PredictedPos).mag() < SAFE_DIST)){
				minT = deltaT;
				pointOfIntersect = PVector.sub(PVector.sub(c_PredictedPos, t_PredictedPos), character.position);
				minT_Target = g;
				d_PosStore = d_Pos;
				d_VelStore = d_Vel;
			}
		}
		PVector relativePos = null;
		// If nothing important was found, do nothing
		if((minT == Integer.MAX_VALUE) || (pointOfIntersect == null))
			return new Output();
		// If the collision point is essentially at the character, GTFO
		else if(PVector.sub(pointOfIntersect, character.position).mag() < 0.01)
			relativePos = PVector.sub(minT_Target.position, character.position);
		// Scale relativePos to how for in the future it is
		else
			relativePos = PVector.add(d_PosStore, d_VelStore).div(minT);
		SimpleUpdater.clipToMaxAcceleration(relativePos);
		// Face to relativePos (if we can)
		float newO = (float) Math.toDegrees(-Math.atan(relativePos.x / relativePos.y));
		if(relativePos.y > 0)
			newO += 180;
		if(character.thinker instanceof NormalAI)
			((NormalAI) character.thinker).kinematicFace(newO);
		return new Output(null, null, relativePos, 0, 0, 0);
	}

	
	@Override
	public GameObject getTarget() {
		return null;
	}
}
