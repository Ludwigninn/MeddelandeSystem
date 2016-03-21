package p3.message;

public class ServerMessage extends Message {
	private static final long serialVersionUID = -2761273264074961785L;
	private String disconnectingUser = null;

	public ServerMessage(String message) {
		super(message);
	}

	public String getDisconnectingUser() {
		return disconnectingUser;
	}

	public void setDisconnectingUser(String disconnectingUser) {
		this.disconnectingUser = disconnectingUser;
	}
}
