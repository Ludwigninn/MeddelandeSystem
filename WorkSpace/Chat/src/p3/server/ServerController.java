package p3.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import p3.message.GroupMessage;
import p3.message.Message;
import p3.message.PrivateMessage;
import p3.server.Server.ClientHandler;

public class ServerController {
	private SimpleDateFormat sDate;
	private LogFormatter logFormatter;
	
	private Server server;
	private ServerGUI serverGUI;
	
	public ServerController(Server server, ServerGUI serverGUI) throws SecurityException, IOException {
		this.sDate = new SimpleDateFormat("HH:mm:ss");
		this.logFormatter = new LogFormatter();
		
		this.server = server;
		this.serverGUI = serverGUI;
	}
	
	/**
	 * The standard way to broadcast messages
	 * @param message the text of the message
	 * @param image The added image, if any
	 */
	public void broadcast(Message message) {
		HashMap<String, ClientHandler> clients = server.getListClients();
		for(ClientHandler client : clients.values()) {
			client.writeMessage(message);
		}
	}
	
	/**
	 * Method to send message to multiple receiver 
	 * @param receivers the receivers for the message
	 * @param sender the user that sent the message
	 * @param message the text of the message
	 * @param image the added image, if any
	 * @throws InterruptedException 
	 */
	public void broadcastToReceivers(Message message) throws InterruptedException {
		HashMap<String, ClientHandler> clients = server.getListClients();
		ArrayList<String> receievers = new ArrayList<String>();
		if(message instanceof GroupMessage) {
			receievers = ((GroupMessage) message).getReceivers();
		} else if(message instanceof PrivateMessage) {
			receievers = ((PrivateMessage) message).getReceivers();
		}
		
		if(receievers != null) {
			for(int i = 0; i < receievers.size(); i++) {
				for(HashMap.Entry<String, ClientHandler> entry : clients.entrySet()) {
					String key = entry.getKey();
				    ClientHandler client = entry.getValue();
				    
				    if(key.equals(receievers.get(i))) {
				    	client.writeMessage(message);
				    }
				}
			}
		}
	}
	
	/**
	 * Method to update the list of online users
	 */
	public void updateOnlineList() {
		HashMap<String, ClientHandler> clients = server.getListClients();
		ArrayList<String> list = null;
		list = new ArrayList<String>();
		for(String key : clients.keySet()) {
			list.add(key);
		}
		
		for(ClientHandler client : clients.values()) {
			client.updateOnline(list);
		}
	}
	
	public String getDate() {
		return "[" + sDate.format(new Date()) + "] ";
	}
	
	public void appendChat(String text) {
        serverGUI.appendChat(text);
    }
	
    public void appendEvent(String text) {
    	serverGUI.appendEvent(text);
    }
	
	public void logMessage(Message message) {
		logFormatter.logMessage(message);
	}
	
	public void logServerMessage(String message) {
		logFormatter.logServerMessage(message);
	}
	
	public void logError(Exception e) {
		logFormatter.logError(e);
	}
}
