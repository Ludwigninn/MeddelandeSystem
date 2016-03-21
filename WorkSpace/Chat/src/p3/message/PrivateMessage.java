package p3.message;

import java.util.ArrayList;

public class PrivateMessage extends Message {
	private static final long serialVersionUID = -5554517515054911223L;
	private ArrayList<String> bReceivers;
	
	public PrivateMessage(String message) {
		super(message);
		bReceivers = new ArrayList<String>();
	}
	
	public ArrayList<String> getReceivers() {
		return bReceivers;
	}

	public void addReceiver(String receiver) {
		this.bReceivers.add(receiver);
	}
}
