package manager;

import java.io.PrintStream;

public class Settings {

	public static PrintStream console = System.out;
	public static boolean debug = true;
	
	public static void fail(String message){
		console.println("ERROR: " + message);
	}
	
	public static void failAndQuit(String message){
		fail(message);
		System.exit(1);
	}
	
	public static void debugMessage(String message){
		if(debug)
			console.println("DEBUG: " + message);
	}
	
	public static void statusMessage(String message){
		console.println("STATUS: " + message);
	}
}
