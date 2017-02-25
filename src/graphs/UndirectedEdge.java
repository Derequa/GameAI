package graphs;

/**
 * This class represents an undirected edge in the graph.
 * This will add itself to both vertices list of edges if told to add itself.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class UndirectedEdge extends Edge {

	/**
	 * This implements a constructor for an undirected edge.
	 * If the addToVertex parameter is true, this edge will add itself to both
	 * vertices it connects.
	 * @param v0 The first vertex on this edge.
	 * @param v1 The second vertex on this edge.
	 * @param cost The cost associated with traveling this edge.
	 * @param addToVertex Whether or no to add this edge to each vertex's list of connected edges.
	 */
	public UndirectedEdge(Vertex v0, Vertex v1, float cost, boolean addToVertex) {
		super(v0, v1, cost, addToVertex);
		// Store vertices in order of ID
		if(v0.getID() < v1.getID()){
			this.v0 = v0;
			this.v1 = v1;
		}
		else{
			this.v0 = v1;
			this.v1 = v0;
		}
		// Add this edge to each vertex's set of edges
		if(addToVertex){
			v0.addEdge(this);
			v1.addEdge(this);
		}
	}
	
	/**
	 * This gets the first vertex on this edge.
	 * @return The first vertex on this edge.
	 */
	public Vertex getV0(){
		return v0;
	}
	
	/**
	 * This gets the second vertex on this edge.
	 * @return The second vertex on this edge.
	 */
	public Vertex getV1(){
		return v1;
	}
	
	/**
	 * This method checks if a vertex is apart of this edge.
	 * @param v The vertex to look for.
	 * @return True if the vertex is on this edge.
	 */
	public boolean hasVertex(Vertex v){
		return (v0.equals(v) || v1.equals(v));
	}

}
