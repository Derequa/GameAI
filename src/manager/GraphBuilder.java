package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import graphs.Edge;
import graphs.Graph;
import graphs.UndirectedEdge;
import graphs.Vertex;
import model.GameObject;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * This class is used to create a graph file and mapping file, using 
 * mouse clicks. This makes it easier to generate a graph for a given background.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class GraphBuilder extends Sketch {
	
	/** This is a list of all the objects we need to draw */
	private LinkedList<GameObject> objects = new LinkedList<GameObject>();
	/** An image to use for the background */
	private PImage bg;
	/** The line currently being dragged */
	private GraphingLine currentDrag = null;
	/** A way to ID vertices */
	private int guidMaker = 0;
	
	/*
	 * (non-Javadoc)
	 * @see manager.Sketch#settings()
	 */
	@Override
	public void settings(){
		size(800, 600);
		bg = loadImage("backgrounds/room.jpg");
	}
	
	/*
	 * (non-Javadoc)
	 * @see manager.Sketch#draw()
	 */
	@Override
	public void draw(){
		image(bg, 0, 0);
		for(GameObject g : objects)
			g.draw();
		if(currentDrag != null)
			currentDrag.draw();
	}
	
	/*
	 * (non-Javadoc)
	 * @see processing.core.PApplet#keyPressed(processing.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent k){
		if(k.getKey() == ESC)
			writeToFile();
	}
	
	/*
	 * (non-Javadoc)
	 * @see processing.core.PApplet#mousePressed(processing.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent m){
		if(m.getButton() == LEFT){
			GraphingCircle c = new GraphingCircle(this);
			c.setPos(m.getX(), m.getY());
			objects.add(c);
		}
		else if(m.getButton() == RIGHT){
			GraphingCircle closest = getClosest(m);
			PVector pos = new PVector(m.getX(), m.getY());
			currentDrag = new GraphingLine(this, closest.position, pos);
			currentDrag.v0 = closest;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see processing.core.PApplet#mouseDragged(processing.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent m){
		if((m.getButton() == RIGHT) && (currentDrag != null)){
			currentDrag.end = new PVector(m.getX(), m.getY());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see processing.core.PApplet#mouseReleased(processing.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent m){
		if(m.getButton() == RIGHT){
			if(currentDrag != null){
				GraphingCircle c = getClosest(m);
				if(currentDrag.v0.equals(c)){
					currentDrag = null;
					return;
				}
				currentDrag.end = c.position;
				currentDrag.v1 = c;
				if(!objects.contains(currentDrag))
					objects.add(currentDrag);
			}
			currentDrag = null;
		}
	}
	
	
	// Helper Methods ----------------------------------------------------------
	
	/**
	 * This finds the GraphingCircle in this sketch that is closest to the mouse event.
	 * @param m The mouse event to look around.
	 * @return The closest GraphingCircle in this sketch.
	 */
	private GraphingCircle getClosest(MouseEvent m){
		float minDist = Float.MAX_VALUE;
		GraphingCircle closest = null;
		// Loop through all the Graphing circles
		for(GameObject g : objects){
			// Forget non-GraphingCircles
			if(!(g instanceof GraphingCircle))
				continue;
			// Get distance and check it against the current min
			float dist = g.position.dist(new PVector(m.getX(), m.getY()));
			if(dist < minDist){
				closest = (GraphingCircle) g;
				minDist = dist;
			}
		}
		return closest;
	}
	
	/**
	 * This method writes a graph and map file for all the circle and lines in the sketch.
	 */
	private void writeToFile(){
		// Make and empty graph
		Graph graph = new Graph(true);
		HashMap<Vertex, PVector> mapping = new HashMap<Vertex, PVector>();
		// Add all the circles in as vertices, using their GUIDs for IDs
		for(GameObject g : objects){
			if(g instanceof GraphingCircle){
				Vertex v = new Vertex(g.guid);
				// Record the PVector for this vertex
				mapping.put(v, g.position);
				graph.addVertex(v);
			}
		}
		// Add all the lines in as edges
		for(GameObject g : objects){
			if(g instanceof GraphingLine){
				GraphingLine l = (GraphingLine) g;
				if(!(l.v0 == null) && !(l.v1 == null))
					graph.addEdge(new UndirectedEdge(graph.getVertex(l.v0.guid), graph.getVertex(l.v1.guid), l.length(), true));
			}
		}
		
		// Setup for writing to file
		File graphFile = new File("graphfiles/custom.graph");
		File mapFile = new File("graphfiles/custom.map");
		PrintWriter graphWriter = null;
		PrintWriter mapWriter = null;
		
		// Try to open print-writers
		try{
			graphWriter = new PrintWriter(graphFile);
			mapWriter = new PrintWriter(mapFile);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		
		// Write each edge of the graph to the graph file
		for(Edge e : graph)
			graphWriter.println(e);
		// Write each vertex and its mapping to the map file
		for(Iterator<Vertex> i = graph.vertexIterator(); i.hasNext();){
			Vertex v = i.next();
			PVector p = mapping.get(v);
			mapWriter.println(v + " " + p.x + " " + p.y);
		}
		
		// Flush buffers
		graphWriter.flush();
		mapWriter.flush();
		graphWriter.close();
		mapWriter.close();
	}
	
	
	// Inner Classes ---------------------------------------------------
	

	/**
	 * This is a wrapper class for drawing a circle.
	 * @author Derek Batts - dsbatts@ncsu.edu
	 *
	 */
	public class GraphingCircle extends GameObject {
		
		/**
		 * This constructs a GraphingCircle, tied to the given sketch.
		 * @param parent The sketch to draw to.
		 */
		public GraphingCircle(Sketch parent) {
			super(parent, guidMaker++);
		}

		/**
		 * This draws the GraphingCircle on the parent sketch.
		 */
		@Override
		public void draw() {
			parent.pushMatrix();
			parent.translate(position.x, position.y);
			parent.fill(255);
			parent.ellipse(0, 0, 20, 20);
			parent.fill(0);
			parent.text("" + guid, 0, 0);
			parent.popMatrix();
		}
	}
	
	/**
	 * This is a wrapper class for drawing a line.
	 * @author Derek Batts - dsbatts@ncsu.edu
	 *
	 */
	public class GraphingLine extends GameObject {
		
		/** The starting point for this line */
		PVector start;
		/** The end point for this line */
		PVector end;
		GraphingCircle v0 = null;
		GraphingCircle v1 = null;

		/**
		 * This constructs a GraphingLine tied to the given sketch.
		 * @param parent The sketch to draw in.
		 * @param start The starting point of the line as a vector.
		 * @param end The end point of the line as a vector.
		 */
		public GraphingLine(Sketch parent, PVector start, PVector end) {
			super(parent, 0);
			this.parent = parent;
			this.start = start;
			this.end = end;
		}
		
		/**
		 * This determines how long the line is.
		 * @return The length of the line as a float.
		 */
		public float length(){
			return start.dist(end);
		}

		/**
		 * This draws the line to the parent sketch.
		 */
		@Override
		public void draw() {
			parent.stroke(255, 0, 255);
			parent.line(start.x, start.y, end.x, end.y);
		}
		
		/*
		 * (non-Javadoc)
		 * @see model.GameObject#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object o){
			if((o == null) || !(o instanceof GraphingLine))
				return false;
			GraphingLine l = (GraphingLine) o;
			return start.equals(l.start) && end.equals(l.end);
		}
	}
}
