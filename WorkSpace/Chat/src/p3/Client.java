package p3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import p3.Message.MessageType;


/**
 * (Client) Representerar en klient som kan koppla upp sig till servern.
 * 
 * @author Ludwig
 *
 */
public class Client {
	private String username;
	private int port;
	private String server;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ClientGUI clientGUI;
	private int id = -1;
	private Message message;

	public Client(String username, String server, int port, ClientGUI clientGUI) {
		this.username = username;
		this.server = server;
		this.port = port;
		this.clientGUI = clientGUI;

		try {
			socket = new Socket(server, port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			if(id == -1) {
				try {
					oos.writeObject(username);
					oos.flush();
					id = (int) ois.readObject();
					this.clientGUI.appendChat(username + " - ID: " + id + " - connected");
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		new Listener().start();
	}

	private class Listener extends Thread {
		public void run() {
			while (true) {
				try {
					message = (Message) ois.readObject();
				} catch (IOException e) {
					clientGUI.appendChat(e.getMessage());
					break;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					break;
				}

				String receivedMessage = message.getMessage();
				switch (message.getType()) {
					case Chat: {
						clientGUI.appendChat(receivedMessage);
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
						clientGUI.appendChat(receivedMessage);
						break;
					}
					default:
						break;
					}
				}
			}
	}
	
	public void writeMessage(Message message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch(Exception e) { 
			clientGUI.appendChat("writeMessage failed to send Message");
		}
	}
}