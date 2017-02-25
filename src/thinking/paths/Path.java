package thinking.paths;

import java.util.Iterator;
import java.util.LinkedList;

import manager.Sketch;
import model.Animation;
import model.Circle;
import model.Line;
import model.Target;
import processing.core.PVector;

/**
 * This class models a path to follow for the AI component of a character.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Path implements Iterable<Target>{
	
	/** The last target on the path that was removed */
	private Target lastRemoved = null;
	/** The list of targets in order on the path */
	private LinkedList<Target> targets = new LinkedList<Target>();
	/** The number of vertices visited while making this path (for stats) */
	public int verticesVistedOnCreation = 0;
	/** The number of megabytes of memory used while making this path (for stats) */
	public long megsUsed = -1;
	
	/**
	 * This method add the given vector to the end of the list of targets.
	 * @param vector The vector to target-ify and add to the end of this path.
	 */
	public void add(PVector vector){
		targets.add(new Target(vector));
	}
	
	/**
	 * This method add the given vector to the start of the path.
	 * @param vector The vector to target-ify and add to the start of this path.
	 */
	public void addFirst(PVector vector){
		targets.addFirst(new Target(vector));
	}
	
	/**
	 * This will get and remove the first target on the path.
	 * @return The first target currently on the path.
	 */
	public Target removeFirst(){
		lastRemoved = targets.removeFirst();
		return lastRemoved;
	}
	
	/**
	 * This will determine whether the given target is the last one on the path.
	 * @param t The target to examine.
	 * @return True if the given target is currently the last one on the path.
	 */
	public boolean isLast(Target t){
		return targets.getLast().equals(t);
	}
	
	/**
	 * This method lets you draw the current state of the path, 
	 * and add animations for elements of the path that have recently been removed.
	 * @param parent The Sketch to add animations to and draw to.
	 * @return True if there is nothing left to draw.
	 */
	public boolean draw(Sketch parent){
		// The fill for lines and circles
		int[] fill = {255, 255, 255, 255};
		parent.fill(fill[0], fill[1], fill[2], fill[3]);
		Target prev = null;
		// Draw each target as a circle, and connect a line to the previous target
		for(Target t : this){
			if(prev != null){
				parent.stroke(fill[0], fill[1], fill[2], fill[3]);
				parent.line(prev.position.x, prev.position.y, t.position.x, t.position.y);
			}
			parent.noStroke();
			parent.ellipse(t.position.x, t.position.y, Circle.DIAMETER, Circle.DIAMETER);
			prev = t;
		}
		// Add a line and Circle animation for the last target removed, to the Sketch
		if(lastRemoved != null){
			if(!isEmpty())
				parent.animations.add(new Line(fill, 0, 120, 50, lastRemoved.position, targets.getFirst().position));
			parent.animations.add(new Circle(fill, 0, 120, 50, lastRemoved.position));
			lastRemoved = null;
		}
		return isEmpty() && (lastRemoved == null);
	}
	
	/**
	 * This will create and return a fade animation for all targets and connections currently in the path.
	 * @return A list of animations to return.
	 */
	public LinkedList<Animation> fadePath(){
		if(isEmpty())
			return null;
		LinkedList<Animation> list = new LinkedList<Animation>();
		int[] fill = {255, 255, 255, 255};
		Target prev = null;
		for(Target t : this){
			if(prev != null)
				list.add(new Line(fill, 0, 120, 50, prev.position, t.position));
			list.add(new Circle(fill, 0, 120, 50, t.position));
			prev = t;
		}
		if(lastRemoved != null){
			if(prev != null)
				list.add(new Line(fill, 0, 120, 50, prev.position, lastRemoved.position));
			list.add(new Circle(fill, 0, 120, 50, lastRemoved.position));
		}
		return list;
	}

	/**
	 * This will determine if there are any targets left.
	 * @return True if there are no more targets on the path.
	 */
	public boolean isEmpty() {
		return targets.isEmpty();
	}

	/**
	 * This gets the first target on the path and returns it.
	 * @return The current first target on this path.
	 */
	public Target getFirst() {
		return targets.getFirst();
	}

	/**
	 * This gets the last target on the path and returns it.
	 * @return The last target in the path.
	 */
	public Target getLast() {
		return targets.getLast();
	}

	/**
	 * This determines the length/size of the path.
	 * @return The number of targets in the path.
	 */
	public int size() {
		return targets.size();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Target> iterator() {
		return targets.iterator();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return targets.toString();
	}
}
