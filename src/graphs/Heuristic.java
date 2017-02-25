package graphs;

import processing.core.PVector;

public class Heuristic {
	
	public enum H_MODE{
		EUCLIDEAN,
		MANHATTAN,
		CLUSTER
	}
	
	public static float getHeuristic(Vertex current, Vertex goal, Translator t, H_MODE mode){
		switch(mode){
			case EUCLIDEAN:		return euclideanDistance(current, goal, t);
			case MANHATTAN:		return manhattanDistance(current, goal, t);
			case CLUSTER:		break;
		}
		return 0.0f;
	}
	
	public static float euclideanDistance(Vertex current, Vertex goal, Translator t){
		PVector currentPosition = t.localize(current);
		PVector goalPosition = t.localize(goal);
		return PVector.dist(currentPosition, goalPosition);
	}
	
	public static float manhattanDistance(Vertex current, Vertex goal, Translator t){
		PVector currentPosition = t.localize(current);
		PVector goalPosition = t.localize(goal);
		return Math.abs(goalPosition.x - currentPosition.x) + Math.abs(goalPosition.y - currentPosition.y);
	}
}
