package p3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import p3.Message.MessageType;

/**
 * (Server) Main klass f�r servern,hanterar hur servern fungerar.
 * 
 * @author Alexander
 *
 */
public class Server {
	private static int uniqueID;

	private ArrayList<ClientHandler> aListClients;
	private ArrayList<String> aOnlineClients;
	private ServerGUI serverGUI;

	private int port;
	private boolean keepLooping = true;
	
	private SimpleDateFormat sDate;
	private LogFormatter logFormatter;
	
	private int[] idHolder;

	public Server(int port, ServerGUI gui) {
		this.port = port;
		this.serverGUI = gui;
		
		this.sDate = new SimpleDateFormat("HH:mm:ss");
		this.aListClients = new ArrayList<ClientHandler>();
		this.aOnlineClients = new ArrayList<String>();
	}

	public void start() {
		serverGUI.appendEvent(sDate.format(new Date()) + " Server started");
		try {
			ServerSocket serverSocket = new ServerSocket(port);

			while (keepLooping) {
				Socket socket = serverSocket.accept();
				ClientHandler cHandler;
				try {
					cHandler = new ClientHandler(socket);
					
					aListClients.add(cHandler);
					cHandler.start();
				} catch (ClassNotFoundException e) { }
			}

			try {
				serverSocket.close();
				for (int i = 0; i < aListClients.size(); ++i) {
					ClientHandler clientThread = aListClients.get(i);
					clientThread.close();
				}
			} catch (Exception e) {
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void stop() {
		keepLooping = false;
	}
	
	public void broadcast(MessageType type, String message) {
		for (int i = 0; i < aListClients.size(); ++i) {
			ClientHandler clientThread = aListClients.get(i);
			clientThread.writeMessage(new Message(type, message));
		}
	}
	
	public void broadcastToReceivers(MessageType type, int[] receivers, int sender, String message) {
		for (int i = 0; i < aListClients.size(); ++i) {
			ClientHandler clientThread = aListClients.get(i);
			for(int j = 0; j < receivers.length; j++) {
				if(Arrays.asList(receivers).contains(idHolder[i]) || idHolder[i] == sender) {
					clientThread.writeMessage(new Message(type, message));
				}
			}
		}
	}
	
	public void broadcastToReceiver(MessageType type, int receiver, int sender, String message) {
		for (int i = 0; i < aListClients.size(); ++i) {
			ClientHandler clientThread = aListClients.get(i);
			if(idHolder[i] == receiver || idHolder[i] == sender) {
				clientThread.writeMessage(new Message(type, message));
			}
		}
	}
	
	public void updateOnlineList() {
		String[] onlineHolder = new String[aOnlineClients.size()];
		idHolder = new int[aListClients.size()];
		Message mess = new Message(MessageType.Online, "");
		for (int i = 0; i < aListClients.size(); ++i) {
			ClientHandler clientThread = aListClients.get(i);
			idHolder[i] = (int) clientThread.getID();
						
			for(int j = 0; j < aOnlineClients.size(); j++) {
				onlineHolder[j] = aOnlineClients.get(j);
			}
			
			mess.setOnlineClients(onlineHolder);
			mess.setOnlineIDs(idHolder);
			clientThread.writeMessage(mess);
		}
	}
	
	public void remove(int id) {
		for (int i = 0; i < aListClients.size(); ++i) {
			ClientHandler clientThread = aListClients.get(i);

			if(clientThread.id == id) {
				aListClients.remove(i);
				aOnlineClients.remove(i);
				updateOnlineList();
				serverGUI.appendEvent(sDate.format(new Date()) + " User disconnected: " + id);
				return;
			}
		}
	}

	private class ClientHandler extends Thread {
		private Socket socket;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private int id;
		private String username = null; // ?
		private Message message;
		
		public int getID() {
			return this.id;
		}

		public ClientHandler(Socket socket) throws IOException, ClassNotFoundException {
			this.id = ++uniqueID;

			this.socket = socket;
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			if(username == null) {
				username = (String) ois.readObject();
				aOnlineClients.add(username);
				oos.writeObject(id);
				oos.flush();
				serverGUI.appendEvent(sDate.format(new Date()) + " " + username + " - ID: " + id + " - connected");
				broadcast(MessageType.Server, sDate.format(new Date()) + " " + username + " connected");
			}
		}

		public void run() {
			while (true) {	
				updateOnlineList();
				
				try {
					message = (Message) ois.readObject();
				} catch (Exception e) {
					serverGUI.appendEvent(sDate.format(new Date()) + " " + e.getMessage());
					e.printStackTrace();
					break;
				}

				String receivedMessage = message.getMessage();
				switch (message.getType()) {
					case Chat: {
						broadcast(message.getType(), sDate.format(new Date()) + " " + username + ": " + receivedMessage);
						serverGUI.appendChat(sDate.format(new Date()) + " " + username + " (ID: " + id + "): " + receivedMessage);
						break;
					}
					case Command: {
						
						break;
					}
					case Private: {
						int[] temp = message.getReceiverIDs();
						broadcastToReceiver(message.getType(), temp[0], message.getSenderID(), sDate.format(new Date()) + " " + username + ": " + receivedMessage);
						serverGUI.appendChat(sDate.format(new Date()) + " " + username + " (ID: " + id + "): " + receivedMessage);
						break;
					}
					case Group: {
						broadcastToReceivers(message.getType(), message.getReceiverIDs(), message.getSenderID(), sDate.format(new Date()) + " " + username + ": " + receivedMessage);
						serverGUI.appendChat(sDate.format(new Date()) + " " + username + " (ID: " + id + "): " + receivedMessage);
						break;
					}
					case Server: {
						remove(message.getSenderID());
						break;
					}
					default: {
						break;
					}
					
				}
			}
		}

		public void close() throws IOException {
			if (socket != null) {
				socket.close();
				ois.close();
				oos.close();
			}
		}
		
		public void writeMessage(Message message) {
			try {
				oos.writeObject(message);
				oos.flush();
			} catch(Exception e) { 
				serverGUI.appendEvent(sDate.format(new Date()) + " Message failed to broadcast");
			}
		}
	}
}