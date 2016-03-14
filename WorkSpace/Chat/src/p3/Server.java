package p3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


/**
 * (Server)
 * Main klass f�r servern,hanterar hur servern fungerar.
 * @author Ludwig
 *
 */
public class Server extends Thread{
	private int port;
	private Thread thread = new Thread(this);
	ServerSocket serverSocket;
	private ServerController serverController;
	private LinkedList<Client> clientList;

	public Server(ServerController controller, int port){
		this.port = port;
		this.serverController = serverController;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		thread.start();
	}
	@Override
	public void run() {
		System.out.println("Server startad");
			while(true) {
				try {
					Socket socket = serverSocket.accept();
					serverController.setSocket(socket);
					serverController.start();
				} catch(IOException e) {
					System.err.println(e);
				}
			}
	}
}