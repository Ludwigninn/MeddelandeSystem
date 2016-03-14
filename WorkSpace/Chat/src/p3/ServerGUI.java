package p3;
/**
 * (GUI)
 * F�nstres f�r servern. 
 * @author Ludwig
 *
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ServerGUI extends JFrame implements ActionListener, WindowListener {
    private static final long serialVersionUID = 1L;
    private JButton btnStart;
    private JTextArea txtChat, txtEvent;
    private JTextField tfPortNumber, tfTextWindow;
    private JButton btnBroadcast;
    private ServerController controller;
    
    public ServerGUI(int port) {
        super("Chat Server");
        
        JPanel north = new JPanel();
        north.add(new JLabel("Port: "));
        tfPortNumber = new JTextField("  " + port);
        north.add(tfPortNumber);

        btnStart = new JButton("Start Server");
        btnStart.addActionListener(this);
        north.add(btnStart);
        add(north, BorderLayout.NORTH);
 
        JPanel center = new JPanel(new GridLayout(2,1));
        txtChat = new JTextArea(80,80);
        txtChat.setEditable(false);
        appendChat("Broadcast log\n");
        center.add(new JScrollPane(txtChat));
        JPanel mid = new JPanel();
        tfTextWindow = new JTextField(24);
        tfTextWindow.setEditable(true);
        btnBroadcast = new JButton("Broadcast");
        mid.add(tfTextWindow);
        mid.add(btnBroadcast);
        add(mid, BorderLayout.SOUTH);
        btnBroadcast = new JButton("Broadcast");
        
        txtEvent = new JTextArea(80,80);
        txtEvent.setEditable(false);
        appendEvent("Event log\n");
        center.add(new JScrollPane(txtEvent));
        add(center);
        addWindowListener(this);
        setSize(400, 500);
        setVisible(true);
    }      

    void appendChat(String str) {
        txtChat.append(str);
    }

    void appendEvent(String str) {
        txtEvent.append(str);
    }
    
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == btnStart) {
    		int port;
    		try {
                port = Integer.parseInt(tfPortNumber.getText().trim());
            } catch(Exception er) {
                appendEvent("Invalid port");
                return;
            }
    		
    		controller = new ServerController(this, port);
    	} else if(e.getSource() == btnBroadcast) {
    		try {
                
            } catch(Exception er) {
                appendChat("Server offline");
                return;
            }
    	}
    }

    public static void main(String[] arg) {
        new ServerGUI(1500);
    }
    
	@Override
	public void windowClosing(WindowEvent arg0) {
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

