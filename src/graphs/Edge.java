package graphs;

import java.util.HashSet;

/**
 * This class outlines generic edge fields and methods.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public abstract class Edge {

	/** The first vertex in this edge */
	protected Vertex v0;
	/** The second vertex in this edge */
	protected Vertex v1;
	/** The cost associated with traveling this edge */
	protected float cost;
	
	/**
	 * The constructor for an edge links two vertices together.
	 * @param start The starting vertex.
	 * @param end The end/destination vertex.
	 * @param cost The cost associated with traveling this edge
	 * @param addToVertex A flag signaling whether or not to add this edge to each vertex's list of connected edges.
	 * 	NOTE: This behavior is not implemented in this class. It should be implemented in any extension of this
	 *  class appropriately.
	 */
	public Edge(Vertex v0, Vertex v1, float cost, boolean addToVertex){
		// Edges must be between two different vertices
		if(v0.equals(v1))
			throw new IllegalArgumentException("Edge must link two DIFFERENT verticies!");
		// Set fields
		this.v0 = v0;
		this.v1 = v1;
		this.cost = cost;
	}
	
	/**
	 * This method clears this edge and removes it from the master
	 * list of edges.
	 */
	protected void clear(HashSet<Edge> edges){
		// Remove myself from the master list
		edges.remove(this);
		// Forget my vertex's
		v0 = null;
		v1 = null;
	}
	
	/**
	 * This method checks if a vertex is apart of this edge.
	 * @param v The vertex to look for.
	 * @return True if the vertex is on this edge.
	 */
	public boolean hasVertex(Vertex v){
		return (v1.equals(v) || v0.equals(v));
	}
	
	
	// Getter methods
	
	
	
	/**
	 * This gets the cost associated with traversing this edge.
	 * @return The cost for traveling this edge as an integer value.
	 */
	public float getCost(){
		return cost;
	}
	
	/**
	 * This method will return the other vertex on this edge if the given
	 * one is a part of this edge.
	 * @param vertex The vertex (that should be on this edge) that determines which vertex (v0 or v1) to return.
	 * @return The opposite vertex of the given one, or null if the given one isn't a part of this edge.
	 */
	public Vertex getOtherVertex(Vertex vertex) {
		if(vertex.equals(v0))
			return v1;
		else if(vertex.equals(v1))
			return v0;
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((v0 == null) ? 0 : v0.hashCode());
		result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o){
		if(o == null)
			return false;
		if(!(o instanceof Edge))
			return false;
		Edge e = (Edge) o;
		
		boolean hasV1 = e.hasVertex(v1);
		boolean hasV0 = e.hasVertex(v0);
		return (hasV0 && hasV1);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return v0.toString() + " " + v1.toString() + " " + cost;
	}
}
