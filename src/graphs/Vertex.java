package graphs;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class wraps up fields and behavior related to a vertex in our graph.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Vertex implements Comparable<Vertex> {
	
	/** The ID of this vertex */
	private int id;
	/** The set of edges incident to this vertex */
	public LinkedList<Edge> edges = new LinkedList<Edge>();
	/** A way of knowing what type of edges to build */
	private boolean undirected = false;
	/** A way to remember if the type of edge has been set */
	private boolean edgeTypeSet = false;
	
	/**
	 * This constructs a new vertex with the given ID.
	 * @param id This vertex's integer ID.
	 */
	public Vertex(int id){
		this.id = id;
	}
	
	/**
	 * This method adds an edge to this vertex's set of incident edges.
	 * NOTE: The type of edges (directed or undirected) that this vertex accepts,
	 * is determined by the first edge added, and cannot be changed after.
	 * @param e The Edge object to add.
	 */
	protected void addEdge(Edge e){
		if(!edgeTypeSet){
			if(e instanceof UndirectedEdge)
				undirected = true;
		}
		else if((undirected && (e instanceof DirectedEdge)) || (!undirected && (e instanceof UndirectedEdge)))
			throw new IllegalArgumentException("Invalid edge type!" );
		if(!edges.contains(e))
			edges.add(e);
		
	}
	
	
	// Getter methods
	
	
	/** 
	 * A simple getter for the vertex's ID.
	 * @return The ID of this vertex.
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * A simple getter for the vertex's degree (number of incident edges).
	 * @return The degree of this vertex.
	 */
	public int getDegree(){
		return edges.size();
	}
	
	/**
	 * This looks for an edge with the given vertex.
	 * @param v The vertex to find an edge with.
	 * @return The Edge object that contains this and the given vertex.
	 */
	public Edge getEdgeWith(Vertex v){
		if(undirected){
			Edge temp = new UndirectedEdge(this, v, 0, false);
			for(Edge e : edges){
				if(e.equals(temp))
					return e;
			}
			return null;
		}
		else{
			Edge temp = new DirectedEdge(this, v, 0, false);
			for(Edge e : edges){
				if(e.equals(temp))
					return e;
			}
			return null;
		}
	}
	
	
	// Edge remover methods
	
	/**
	 * This method just removes a given edge from this vertex's set of edges.
	 * @param e The edge to remove.
	 * @return True if the edge was removed.
	 */
	protected boolean justRemove(Edge e){
		return edges.remove(e);
	}
	
	/**
	 * This method removes an edge from this vertex's set of edges, while
	 * also removing the same edge from the vertex on the other end of the
	 * edge.
	 * @param e The edge to remove.
	 * @return True if the edge was removed.
	 */
	protected boolean remove(Edge e){
		if(undirected){
			boolean b = e.getOtherVertex(this).justRemove(e);
			return edges.remove(e) && b;
		}
		else{
			Vertex other = e.getOtherVertex(this);
			DirectedEdge otherWay = new DirectedEdge(other, this, 0, false);
			other.justRemove(otherWay);
			return edges.remove(e);
		}
	}
	
	/**
	 * This method removes all the edges connected to this vertex, and also clears the edge
	 * which consequently removes it from the master list in VertexCoverApproximation.
	 */
	protected void removeFromGraph(HashSet<Edge> graph){
		Iterator<Edge> it = graph.iterator();
		if(undirected){
			while(it.hasNext()){
				Edge e = it.next();
				// Remove the current edge from the other vertex connected
				e.getOtherVertex(this).justRemove(e);
				// Remove the current edge from my set of edges
				it.remove();
				// Clear the current edge (removing it from the master list)
				e.clear(graph);
			}
		}
		else{
			while(it.hasNext()){
				DirectedEdge e = (DirectedEdge) it.next();
				if(e.getDestination().equals(this)){
					e.getStart().justRemove(e);
					it.remove();
					e.clear(graph);
				}
				else if(e.getStart().equals(this)){
					it.remove();
					e.clear(graph);
				}
			}
		}
		
		edges.clear();
	}
	
	
	// Overrides for generic object methods
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Vertex))
			return false;
		return (this.id == ((Vertex) o).getID());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Vertex v) {
		if(this.getDegree() == v.getDegree())
			return 0;
		if(this.getDegree() < v.getDegree())
			return -1;
		else
			return 1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return "" + id;
	}
}
