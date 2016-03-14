package p3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * (Server)
 * Main klass f�r servern,hanterar hur servern fungerar.
 * @author Ludwig
 *
 */
public class Server extends Thread{
	private int port;
	private String address;
	private Thread thread = new Thread(this);
	ServerSocket serverSocket;

	public Server(ServerController controller,String address, int port){
		this.port = port;
		this.address = address;
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
					new ClientHandler(socket).start();
				} catch(IOException e) {
					System.err.println(e);
				}
			}
	}
}
