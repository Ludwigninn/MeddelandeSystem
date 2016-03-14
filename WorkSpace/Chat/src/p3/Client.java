package p3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

	public Client(String username, String server, int port, ClientGUI clientGUI) {
		this.username = username;
		this.server = server;
		this.port = port;
		this.clientGUI = clientGUI;
		try {

			socket = new Socket(server, port);
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (Exception e) {

		}

		new Listener().start();
	}

	private class Listener extends Thread {
		public void run() {

			try {
				oos.writeObject(username);

			} catch (Exception e) {

			}
		}
	}
}