package p3.client;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import p3.message.GroupMessage;
import p3.message.Message;
import p3.message.PrivateMessage;
import p3.message.ServerMessage;
import p3.server.NameInUseException;


/**
 * (Client) Representerar en klient som kan koppla upp sig till servern.
 * @author Alexander
 * @author Björn
 * @author David
 * @author Robert
 * @author Rasmus
 * @author Ludwig
 *
 */
public class Client {
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ObjectOutputStream getOos() {
		return oos;
	}

	private ClientController clientController;
	private Message message;
	private ArrayList<String> clientList;

	public ArrayList<String> getClientList() {
		return clientList;
	}

	/**
	 * Constructor for each client connected with the server
	 * @param username
	 * @param server
	 * @param port
	 * @param clientController
	 */
	public Client(String username, String server, int port) throws NameInUseException {
		try {
			socket = new Socket(server, port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			try {
				oos.writeObject(username);
				oos.flush();
				
				int returnValue = ois.readInt();
				if(returnValue == -1) {
					JOptionPane.showMessageDialog(null, "Name is in use, please choose another one!");
					throw new NameInUseException("Name in use!");
				} else {
					ClientGUI gui = new ClientGUI(username);
					
					ClientController clientController = new ClientController(this, gui);
					this.setClientController(clientController);
					gui.setClientController(clientController);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		new Listener().start();
	}
	
	public void setClientController(ClientController controller) {
		this.clientController = controller;
	}

	/**
	 * Private threaded listenerclass which hanldles messages
	 * @author bjorsven
	 *
	 */
	private class Listener extends Thread implements Serializable {
		private static final long serialVersionUID = 1958587906766481842L;

		@SuppressWarnings("unchecked")
		public void run() {
			while (true) {
				try {
					Object output = (Object) ois.readObject();
					message = null;
					clientList = null;
					if(output instanceof GroupMessage) {
						message = (GroupMessage) output;
					} else if(output instanceof PrivateMessage) {
						message = (PrivateMessage) output;
					} else if(output instanceof ServerMessage) {
						message = (ServerMessage) output;
					} else if(output instanceof ArrayList) {
						clientList = ((ArrayList<String>) output);
					} else {
						message = (Message) output;
					}
				} catch (Exception e) {
					clientController.appendChat(e.getMessage(), Color.YELLOW);
					e.printStackTrace();
					break;
				}

				if(message != null) {
					String receivedMessage = message.getMessage();
					if(message instanceof GroupMessage) {
						clientController.appendChat(receivedMessage, Color.BLUE);
					} else if(message instanceof PrivateMessage) {
						clientController.appendChat(receivedMessage, Color.CYAN);
					} else if(message instanceof ServerMessage) {
						clientController.appendChat(receivedMessage, Color.ORANGE);
					} else {
						clientController.appendChat(receivedMessage, Color.BLACK);
					}
					
					if(message.getImage() != null) {
						clientController.addImage(message.getImage());
					}
				} else if(clientList != null) {
					clientController.updateOnlineList(clientList);
				}
			}
		}
	}
}