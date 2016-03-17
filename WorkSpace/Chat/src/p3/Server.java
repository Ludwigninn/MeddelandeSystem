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

import javax.swing.ImageIcon;

import p3.Message.MessageType;

/**
 * (Server) Main klass f�r servern,hanterar hur servern fungerar.
 * 
 * @author Alexander
 * @author Björn
 * @author David
 * @author Robert
 * @author Rasmus
 * @author Ludwig
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

	/**
	 * Constructor for the server 
	 * @param port Which port is used
	 * @param gui The associated gui
	 * @throws SecurityException
	 * @throws IOException
	 */
	public Server(int port, ServerGUI gui) throws SecurityException, IOException {
		this.port = port;
		this.serverGUI = gui;

		this.sDate = new SimpleDateFormat("HH:mm:ss");
		this.aListClients = new ArrayList<ClientHandler>();
		this.aOnlineClients = new ArrayList<String>();
		logFormatter = new LogFormatter();
	}

	/**
	 * The startup for the server
	 */
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
				logFormatter.logError(e);
			}
		} catch (IOException e) {
			logFormatter.logError(e);
			System.err.println(e);
		}
	}

	/**
	 * Method to stop the server from running
	 */
	public void stop() {
		keepLooping = false;
	}

	/**
	 * The standard way to broadcast messages
	 * @param type The type of message to be broadcasted
	 * @param message the text of the message
	 * @param image The added image, if any
	 */
	public void broadcast(MessageType type, String message, ImageIcon image) {
		if(image != null){
			for (int i = 0; i < aListClients.size(); ++i) {
				ClientHandler clientThread = aListClients.get(i);
				clientThread.writeMessage(new Message(type, message, image));
			}
		}else{
			for (int i = 0; i < aListClients.size(); ++i) {
				ClientHandler clientThread = aListClients.get(i);
				clientThread.writeMessage(new Message(type, message));
			}
		}
	}
	/**
	 * Method to send message to multiple receiver 
	 * @param type The type of message
	 * @param receivers the receivers for the message
	 * @param sender the user that sent the message
	 * @param message the text of the message
	 * @param image the added image, if any
	 */
	public void broadcastToReceivers(MessageType type, int[] receivers, int sender, String message, ImageIcon image) {
		if(image != null){
			for (int i = 0; i < aListClients.size(); ++i) {
				ClientHandler clientThread = aListClients.get(i);
				for(int j = 0; j < receivers.length; j++) {
					if(Arrays.asList(receivers).contains(idHolder[i]) || idHolder[i] == sender) {
						clientThread.writeMessage(new Message(type, message, image));
					}
				}
			}
		}else{
			for (int i = 0; i < aListClients.size(); ++i) {
				ClientHandler clientThread = aListClients.get(i);
				for(int j = 0; j < receivers.length; j++) {
					if(Arrays.asList(receivers).contains(idHolder[i]) || idHolder[i] == sender) {
						clientThread.writeMessage(new Message(type, message));
					}
				}
			}
		}
	}

	/**
	 * Method to send a message to a specific receiver
	 * @param type The type of message
	 * @param receiver the receivers for the message
	 * @param sender the user that sent the message
	 * @param message the text of the message
	 * @param image the added image, if any
	 */
	public void broadcastToReceiver(MessageType type, int receiver, int sender, String message, ImageIcon image) {
		if(image!=null){
			for (int i = 0; i < aListClients.size(); ++i) {
				ClientHandler clientThread = aListClients.get(i);
				if(idHolder[i] == receiver || idHolder[i] == sender) {
					clientThread.writeMessage(new Message(type, message, image));
				}
			}
		}else{
			for (int i = 0; i < aListClients.size(); ++i) {
				ClientHandler clientThread = aListClients.get(i);
				if(idHolder[i] == receiver || idHolder[i] == sender) {
					clientThread.writeMessage(new Message(type, message));
				}
			}
		}
	}

	/**
	 * Method to update the list of online users
	 */
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

	/**
	 * Method to remove a user from the server
	 * @param id the user
	 */
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

	/**
	 * A threaded class to handle every client connected to the server
	 * @author bjorsven
	 *
	 */
	private class ClientHandler extends Thread {
		private Socket socket;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private int id;
		private String username = null; 
		private Message message;

		public int getID() {
			return this.id;
		}

		/**
		 * Constructor for each clienthandler
		 * @param socket the connecting socket 
		 * @throws IOException
		 * @throws ClassNotFoundException
		 */
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
				broadcast(MessageType.Server, sDate.format(new Date()) + " " + username + " connected", null);
				logFormatter.logServerMessage(username + "has connected");
			}
		}

		/**
		 * The running method for the thread.
		 * it checks for new messages to be sent
		 */
		public void run() {
			while (true) {	
				updateOnlineList();

				try {
					message = (Message) ois.readObject();
				} catch (Exception e) {
					serverGUI.appendEvent(sDate.format(new Date()) + " " + e.getMessage());
					logFormatter.logError(e);
					e.printStackTrace();
					break;
				}

				String receivedMessage = message.getMessage();
				//Sorting the messages
				switch (message.getType()) {
				case Chat: {
					if(message.getImage()!=null){
						broadcast(message.getType(), sDate.format(new Date()) + " " + username + ": " + receivedMessage,message.getImage());
						logFormatter.logMessage(message);
					}
					else{
						broadcast(message.getType(), sDate.format(new Date()) + " " + username + ": " + receivedMessage, null);
						serverGUI.appendChat(sDate.format(new Date()) + " " + username + " (ID: " + id + "): " + receivedMessage);

					}
					logFormatter.logMessage(message);
					break;
				}
				case Private: {
					int[] temp = message.getReceiverIDs();
					if(message.getImage()!=null){
						broadcastToReceiver(message.getType(), temp[0], message.getSenderID(), sDate.format(new Date()) + " " + username + ": " + receivedMessage, message.getImage());
					}
					else{
						broadcastToReceiver(message.getType(), temp[0], message.getSenderID(), sDate.format(new Date()) + " " + username + ": " + receivedMessage, null);
						serverGUI.appendChat(sDate.format(new Date()) + " " + username + " (ID: " + id + "): " + receivedMessage);
					}
					logFormatter.logMessage(message);
					break;
				}
				case Group: {
					if(message.getImage()!=null){
						broadcastToReceivers(message.getType(), message.getReceiverIDs(), message.getSenderID(), sDate.format(new Date()) + " " + username + ": " + receivedMessage, message.getImage());
					}else{
						broadcastToReceivers(message.getType(), message.getReceiverIDs(), message.getSenderID(), sDate.format(new Date()) + " " + username + ": " + receivedMessage, null);
						serverGUI.appendChat(sDate.format(new Date()) + " " + username + " (ID: " + id + "): " + receivedMessage);
					}
					logFormatter.logMessage(message);
					break;
				}
				case Server: {
					remove(message.getSenderID());
					logFormatter.logMessage(message);
					break;
				}
				default: {
					break;
				}

				}
			}
		}

		/**
		 * Method to close Server
		 * @throws IOException
		 */
		public void close() throws IOException {
			if (socket != null) {
				logFormatter.logServerMessage("Closing Server");
				socket.close();
				ois.close();
				oos.close();
			}
		}

		/**
		 * Method that writes the messages to an ObjectOutputStream
		 * @param message
		 */
		public void writeMessage(Message message) {
			try {
				oos.writeObject(message);
				oos.flush();
			} catch(Exception e) { 
				serverGUI.appendEvent(sDate.format(new Date()) + " Message failed to broadcast");
				logFormatter.logError(e);
			}
		}
	}
}