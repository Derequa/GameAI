package thinking.paths;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import graphs.*;
import graphs.Heuristic.H_MODE;

/**
 * This class contains methods for path finding,
 * using Dijkstra's algorithm and A*.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class PathFinding {
	
	/** The translator for this instance of PathFinding, used to quantize points and localize vertices */
	public Translator translator;
	
	/**
	 * This constructs a PathFinding object with the given parameters.
	 * @param t The translator to use for this PathFinding instance.
	 * @param g The graph we are working on.
	 */
	public PathFinding(Translator t){
		translator = t;
	}
	
	/**
	 * This method runs Dijkstra's algorithm to determine the shortest path to the goal.
	 * NOTE: The start and goal vertices MUST exist in the same graph, and that graph must 
	 * be the same as the one currently defined in the Translator object for this instance.
	 * @param start Where the algorithm starts.
	 * @param goal Where to compute the shortest path to.
	 * @return A path object representing the shortest path we find.
	 */
	public Path dijkstras(Vertex start, Vertex goal){
		// Create the open list, closed list, and path map
		LinkedList<SortableVertex> openSet = new LinkedList<SortableVertex>();
		HashSet<Vertex> closedSet = new HashSet<Vertex>();
		HashMap<Vertex, Vertex> path = new HashMap<Vertex, Vertex>();
		
		Runtime rt = Runtime.getRuntime();
		long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 /1024;
		openSet.add(new SortableVertex(start, 0));
		while(!openSet.isEmpty()){
			// Sort the open list by cost so far, and get the lowest
			Collections.sort(openSet);
			SortableVertex sv_x = openSet.removeFirst();
			Vertex v_x = sv_x.v;
			
			// Add the current vertex to the closed list
			closedSet.add(v_x);
			// Check if we have arrived at the goal
			if(v_x.equals(goal)){
				long current = (rt.totalMemory() - rt.freeMemory()) / 1024 /1024;
				if(current > usedMB)
					usedMB = current;
				return rebuildPath(path, start, v_x, closedSet.size(), usedMB);
			}
			
			// Examine all neighbors of this vertex
			for(Edge e : v_x.edges){
				// Skip vertices on the closed list
				Vertex v_y = e.getOtherVertex(v_x);
				if(closedSet.contains(v_y))
					continue;
				// Add to the open list if not present
				// Replace the value on the open list if there is a lower cost so far
				SortableVertex sv_y = new SortableVertex(v_y, sv_x.csf + e.getCost());
				int index = openSet.indexOf(sv_y);
				if(index >= 0){
					SortableVertex sv_y_old = openSet.get(index);
					if(sv_y.csf < sv_y_old.csf){
						openSet.add(index, sv_y);
						path.put(v_y, v_x);
					}
				}
				else{
					openSet.add(sv_y);
					path.put(v_y, v_x);
				}
				long current = (rt.totalMemory() - rt.freeMemory()) / 1024 /1024;
				if(current > usedMB)
					usedMB = current;
			}
			
		}
		return null;
	}
	
	/**
	 * This method implements the A* algorithm and gets the shortest path to the goal
	 * using the given heuristic method.
	 * @param start The vertex to start searching at.
	 * @param goal The vertex to find the shortest path to.
	 * @param heuristic The type of heuristic to use.
	 * @return A path object representing the shortest path to the goal.
	 */
	public Path aStar(Vertex start, Vertex goal, H_MODE heuristic){
		// Create the open list, closed list, and path map
		LinkedList<SortableVertex> openSet = new LinkedList<SortableVertex>();
		HashSet<Vertex> closedSet = new HashSet<Vertex>();
		HashMap<Vertex, Vertex> path = new HashMap<Vertex, Vertex>();
		
		Runtime rt = Runtime.getRuntime();
		long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 /1024;
		openSet.add(new SortableVertex(start, 0, Heuristic.getHeuristic(start, goal, translator, heuristic)));
		while(!openSet.isEmpty()){
			// Sort the open list by f-score, and get the vertex with the lowest
			Collections.sort(openSet);
			SortableVertex sv_x = openSet.removeFirst();
			Vertex v_x = sv_x.v;
			
			// Add the current vertex to the closed list
			closedSet.add(v_x);
			// Check if we are at the goal
			if(v_x.equals(goal)){
				long current = (rt.totalMemory() - rt.freeMemory()) / 1024 /1024;
				if(current > usedMB)
					usedMB = current;
				return rebuildPath(path, start, v_x, closedSet.size(), usedMB);
			}
			
			// Look at all the neighbors of this vertex
			for(Edge e : v_x.edges){
				// Ignore vertices on the closed list
				Vertex v_y = e.getOtherVertex(v_x);
				if(closedSet.contains(v_y))
					continue;
				
				SortableVertex sv_y = new SortableVertex(v_y, sv_x.csf + e.getCost(), Heuristic.getHeuristic(v_y, goal, translator, heuristic));
				
				// Add neighbors to the open list if not there
				// Replace neighbors on the open list if there is a lower f-score from this vertex
				int index = openSet.indexOf(sv_y);
				if(index >= 0){
					SortableVertex sv_y_old = openSet.get(index);
					if(sv_y.fscore < sv_y_old.fscore){
						openSet.add(index, sv_y);
						path.put(v_y, v_x);
					}
				}
				else{
					openSet.add(sv_y);
					path.put(v_y, v_x);
				}
				long current = (rt.totalMemory() - rt.freeMemory()) / 1024 /1024;
				if(current > usedMB)
					usedMB = current;
			}
		}
		return null;
	}
	
	// The method will rebuild the path from start to goal, using a path map
	private Path rebuildPath(HashMap<Vertex, Vertex> path, Vertex start, Vertex current, int nodesVisited, long maxmem){
		Path p  = new Path();
		p.addFirst(translator.localize(current));
		for(Vertex v = path.get(current) ; !(v == null) && !v.equals(start) ; v = path.get(v))
			p.addFirst(translator.localize(v));
		p.addFirst(translator.localize(start));
		p.verticesVistedOnCreation = nodesVisited;
		p.megsUsed = maxmem;
		return p;
	}
	
	/**
	 * This class wraps a Vertex object with parameters like Heuristic values, cost so far, and fscore.
	 * It combines this with an implementation of the Comparable interface so that it can be properly sorted
	 * with java's Collections class.
	 * @author Derek Batts - dsbatts@ncsu.edu
	 *
	 */
	private class SortableVertex implements Comparable<SortableVertex> {
		
		/** The vertex wrapped in this class */
		Vertex v;
		/** The cost so far for this vertex */
		float csf;
		/** The heuristic value for this vertex */
		@SuppressWarnings("unused")
		float h;
		/** The f-score (heuristic + cost so far) for this vertex */
		float fscore;
		
		/**
		 * This constructs a wrapped vertex.
		 * @param v The vertex to wrap.
		 * @param csf The cost so far for this vertex.
		 */
		public SortableVertex(Vertex v, float csf){
			this(v, csf, 0);
		}
		
		/**
		 * This constructs a wrapped vertex.
		 * @param v The vertex to wrap.
		 * @param csf The cost so far for this vertex.
		 * @param h The heuristic value for this vertex.
		 */
		public SortableVertex(Vertex v, float csf, float h){
			this.v = v;
			this.csf = csf;
			this.h = h;
			fscore = csf + h;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(SortableVertex arg0) {
			if(this.fscore == arg0.fscore)
				return 0;
			if(this.fscore < arg0.fscore)
				return -1;
			else
				return 1;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object o){
			if(o == null)
				return false;
			else if(o instanceof SortableVertex)
				return ((SortableVertex) o).v.equals(this.v);
			return false;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString(){
			return v.toString() + " " + csf;
		}
		
	}
}
