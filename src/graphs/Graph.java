package graphs;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import manager.Settings;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * This class models a simple graph that can do stuff.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Graph implements Iterable<Edge>{
	
	/** A list of all the edges in the graph */
	HashSet<Edge> edges = new HashSet<Edge>();
	/** A list of all the vertices in the graph. */
	ArrayList<Vertex> verts = new ArrayList<Vertex>();
	/** Whether or not this graph is undirected. */
	private boolean undirected;

	/**
	 * This constructs a graph from a file.
	 * @param f The file containing a list of edges.
	 * @param undirected Whether or not this graph is undirected.
	 */
	public Graph(File f, boolean undirected) {
		this.undirected = undirected;
		try {
			readFile(f, undirected);
		} catch (FileNotFoundException e) {
			Settings.fail("Unable to read graph file!");
			e.printStackTrace();
		}
	}
	
	/**
	 * This constructs an empty graph.
	 * @param undirected Whether or not this graph is undirected.
	 */
	public Graph(boolean undirected){
		this.undirected = undirected;
	}
	
	/**
	 * This will determine the number of vertices in the graph.
	 * @return The number of vertices in the graph.
	 */
	public int getNumberOfVertices(){
		return verts.size();
	}
	
	/**
	 * This will determine how many edges are in the graph.
	 * @return The number of edges in the graph.
	 */
	public int getNumberOfEdges(){
		return edges.size();
	}
	
	/**
	 * This method will add the given vertex to the graph.
	 * @param v The vertex to add to the graph.
	 */
	public void addVertex(Vertex v){
		verts.add(v);
	}
	
	/**
	 * This method will add the given edge to the graph.
	 * @param e The edge to add to the graph.
	 */
	public void addEdge(Edge e){
		edges.add(e);
	}
	
	/**
	 * This will determine if the given vertex exists in the graph.
	 * @param v The vertex to look for.
	 * @return True if the given vertex is in the graph.
	 */
	public boolean hasVertex(Vertex v){
		return verts.contains(v);
	}
	
	/**
	 * This method will get the edge in the graph with the given vertices.
	 * NOTE: if the graph is in undirected mode, ordering of vertices will not matter.
	 * @param start The starting vertex in the edge we are looking for.
	 * @param dest The destination vertex in the edge we are looking for.
	 * @return The edge with the given vertices. (if it exists in the graph)
	 */
	public Edge getEdge(Vertex start, Vertex dest){
		Edge temp = null;
		if(undirected)
			temp = new UndirectedEdge(start, dest, 0, false);
		else
			temp = new DirectedEdge(start, dest, 0, false);
		return getEdge(temp);
	}
	
	/**
	 * This method will get the edge in the graph equal to the given edge.
	 * @param e The edge to look for.
	 * @return The edge in this graph equal to the given edge.
	 */
	public Edge getEdge(Edge e){
		for(Edge edge : edges){
			if(edge.equals(e))
				return edge;
		}
		return null;
	}
	
	/**
	 * This method will get the vertex with the given ID in the graph (if it exists)
	 * @param id The ID of the vertex to look for.
	 * @return The vertex with the given ID.
	 */
	public Vertex getVertex(int id){
		int index = verts.indexOf(new Vertex(id));
		if(index < 0)
			return null;
		return verts.get(index);
	}
	
	/**
	 * This method will determine if the given edge exists in the graph.
	 * @param e The edge to look for.
	 * @return True if the edge exist in the graph.
	 */
	public boolean hasEdge(Edge e){
		return edges.contains(e);
	}
	
	/**
	 * This class removes the given edge (if present in the graph)
	 * @param e The edge to remove.
	 */
	public void removeEdge(Edge e){
		if(e instanceof DirectedEdge)
			((DirectedEdge) e).getStart().remove(e);
		else
			((UndirectedEdge) e).getV0().remove(e);
		e.clear(edges);
	}
	
	/**
	 * This method allows for removing of a given vertex.
	 * @param v The vertex to remove (if present in the graph)
	 */
	public void removeVertex(Vertex v){
		verts.remove(v);
		v.removeFromGraph(edges);
	}
	
	/**
	 * Make and return an iterator for all the edges in the graph.
	 */
	public Iterator<Edge> iterator(){
		return edges.iterator();
	}
	
	/**
	 * Make and return an iterator for all the vertexes in the graph.
	 */
	public Iterator<Vertex> vertexIterator(){
		return verts.iterator();
	}
	
	/**
	 * This method draws a representation of this graph to the given sketch.
	 * @param sketch The sketch to draw this graph to.
	 * @param t The translator for localizing vertices.
	 */
	public void draw(PApplet sketch, Translator t) {
		sketch.stroke(0, 200, 200);
		for(Edge e : this){
			PVector v0 = t.localize(e.v0);
			PVector v1 = t.localize(e.v1);
			sketch.fill(0, 200, 200, 150);
			sketch.ellipse(v0.x, v0.y, 15, 15);
			sketch.ellipse(v1.x, v1.y, 15, 15);
			sketch.line(v0.x, v0.y, v1.x, v1.y);
			sketch.fill(255, 255, 255, 255);
			sketch.text(e.v0.toString(), v0.x, v0.y);
			sketch.text(e.v1.toString(), v1.x, v1.y);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String build = "";
		
		for(Vertex v :  verts){
			build += v + " - [ ";
			for(Edge e : v.edges){
				build += e + " ";
			}
			build += "]\n";
		}
		return build;
	}
	
	// This method will read in a file of edges and build the graph accordingly
	private void readFile(File f, boolean undirected) throws FileNotFoundException{
		// Try to make a scanner for the file
		Scanner s = new Scanner(f);
		String failMessage = "Each line of the file must specify three in values : Start ID, Destination ID, and Edge cost";

		// Loop while pairs of integers are available in the file
		while(s.hasNextLine()){
			String lineText = s.nextLine();
			Scanner line = new Scanner(lineText);
			// Make a vertex object from the number
			
			if(!line.hasNextInt()){
				stopReading(line, s, failMessage);
				return;
			}
			
			Vertex start = new Vertex(line.nextInt());
			
			// Look for that vertex ID in our list of vertices
			int index1 = verts.indexOf(start);
			// If the vertex is in our list already, get it
			if(index1 >= 0)
				start = verts.get(index1);
			// Otherwise add it to the list
			else 
				verts.add(start);
			
			// Look for a second int, failing if it isn't available
			if(!line.hasNextInt()){
				stopReading(line, s, failMessage);
				return;
			}
			
			// Go through the same look-up/checking process with the second vertex
			Vertex dest = new Vertex(line.nextInt());
			int index2 = verts.indexOf(dest);
			if(index2 >= 0)
				dest = verts.get(index2);
			else
				verts.add(dest);
			
			float cost = 1;
			
			if(line.hasNextFloat())
				cost = line.nextFloat();
			if(cost < 1)
				cost = 1;

			// Make an edge with both vertices and add that edge to our list
			if(undirected)
				edges.add(new UndirectedEdge(dest, start, cost, true));
			else
				edges.add(new DirectedEdge(start, dest, cost, true));
			line.close();
		}
		// Close the file scanner
		s.close();
	}
	
	// This method helps with ending reading of a file
	private void stopReading(Scanner close0, Scanner close1, String reason){
		close0.close();
		close1.close();
		Settings.fail(reason);
		verts.clear();
		edges.clear();
	}

}
