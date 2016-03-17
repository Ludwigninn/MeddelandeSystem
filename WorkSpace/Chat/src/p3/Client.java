package p3;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import p3.Message.MessageType;


/**
 * (Client) Representerar en klient som kan koppla upp sig till servern.
 * 
 * @author Ludwig
 *
 */
public class Client {
	private String username;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ClientGUI clientGUI;
	private int id = -1;
	private Message message;

	/**
	 * Constructor for each client connected with the server
	 * @param username
	 * @param server
	 * @param port
	 * @param clientGUI
	 */
	public Client(String username, String server, int port, ClientGUI clientGUI) {
		this.username = username;
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
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		new Listener().start();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Private threaded listenerclass which hanldles messages
	 * @author bjorsven
	 *
	 */
	private class Listener extends Thread {
		public void run() {
			while (true) {
				try {
					message = (Message) ois.readObject();
				} catch (Exception e) {
					clientGUI.appendChat(e.getMessage(), Color.YELLOW);
					e.printStackTrace();
					break;
				}

				String receivedMessage = message.getMessage();
				switch (message.getType()) {
					case Chat: {
						clientGUI.appendChat(receivedMessage, Color.BLACK);
						if(message.getImage()!=null)
							clientGUI.addImage(message.getImage());
						break;
					}
					case Command: {
	
						break;
					}
					case Private: {
						clientGUI.appendChat(receivedMessage, Color.CYAN);
						break;
					}
					case Group: {
						clientGUI.appendChat(receivedMessage, Color.BLUE);
						break;
					}
					case Server: {
						clientGUI.appendChat(receivedMessage, Color.ORANGE);
						break;
					}
					case Online: {
						clientGUI.addToOnline(message.getOnlineClients(), message.getOnlineIDs());
						break;
					}
					default:
						
					}
				}
			}
	}
	/**
	 * Method which adds a message to the outputstream
	 * @param message
	 */
	public void writeMessage(Message message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch(Exception e) { 
			clientGUI.appendChat("writeMessage failed to send Message", Color.YELLOW);
		}
	}
}