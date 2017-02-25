package manager;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import graphs.Heuristic.H_MODE;
import manager.Sketch.SKETCHMODE;
import processing.core.PApplet;
import thinking.trees.BehaviorLog;

/**
 * This class sets up a GUI to run different demos of the Sketch class.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
@SuppressWarnings("serial")
public class Runner extends JFrame implements ActionListener{
	
	/** The height of the window */
	private static final int HEIGHT = 200;
	/** The width of the window */
	private static final int WIDTH = 200;
	static final String card_hw1 = "Homework 1";
	static final String card_hw2 = "Homework 2";
	static final String card_hw3 = "Homework 3";
	
	static final String[] cards = {card_hw1, card_hw2, card_hw3};
	JComboBox<String> combo_homeworks = new JComboBox<String>(cards);
	/** A button for the kinematic motion demo */
	JButton b_Kinematic = new JButton("Basic-Motion");
	/** A button for the Steering Seek demo */
	JButton b_SteeringSeek = new JButton("Seek-Steering");
	/** A button for the Steering Arrive demo */
	JButton b_SteeringArrive = new JButton("Arrive-Steering");
	/** A button for the Steering Wander demo */
	JButton b_SteeringWander = new JButton("Wander-Steering");
	/** A button for the Flocking demo */
	JButton b_Flocking = new JButton("Flocking-Behavior");
	/** A button for Path-Finding mode */
	JButton b_Paths = new JButton("Path Following (A*)");
	/** A button for the algorithm-comparison mode */
	JButton b_compare = new JButton("Compare Algorithms");
	/** A button to run the Decision tree demo */
	JButton b_decisionTree = new JButton("Decision Tree");
	/** A button to run the Behavior tree demo */
	JButton b_behaviorTree = new JButton("Behavior Tree");
	/** A button to run the Learned-Decision tree demo */
	JButton b_learnedDecisionTree = new JButton("Learned Decision Tree");
	/** A button for choosing a the Manhattan heuristic */
	JRadioButton b_heuristic_man = new JRadioButton("Manhattan Distance");
	/** A button for choosing a the Euclidean heuristic */
	JRadioButton b_heuristic_ecd = new JRadioButton("Euclidean Distance");
	/** Card panel to swap between homework deliverables */
	JPanel pnl_hwoptions = new JPanel(new CardLayout());
	/** The panel for homework 1 deliverables */
	JPanel pnl_hw1 = new JPanel(new GridLayout(0, 1));
	/** The panel for homework 2 deliverables */
	JPanel pnl_hw2 = new JPanel(new GridLayout(0, 1));
	/** The panel for homework 3 deliverables */
	JPanel pnl_hw3 = new JPanel(new GridLayout(0, 1));
	/** The mode for Sketch to read when it runs */
	public static SKETCHMODE lastMode = SKETCHMODE.STEERINGSEEK;
	/** The type of heuristic to use for A* */
	public static H_MODE heuristic = H_MODE.EUCLIDEAN;
	
	
	/**
	 * This constructs a Runner and sets up the GUI and buttons.
	 */
	public Runner(){
		// Set size
		setTitle("CSC 484 : Derek Batts");
		setSize(WIDTH, HEIGHT);
		// Get pane and set layout
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(combo_homeworks, BorderLayout.NORTH);
		contentPane.add(pnl_hwoptions, BorderLayout.CENTER);
		pnl_hwoptions.add(pnl_hw1, card_hw1);
		pnl_hwoptions.add(pnl_hw2, card_hw2);
		pnl_hwoptions.add(pnl_hw3, card_hw3);
		// Add buttons
		pnl_hw1.add(b_Kinematic);
		pnl_hw1.add(b_SteeringSeek);
		pnl_hw1.add(b_SteeringArrive);
		pnl_hw1.add(b_SteeringWander);
		pnl_hw1.add(b_Flocking);
		
		pnl_hw2.add(b_Paths);
		JPanel heuristic = new JPanel();
		heuristic.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Heuristic"));
		heuristic.add(b_heuristic_man);
		heuristic.add(b_heuristic_ecd);
		pnl_hw2.add(heuristic);
		pnl_hw2.add(b_compare);
		
		pnl_hw3.add(b_decisionTree);
		pnl_hw3.add(b_behaviorTree);
		pnl_hw3.add(b_learnedDecisionTree);
		
		// Register buttons
		combo_homeworks.addActionListener(this);
		b_Kinematic.addActionListener(this);
		b_SteeringSeek.addActionListener(this);
		b_SteeringArrive.addActionListener(this);
		b_SteeringWander.addActionListener(this);
		b_Flocking.addActionListener(this);
		b_Paths.addActionListener(this);
		b_compare.addActionListener(this);
		b_heuristic_man.addActionListener(this);
		b_heuristic_ecd.addActionListener(this);
		combo_homeworks.setSelectedItem(card_hw3);
		b_heuristic_ecd.setSelected(true);
		b_decisionTree.addActionListener(this);
		b_behaviorTree.addActionListener(this);
		b_learnedDecisionTree.addActionListener(this);
		// Finish up
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	
	/**
	 * This method starts up the GUI.
	 * @param args Command-line arguments
	 */
	public static void main(String[] args){
		Runner r = new Runner();
		 r.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		 r.addWindowListener(new WindowAdapter() {
		        @Override
		        public void windowClosing(WindowEvent event) {
		            BehaviorLog.writeFile();
		            r.dispose();
		            System.exit(0);
		        }
	    });
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// For each button set the mode, and start the Sketch class
		if(e.getSource().equals(b_Kinematic)){
			lastMode = SKETCHMODE.KINEMATIC;
			PApplet.main("manager.Sketch");
		}
		else if(e.getSource().equals(b_SteeringSeek)){
			lastMode = SKETCHMODE.STEERINGSEEK;
			PApplet.main("manager.Sketch");
		}
		else if(e.getSource().equals(b_SteeringArrive)){
			lastMode = SKETCHMODE.STEERINGARRIVE;
			PApplet.main("manager.Sketch");
		}
		else if(e.getSource().equals(b_SteeringWander)){
			lastMode = SKETCHMODE.STEERINGWANDER;
			PApplet.main("manager.Sketch");
		}
		else if(e.getSource().equals(b_Flocking)){
			lastMode = SKETCHMODE.FLOCKING;
			PApplet.main("manager.Sketch");
		}
		else if(e.getSource().equals(b_Paths)){
			lastMode = SKETCHMODE.PATHFOLLOWING;
			PApplet.main("manager.Sketch");
		}
		else if(e.getSource().equals(combo_homeworks)){
			((CardLayout) pnl_hwoptions.getLayout()).show(pnl_hwoptions, (String) combo_homeworks.getSelectedItem());;
		}
		else if(e.getSource().equals(b_heuristic_man)){
			b_heuristic_ecd.setSelected(false);
			heuristic = H_MODE.MANHATTAN;
		}
		else if(e.getSource().equals(b_heuristic_ecd)){
			b_heuristic_man.setSelected(false);
			heuristic = H_MODE.EUCLIDEAN;
		}
		else if(e.getSource().equals(b_compare)){
			AlgCompare a = new AlgCompare();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Thread t = new Thread(a);
			t.start();
		}
		else if(e.getSource().equals(b_decisionTree)){
			lastMode = SKETCHMODE.DECISIONTREE;
			PApplet.main("manager.Sketch");
		}
		else if(e.getSource().equals(b_behaviorTree)){
			lastMode = SKETCHMODE.BEHAVIORTREE;
			PApplet.main("manager.Sketch");
		}
		else if(e.getSource().equals(b_learnedDecisionTree)){
			lastMode = SKETCHMODE.L_DECISIONTREE;
			PApplet.main("manager.Sketch");
		}
	}
}
