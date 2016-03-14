package p3;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;

/**
 * (Server)
 * Det k�rs en handler per klient som �r ansluten till servern.De lyssnar efter meddelanden fr�n alla klienter.
 * @author Ludwig
 *
 */
public class ClientHandler extends Thread implements Observer{
	private Socket socket;
	private Message message;
	private Message lastMessage;

	public ClientHandler(Socket socket){
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

	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof Message){
			message=(Message)arg;
		}

	}
}


