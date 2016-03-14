package p3;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * (Server)
 * Det k�rs en handler per klient som �r ansluten till servern.De lyssnar efter meddelanden fr�n alla klienter.
 * @author Ludwig
 *
 */
public class ClientHandler extends Thread implements Observer{

	public ClientHandler(Socket socket){
		
	}
}