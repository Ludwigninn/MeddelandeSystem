package p3;

import java.io.Serializable;

import javax.swing.ImageIcon;


/**
 * (Message)
 * Serialiserad meddelandeklass som skickas mellan klient och server.
 * 
 * 
 * @author Alexander
 *
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 7777522899577544632L;
	
	private MessageType type;
	private String message;
	private int senderID;
	private int[] receiverID;
	private String[] onlineClients;
	private int[] onlineIDs;
	private ImageIcon image;

	enum MessageType {
		Chat, Command, Private, Group, Server, Online
	}
	/**
	 * Constructor with only a message
	 * @param type 
	 * @param message
	 */
	public Message(MessageType type, String message) {
		this(type, message, 0, null);
	}
	
	/**
	 * Constructor with a message and an image
	 * @param type
	 * @param message
	 * @param image
	 */
	public Message(MessageType type, String message, ImageIcon image) {
		this(type, message, 0, image);
	}
	
	/**
	 * Constructor with a message and a senderID
	 * @param type
	 * @param message
	 * @param senderID
	 */
	public Message(MessageType type, String message, int senderID){
		this(type, message, senderID, null);
	}
	
	/**
	 * Constructor with all the parameters needed, all other constructors
	 * calls this
	 * @param type
	 * @param message
	 * @param senderID
	 * @param image
	 */
	public Message(MessageType type, String message, int senderID, ImageIcon image) {
		this.type = type;
		this.message = message;
		this.senderID = senderID;
		this.setImage(image);
	}
	
	public MessageType getType() {
		return type;
	}
	
	public void setType(MessageType type) {
		this.type = type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	
	public int[] getReceiverIDs() {
		return receiverID;
	}

	public void setReceiverIDs(int[] receiverID) {
		this.receiverID = receiverID;
	}

	public String[] getOnlineClients() {
		return onlineClients;
	}

	public void setOnlineClients(String[] onlineClients) {
		this.onlineClients = onlineClients;
	}

	public int[] getOnlineIDs() {
		return onlineIDs;
	}

	public void setOnlineIDs(int[] onlineIDs) {
		this.onlineIDs = onlineIDs;
	}
	public ImageIcon getImage() {
		return image;
	}
	public void setImage(ImageIcon image) {
		this.image = image;
	}
}