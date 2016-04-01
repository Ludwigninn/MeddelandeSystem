package p3.server;
/**
 * (GUI)
 * F�nstres f�r servern. 
 * @author Ludwig
 *
 */
import javax.swing.*;

import p3.message.ServerMessage;

import java.awt.*;
import java.awt.event.*;

/**
 * The GUI of the Server
 * @author Alexander
 * @author Björn
 * @author David
 * @author Robert
 * @author Rasmus
 * @author Ludwig
 *
 */
public class ServerGUI extends JFrame implements ActionListener, WindowListener {
	private static final long serialVersionUID = -503920831543556685L;
	private JButton btnStart;
    private JTextArea txtChat, txtEvent;
    private JTextField tfPortNumber, tfTextWindow;
    private JButton btnBroadcast;
    
    private Server server;
    private ServerController serverController;
    
    /**
     * Constructor for the GUI
     * @param port
     */
    public ServerGUI(int port) {
        super("Chat Server");
        
        JPanel north = new JPanel();
        north.add(new JLabel("Port: "));
        tfPortNumber = new JTextField("" + port);
        north.add(tfPortNumber);

        btnStart = new JButton("Start/Stop Server");
        btnStart.addActionListener(this);
        north.add(btnStart);
        add(north, BorderLayout.NORTH);
 
        JPanel center = new JPanel(new GridLayout(2,1));
        txtChat = new JTextArea(80,80);
        txtChat.setEditable(false);
        txtChat.setText("CHAT LOG");
        center.add(new JScrollPane(txtChat));
        JPanel mid = new JPanel();
        tfTextWindow = new JTextField(24);
        tfTextWindow.setEditable(true);
        btnBroadcast = new JButton("Broadcast");
        btnBroadcast.addActionListener(this);
        mid.add(tfTextWindow);
        mid.add(btnBroadcast);
        add(mid, BorderLayout.SOUTH);
        
        txtEvent = new JTextArea(80,80);
        txtEvent.setEditable(false);
        txtEvent.setText("EVENT LOG");
        center.add(new JScrollPane(txtEvent));
        add(center);
        addWindowListener(this);
        setSize(400, 500);
        setVisible(true);
    }      

    /**
     * Method to add a message in the chat window
     * @param text
     */
    public void appendChat(String text) {
        txtChat.append("\n" + text);
    }

    /**
     * Method to add text to the event window
     * @param text
     */
    public void appendEvent(String text) {
        txtEvent.append("\n" + text);
    }
    
    /**
     * The action perfromed for the buttons
     */
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == btnStart) {
    		if(server != null) {
    			server.stop();
    			
    			return;
    		}
    		
    		int port;
    		try {
                port = Integer.parseInt(tfPortNumber.getText().trim());
                
                server = new Server(port);
                serverController = new ServerController(server, this);
                server.setServerController(serverController);
                new ServerThread().start();
            } catch(Exception er) {
            	appendEvent(serverController.getDate() + "Invalid port");
                serverController.logServerMessage(serverController.getDate() + "Invalid port");
                serverController.logError(er);
            }
    	} else if(e.getSource() == btnBroadcast) {
    		if(server != null) {
	    		try {
	    			if(tfTextWindow.getText() != null) {
	    				String message = tfTextWindow.getText();
		                serverController.broadcast(new ServerMessage(message));
		                
		                appendChat(serverController.getDate() + message);
		                serverController.logServerMessage(serverController.getDate() + message);
		                tfTextWindow.setText(null);
	    			}
	            } catch(Exception er) {
	                appendEvent(serverController.getDate() + "Server offline");
	                serverController.logServerMessage(serverController.getDate() + "Server offline");
	                serverController.logError(er);
	            }
    		}
    	}
    }

    /**
     * the start of the program
     * @param arg
     */
    public static void main(String[] arg) {
        new ServerGUI(1500);
    }
    
    /**
     * Threaded class to run the server with
     * 
     *
     */
    private class ServerThread extends Thread {
    	public void run() {
    		server.start();
    	}
    }
    
    /**
     * Method which acts when the window closes
     */
	@Override
	public void windowClosing(WindowEvent arg0) {
		if(server != null) {
			server.stop();
		}
		
		dispose();
        System.exit(0);
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {}
	
	@Override
	public void windowClosed(WindowEvent arg0) {}
	
	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {} 
}

