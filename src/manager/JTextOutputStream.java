package manager;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 * This is a cool class I found online at:
 * http://www.codejava.net/java-se/swing/redirect-standard-output-streams-to-jtextarea
 * That lets you use a JTextArea like an output stream
 * This is how we get our all the parts of our server to print to the GUI
 * @author Derek Batts - dsbatts@ncsu.edu
 *
 */
public class JTextOutputStream extends OutputStream {

	// The text area this is tied to
	private JTextArea textArea;
	
	/**
	 * This constructs a new OutputStream, tied to the given text area.
	 * @param textArea The JTextArea this output stream is tied to.
	 */
	public JTextOutputStream(JTextArea textArea){
		this.textArea = textArea;
	}
	
	public void write(int b) throws IOException {
		// Redirects data to the text area
        textArea.append(String.valueOf((char)b));
        // Scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
	}

}
