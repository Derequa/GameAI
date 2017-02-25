package manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.io.PrintStream;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import graphs.Graph;
import graphs.Heuristic.H_MODE;
import graphs.Translator;
import thinking.paths.Path;
import thinking.paths.PathFinding;

/**
 * This class presents to user with a console like screen to view
 * the algorithm comparisons of Dijkstra and A*.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
@SuppressWarnings("serial")
public class AlgCompare extends JFrame implements Runnable{
	
	// The text are for the GUI
	JTextArea consoleArea = new JTextArea();
	// A PrintStream tied to the text are for sending messages
	PrintStream console = new PrintStream(new JTextOutputStream(consoleArea));

	/**
	 * This constructs an AlgCompare instance and sets up the GUI.
	 */
	public AlgCompare(){
		setup();
	}

	private void setup() {
		consoleArea.setEditable(false);
		JScrollPane consolePane = new JScrollPane(consoleArea);
		consolePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		consolePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		consolePane.setPreferredSize(new Dimension(400, 500));
		consolePane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Output"));
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(consolePane, BorderLayout.NORTH);
		setVisible(true);
		setTitle("Algorithm Comparison");
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	// This method runs comparisons between A* and Dijkstra
	private void runTests(){
		Runtime rt = Runtime.getRuntime();
		String graph0 = "graphfiles/biggraph0.graph";
		String graph1 = "graphfiles/biggraph1.graph";
		String map0 = "graphfiles/biggraph0.map";
		String map1 = "graphfiles/biggraph1.map";
		console.println("Importing first large graph at: " + graph0);
		console.flush();
		Graph g0 = new Graph(new File(graph0), true);
		int graph0_verts = g0.getNumberOfVertices();
		int graph0_edges = g0.getNumberOfEdges();
		console.println("Done importing!");
		long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 /1024;
		console.println("Total Memory Used: " + usedMB);
		console.println("First Graph: " + graph0);
		console.println("Number of Vertices: " + graph0_verts);
		console.println("Number of Edges: " + graph0_edges);
		console.println("Importing translator for graph: " + graph0 + "\nLooking for: " + map0);
		Translator t0 = new Translator(g0, new File(map0));
		console.println("Done importing!");
		
		console.println("Importing second large graph at: " + graph1);
		console.flush();
		Graph g1 = new Graph(new File(graph1), true);
		int graph1_verts = g1.getNumberOfVertices();
		int graph1_edges = g1.getNumberOfEdges();
		console.println("Done importing!");
		console.println("Second Graph: " + graph1);
		console.println("Number of Vertices: " + graph1_verts);
		console.println("Number of Edges: " + graph1_edges);
		console.println("Importing translator for graph: " + graph1 + "\nLooking for: " + map1);
		Translator t1 = new Translator(g1, new File(map1));
		console.println("Done importing!");
		
		usedMB = ((rt.totalMemory() - rt.freeMemory()) / 1024 /1024);
		console.println("Total Memory Used: " + usedMB);
		
		PathFinding pFinder0 = new PathFinding(t0);
		PathFinding pFinder1 = new PathFinding(t1);
		
		int g0_goal = 3;
		int g1_goal = 3;
		
		console.println("Running Dijkstra's on first graph...");
		long timer = System.nanoTime();
		Path p_g0_d = pFinder0.dijkstras(g0.getVertex(0), g0.getVertex(g0_goal));
		timer = System.nanoTime() - timer;
		console.println("Done!\n"
					  + "Number of Vertices in Path: " + p_g0_d.size() + "\n"
					  + "Number of Vertices Visited: " + p_g0_d.verticesVistedOnCreation + "\n"
					  + "Max Memory Usage: " + p_g0_d.megsUsed + "\n"
					  + "Took: " + timer / 1000000 + "ms");
		
		console.println("Running Dijkstra's on second graph...");
		timer = System.nanoTime();
		Path p_g1_d = pFinder1.dijkstras(g1.getVertex(0), g1.getVertex(g1_goal));
		timer = System.nanoTime() - timer;
		console.println("Done!\n"
					  + "Number of Vertices in Path: " + p_g1_d.size() + "\n"
					  + "Number of Vertices Visited: " + p_g1_d.verticesVistedOnCreation + "\n"
					  + "Max Memory Usage: " + p_g1_d.megsUsed + "\n"
					  + "Took: " + timer / 1000000 + "ms");
		
		console.println("Running A* on first graph with Euclidean heuristic...");
		timer = System.nanoTime();
		Path p_g0_a_e = pFinder0.aStar(g0.getVertex(0), g0.getVertex(g0_goal), H_MODE.EUCLIDEAN);
		timer = System.nanoTime() - timer;
		console.println("Done!\n"
				  	  + "Number of Vertices in Path: " + p_g0_a_e.size() + "\n"
				  	  + "Number of Vertices Visited: " + p_g0_a_e.verticesVistedOnCreation + "\n"
				  	  + "Max Memory Usage: " + p_g0_a_e.megsUsed + "\n"
				  	  + "Took: " + timer / 1000000 + "ms");
		
		console.println("Running A* on first graph with Manhattan heuristic...");
		timer = System.nanoTime();
		Path p_g0_a_m = pFinder0.aStar(g0.getVertex(0), g0.getVertex(g0_goal), H_MODE.MANHATTAN);
		timer = System.nanoTime() - timer;
		console.println("Done!\n"
				  	  + "Number of Vertices in Path: " + p_g0_a_m.size() + "\n"
				  	  + "Number of Vertices Visited: " + p_g0_a_m.verticesVistedOnCreation + "\n"
				  	  + "Max Memory Usage: " + p_g0_a_m.megsUsed + "\n"
				  	  + "Took: " + timer / 1000000 + "ms");
		
		console.println("Running A* on second graph with Euclidean heuristic...");
		timer = System.nanoTime();
		Path p_g1_a_e = pFinder1.aStar(g1.getVertex(0), g1.getVertex(g1_goal), H_MODE.EUCLIDEAN);
		timer = System.nanoTime() - timer;
		console.println("Done!\n"
				  	  + "Number of Vertices in Path: " + p_g1_a_e.size() + "\n"
				  	  + "Number of Vertices Visited: " + p_g1_a_e.verticesVistedOnCreation + "\n"
				  	  + "Max Memory Usage: " + p_g1_a_e.megsUsed + "\n"
				  	  + "Took: " + timer / 1000000 + "ms");
		
		console.println("Running A* on second graph with Manhattan heuristic...");
		timer = System.nanoTime();
		Path p_g1_a_m = pFinder1.aStar(g1.getVertex(0), g1.getVertex(g1_goal), H_MODE.MANHATTAN);
		timer = System.nanoTime() - timer;
		console.println("Done!\n"
				  	  + "Number of Vertices in Path: " + p_g1_a_m.size() + "\n"
				  	  + "Number of Vertices Visited: " + p_g1_a_m.verticesVistedOnCreation + "\n"
				  	  + "Max Memory Usage: " + p_g1_a_m.megsUsed + "\n"
				  	  + "Took: " + timer / 1000000 + "ms");
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		runTests();
	}
}
