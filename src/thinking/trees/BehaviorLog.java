package thinking.trees;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class implements a way to log behavior tasks picked by the monster's behavior tree.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class BehaviorLog {
	
	/**
	 * This provides a way to easily compare tasks executed by the monsters
	 * behavior tree.
	 * @author Derek Batts - dsbatts@ncsu.edu
	 *
	 */
	public enum ACTION{
		SEEK_PLAYER,
		WANDER,
		SEEK_FRIDGE,
		EAT_PLAYER
	}
	
	/** A flag for whether or not to log data */
	private static boolean logdata = false;
	/** A buffer to store logged data before writing to file */
	private static StringBuffer writer = new StringBuffer();
	/** A universal log file name scheme */
	public static final String filename = "logs/behavior-log-";
	
	/**
	 * This method will write the contents of the buffer to the first available log
	 * filename if the logging flag is true.
	 */
	public static void writeFile(){
		if(!logdata)
			return;
		// Look for the first available log filename available
		loop: for(int i = 0 ; i < Integer.MAX_VALUE ; i++){
			File f = new File(filename + i + ".txt");
			if(f.exists())
				continue;
			OutputStream out = null;
			try {
				// Write buffer
				out = new FileOutputStream(f);
				out.write(writer.toString().getBytes());
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break loop;
		}
	}
	
	/**
	 * This logs attributes and the actions associated with them for the 
	 * monster behavior tree.
	 * @param distance The distance the Monster is from the player.
	 * @param isMonsterHungry The monster's hunger flag.
	 * @param actionTaken The type of task the monster executed.
	 */
	public static void logState(float distance, boolean isMonsterHungry, ACTION actionTaken){
		if(!logdata)
			return;
		String s = "" + distance + " " + isMonsterHungry + " " + actionTaken + "\n";
		writer.append(s);
	}
	
	/**
	 * This set the log state to the given value.
	 * @param b The value to set the log state to.
	 */
	public static void setLogState(boolean b){
		logdata = b;
	}
	
	/**
	 * This will report whether or not we are logging data.
	 * @return True if we are currently logging, false if otherwise.
	 */
	public static boolean isLogging(){
		return logdata;
	}
}
