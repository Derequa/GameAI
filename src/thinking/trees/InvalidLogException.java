package thinking.trees;

/**
 * This class provides an easy way to flag a bad log
 * file for decision tree learning.
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class InvalidLogException extends IllegalArgumentException {

	/** This thing makes eclipse happy */
	private static final long serialVersionUID = -8682537404022405615L;

	/**
	 * Construct a generic exception.
	 */
	public InvalidLogException() {
		super();
	}
	
	/**
	 * Create an exception with the given message.
	 * @param message The message to report when the exception is thrown.
	 */
	public InvalidLogException(String message) {
		super(message);
	}

}
