package graphs;

/**
 * This class implements a directed edge in a graph.
 * If told to add edges to vertices, it will only add itself to the starting vertex.
 * @author Derek Batts - dsabtts@ncsu.edu
 *
 */
public class DirectedEdge extends Edge{

	/**
	 * This implements a constructor for an directed edge.
	 * If the addToVertex parameter is true, this edge will add itself to only the
	 * start vertex's list of edges.
	 * @param start The first vertex on this edge.
	 * @param destination The second vertex on this edge.
	 * @param cost The cost associated with traveling this edge.
	 * @param addToVertex Whether or no to add this edge to the start vertex's list of connected edges.
	 */
	public DirectedEdge(Vertex start, Vertex destination, float cost, boolean addToVertex) {
		super(start, destination, cost, addToVertex);
		// Add this edge to the start vertex's set of edges
		if(addToVertex)
			v0.addEdge(this);
	}
	
	/**
	 * A simple getter method for the starting vertex associated with an edge.
	 * @return The starting vertex tied to this edge.
	 */
	public Vertex getStart(){
		return v0;
	}
	
	/**
	 * A simple getter method for the destination vertex associated with an edge.
	 * @return The destination vertex tied to this edge.
	 */
	public Vertex getDestination(){
		return v1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see graphs.Edge#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o){
		if((o == null) || !(o instanceof DirectedEdge))
			return false;
		DirectedEdge e = (DirectedEdge) o;
		return e.getStart().equals(v0) && e.getDestination().equals(v1);
	}

}
