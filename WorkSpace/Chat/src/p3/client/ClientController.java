package p3.client;

import java.awt.Color;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import p3.message.Message;

public class ClientController {
	private Client client;
	private ClientGUI clientGUI;
	
	public ClientController(Client client, ClientGUI clientGUI) {
		this.client = client;
		this.clientGUI = clientGUI;
	}
	
	public void appendChat(String msg, Color c) {
		clientGUI.appendChat(msg, c);
	}
	
	public void addImage(ImageIcon image){
		clientGUI.addImage(image);
	}
	
	public void updateOnlineList(ArrayList<String> cList) {
		clientGUI.updateOnlineList(cList);
	}
	
	public void writeMessage(Message message) {
		ObjectOutputStream oos = client.getOos();
		try {
			oos.writeObject(message);
			oos.flush();
		} catch(Exception e) { 
			appendChat("Failed to send Message", Color.YELLOW);
		}
	}
}
