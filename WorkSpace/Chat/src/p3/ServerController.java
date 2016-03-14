package p3;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * (Server)
 * Main klassen f�r programmet,hanterar hur det generella fl�det i programmet g�r.
 * @author Ludwig
 *
 */
public class ServerController extends Thread{
	private Server server;
	private Socket socket;
	private Message message;
	private Message lastMessage;
	
	public ServerController(int port){
		this.server = new Server(this, port);
		
	}
		public void setSocket(Socket socket) {
		this.socket = socket;
	}
		
	public void run() {
		System.out.println("Klient ansluten");
		try(ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
			while(true){
				System.out.println(message + "ute");
				if(message != null && message != lastMessage){
					System.out.println(message + "inne");
					oos.writeObject(message);
					oos.flush();
					lastMessage = message;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Klient DC");
	}
	public static void main(String[] args){
		
	}


}