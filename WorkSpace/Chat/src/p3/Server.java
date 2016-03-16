package p3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
	private ServerGUI serverGUI;

	private int port;
	private boolean keepLooping = true;

	public Server(int port, ServerGUI gui) {
		this.port = port;
		this.serverGUI = gui;

		this.aListClients = new ArrayList<ClientHandler>();
	}

	public void start() {
		serverGUI.appendEvent("Server started");
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
	
	public void broadcast(String message) {
		for (int i = 0; i < aListClients.size(); ++i) {
			ClientHandler clientThread = aListClients.get(i);
			clientThread.writeMessage(new Message(MessageType.Server, message));
		}
	}

	private class ClientHandler extends Thread {
		private Socket socket;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private int id;
		private String username; // ?
		private Message message;

		public ClientHandler(Socket socket) throws IOException, ClassNotFoundException {
			this.id = ++uniqueID;

			this.socket = socket;
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			if(username == null) {
				username = (String) ois.readObject();
				oos.writeObject(id);
				oos.flush();
				serverGUI.appendEvent("ID: " + id);
			}
		}

		public void run() {
			while (true) {
				try {
					message = (Message) ois.readObject();
				} catch (IOException e) {
					serverGUI.appendEvent(e.getMessage());
					break;
				} catch (ClassNotFoundException e) {
					break;
				}

				String receivedMessage = message.getMessage();
				switch (message.getType()) {
				case Chat: {
					// logik h�r
					break;
				}
				case Command: {

					break;
				}
				case Private: {
					// tex till aListClient id = ??
					break;
				}
				case Group: {

					break;
				}
				case Server: {

					break;
				}
				default:
					break;
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
			} catch(Exception e) { 
				serverGUI.appendEvent("Message failed to broadcast");
			}
		}
	}
}