package p3.message;

import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 * (Message)
 * Serialiserad meddelandeklass som skickas mellan klient och server.
 * 
 * @author Alexander
 * @author Björn
 * @author David
 * @author Robert
 * @author Rasmus
 * @author Ludwig
 *
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 7777522899577544632L;
	
	private String message;
	private ImageIcon image;
	
	/**
	 * Constructor with all the parameters needed, all other constructors
	 * calls this
	 * @param message
	 */
	public Message(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}
}