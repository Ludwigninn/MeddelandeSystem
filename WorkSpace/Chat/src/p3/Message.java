package p3;

import java.io.Serializable;

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

	enum MessageType {
		Chat, Command, Private, Group, Server
	}
	
	public Message(MessageType type, String message) {
		this(type, message, 0);
	}
	
	public Message(MessageType type, String message, int senderID) {
		this.type = type;
		this.message = message;
		this.senderID = senderID;
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
}