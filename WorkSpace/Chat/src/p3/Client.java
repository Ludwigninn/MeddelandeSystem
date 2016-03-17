package p3;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
	private int port;
	private String server;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ClientGUI clientGUI;
	private int id = -1;
	private Message message;
	private ArrayList<String> onlineList;

	public Client(String username, String server, int port, ClientGUI clientGUI) {
		this.username = username;
		this.server = server;
		this.port = port;
		this.clientGUI = clientGUI;
		this.onlineList = new ArrayList<String>();

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
	
	public int getIdByUsername(String username) {
		if(username == this.username) {
			return id;
		}
		
		return -1;
	}

	public void setId(int id) {
		this.id = id;
	}

	private class Listener extends Thread {
		public void run() {
			while (true) {
				try {
					onlineList = (ArrayList) ois.readObject();
					clientGUI.appendChat("" + onlineList.size(), Color.YELLOW);
					clientGUI.addToOnline(onlineList);
				} catch (IOException e) {
					clientGUI.appendChat(e.getMessage(), Color.YELLOW);
					break;
				} catch (ClassNotFoundException e) {
					break;
				}
				
				try {
					message = (Message) ois.readObject();
				} catch (IOException e) {
					clientGUI.appendChat(e.getMessage(), Color.YELLOW);
					break;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					break;
				}

				String receivedMessage = message.getMessage();
				switch (message.getType()) {
					case Chat: {
						clientGUI.appendChat(receivedMessage, Color.BLACK);
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
						clientGUI.appendChat(receivedMessage, Color.YELLOW);
						break;
					}
					default:
						
					}
				}
			}
	}
	
	public void writeMessage(Message message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch(Exception e) { 
			clientGUI.appendChat("writeMessage failed to send Message", Color.YELLOW);
		}
	}
}