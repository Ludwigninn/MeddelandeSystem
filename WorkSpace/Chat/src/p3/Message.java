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
	
	enum MessageType {
		Chat, Command, Private, Group, Server
	}
	
	public Message(MessageType type, String message) {
		this.type = type;
		this.message = message;
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
}