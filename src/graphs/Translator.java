package graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import manager.Settings;
import processing.core.PVector;

/**
 * This class will handle quantization and localization for a Graph, according to 
 * a scheme defined by a file.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class Translator {

	/** A dictionary that maps vertices to vectors */
	private HashMap<Vertex, PVector> vertexMapping = new HashMap<Vertex, PVector>();
	/** A dictionary that maps vectors to vertices */
	private HashMap<PVector, Vertex> vectorMapping = new HashMap<PVector, Vertex>();
	
	/**
	 * This constructs a Translator object with the given graph and mapping file.
	 * @param g The graph this translator will translate.
	 * @param scheme The file containing the mapping scheme for the given graph.
	 */
	public Translator(Graph g, File scheme){
		try {
			readFile(g, scheme);
		} catch (FileNotFoundException e) {
			Settings.fail("Unable to read graph-mapping file!");
			e.printStackTrace();
		}
	}
	
	/**
	 * This will quantize the given vector to the nearest vertex.
	 * @param vector The vector to quantize.
	 * @return The vertex with the closest mapping.
	 */
	public Vertex quantize(PVector vector){
		float minDist = Float.MAX_VALUE;
		PVector minKey = null;
		for(PVector p : vectorMapping.keySet()){
			float dist = PVector.dist(vector, p);
			if(dist < minDist){
				minDist = dist;
				minKey = p;
			}
		}
		return vectorMapping.get(minKey);
	}
	
	/**
	 * This method will localize the given vertex to a point in the game world.
	 * @param vertex The vertex to localize.
	 * @return The vector that the vertex maps to.
	 */
	public PVector localize(Vertex vertex){
		return vertexMapping.get(vertex);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String build = "";
		for(Vertex v : vertexMapping.keySet())
			build += v + " : " + vertexMapping.get(v) + "\n";
		
		return build;
	}
	
	
	// Helper Methods --------------------------------------------------------
	
	
	// This method will read in a given mapping file and create a Translator from it
	private void readFile(Graph g, File f) throws FileNotFoundException{
		// Clear the current mapping
		vertexMapping.clear();
		vectorMapping.clear();
		// Error messages
		String invalidFormat = "Invalid graph-mapping file format!";
		String invalidID = "Invalid vertex ID!";
		Scanner s = new Scanner(f);
		// Read the file line by line
		while(s.hasNextLine()){
			Scanner line = new Scanner(s.nextLine());
			
			// Try to read in the vertex ID
			if(!line.hasNextInt()){
				stopReading(s, line, invalidFormat);
				return;
			}
			int id = line.nextInt();
			
			// Check for the ID in the graph
			if(!g.hasVertex(new Vertex(id))){
				stopReading(s, line, invalidID);
				return;
			}
			
			// Try to read in x coordinate
			if(!line.hasNextFloat()){
				stopReading(s, line, invalidFormat);
				return;
			}
			float x = line.nextFloat();
			
			// Try to read in y coordinate
			if(!line.hasNextFloat()){
				stopReading(s, line, invalidFormat);
				return;
			}
			float y = line.nextFloat();
			
			// Map the vertex and vector to each other
			Vertex v = g.getVertex(id);
			PVector p = new PVector(x, y);
			vertexMapping.put(v, p);
			vectorMapping.put(p, v);
			line.close();
		}
		s.close();
	}
	
	// This method helps to stop reading a file quickly
	private void stopReading(Scanner close0, Scanner close1, String reason){
		close0.close();
		close1.close();
		Settings.fail(reason);
		vertexMapping.clear();
		vectorMapping.clear();
	}
}
