package p3.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import p3.message.GroupMessage;
import p3.message.Message;
import p3.message.PrivateMessage;
import p3.message.ServerMessage;

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
	private HashMap<String, ClientHandler> aListClients;
	
	public HashMap<String, ClientHandler> getListClients() {
		return aListClients;
	}
	
	private ServerController serverController;

	private int port;
	private boolean keepLooping = false;

	/**
	 * Constructor for the server 
	 * @param port Which port is used
	 * @param gui The associated gui
	 * @throws SecurityException
	 * @throws IOException
	 */
	public Server(int port) throws SecurityException, IOException {
		this.port = port;
		
		this.aListClients = new HashMap<String, ClientHandler>();
	}
	
	public void setServerController(ServerController controller) {
		this.serverController = controller;
	}
	
	/**
	 * The startup for the server
	 */
	public void start() {
		if(!keepLooping) {
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				
				serverController.appendEvent(serverController.getDate() + "Server started");
				serverController.logServerMessage(serverController.getDate() + "Server started");
				keepLooping = true;
				
				while (keepLooping) {
					Socket socket = serverSocket.accept();
					ClientHandler cHandler;
					try {
						cHandler = new ClientHandler(socket);
						cHandler.start();
					} catch (ClassNotFoundException | NameInUseException e) { 
						serverController.appendEvent(serverController.getDate() + e.getMessage());
						serverController.logError(e);
					}
				}
	
				try {
					serverSocket.close();
					for (ClientHandler value : aListClients.values()) {
					    ClientHandler clientThread = value;
						clientThread.close();
					}
				} catch (Exception e) {
					serverController.appendEvent(serverController.getDate() + e.getMessage());
					serverController.logError(e);
				}
			} catch (IOException e) {
				serverController.appendEvent(serverController.getDate() + e.getMessage());
				serverController.logError(e);
			}
		}
	}

	/**
	 * Method to stop the server from running
	 */
	public void stop() {
		keepLooping = false;
	}

	/**
	 * Method to remove a user from the server
	 * @param key the user
	 */
	public void remove(String key) {
		aListClients.remove(key);
		serverController.updateOnlineList();
		serverController.appendEvent(serverController.getDate() + "User disconnected: " + key);
		serverController.logServerMessage(serverController.getDate() + "User disconnected: " + key);
	}

	/**
	 * A threaded class to handle every client connected to the server
	 * @author bjorsven
	 *
	 */
	public class ClientHandler extends Thread implements Serializable {
		private static final long serialVersionUID = 5409820190756873003L;
		private transient Socket socket;
		private transient ObjectInputStream ois;
		private transient ObjectOutputStream oos;

		private String username;
		private Message message;

		/**
		 * Constructor for each clienthandler
		 * @param socket the connecting socket 
		 * @throws IOException
		 * @throws ClassNotFoundException
		 * @throws NameInUseException 
		 */
		public ClientHandler(Socket socket) throws IOException, ClassNotFoundException, NameInUseException {
			this.socket = socket;
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());

			username = (String) ois.readObject();
			if(aListClients.containsKey(username)) {
				oos.writeInt(-1);
				oos.flush();
				throw new NameInUseException("Name in use!");
			} else {
				oos.writeInt(0);
				oos.flush();
			}
			
			aListClients.put(username, this);
			
			serverController.appendEvent(serverController.getDate() + username + " connected");
			serverController.broadcast(new ServerMessage(serverController.getDate() + username + " connected"));
			serverController.logServerMessage(username + " has connected");
		}

		/**
		 * The running method for the thread.
		 * it checks for new messages to be sent
		 */
		public void run() {
			while (true) {	
				serverController.updateOnlineList();

				try {
					Object output = (Object) ois.readObject();
					message = null;
					if(output instanceof GroupMessage) {
						message = (GroupMessage) output;
					} else if(output instanceof PrivateMessage) {
						message = (PrivateMessage) output;
					} else if(output instanceof ServerMessage) {
						message = (ServerMessage) output;
					} else {
						message = (Message) output;
					}
				} catch (Exception e) {
					serverController.appendEvent(serverController.getDate() + e.getMessage());
					serverController.logError(e);
					e.printStackTrace();
					break;
				}
				
				if(message != null) {
					String receivedMessage = message.getMessage();
					if(message instanceof GroupMessage || message instanceof PrivateMessage) {
						message.setMessage(serverController.getDate() + username + ": " + receivedMessage);
						try {
							serverController.broadcastToReceivers(message);
						} catch (InterruptedException e) {
							serverController.appendEvent(serverController.getDate() + e.getMessage());
							serverController.logError(e);
						}
						
						serverController.appendChat(serverController.getDate() + username + ": " + receivedMessage);
						serverController.logMessage(message);
					} else if(message instanceof ServerMessage) {
						message.setMessage(serverController.getDate() + receivedMessage);
						serverController.broadcast(message);
						
						String key = ((ServerMessage) message).getDisconnectingUser();
						if(key != null) {
							remove(key);
							serverController.updateOnlineList();
						}
						
						serverController.appendChat(serverController.getDate() + receivedMessage);
						serverController.logMessage(message);
					} else {
						message.setMessage(serverController.getDate() + username + ": " + receivedMessage);
						serverController.broadcast(message);
						
						serverController.appendChat(serverController.getDate() + username + ": " + receivedMessage);
						serverController.logMessage(message);
					}
				}
			}
			
			remove(username);
			serverController.updateOnlineList();
		}

		/**
		 * Method to close Server
		 * @throws IOException
		 */
		public void close() throws IOException {
			if (socket != null) {
				serverController.logServerMessage(serverController.getDate() + "Closing Server");
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
				serverController.appendEvent(serverController.getDate() + "Message failed to broadcast");
				serverController.logServerMessage(serverController.getDate() + "Message failed to broadcast");
				serverController.logError(e);
			}
		}
		
		/**
		 * Method that updates online list for clients
		 * @param message
		 */
		public void updateOnline(ArrayList<String> list) {
			try {
				oos.writeObject(list);
				oos.flush();
			} catch(Exception e) { 
				serverController.appendEvent(serverController.getDate() + "Message failed to broadcast");
				serverController.logServerMessage(serverController.getDate() + "Message failed to broadcast");
				serverController.logError(e);
			}
		}
	}
}