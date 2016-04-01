package p3.client;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import p3.message.GroupMessage;
import p3.message.Message;
import p3.message.PrivateMessage;
import p3.message.ServerMessage;

/**
 * ClientGUI vissar Klients fonster. Det gar ocksa att navigera vidare till GM
 * och PM. 
 * @author Alexander
 * @author Björn
 * @author David
 * @author Robert
 * @author Rasmus
 * @author Ludwig
 *
 */
public class ClientGUI extends JFrame implements ActionListener, WindowListener {
	private static final long serialVersionUID = 7743903121933661037L;
	private JPanel east;
	private JPanel main;
	private JPanel north;
	private JPanel center;
	private JPanel south;
	private JPanel southeastpnl;
	private JPanel southwestpnl;

	private JFileChooser jfc = new JFileChooser();
	private File file;

	private JLabel namelbl;
	private JList<String> onlineListWindow;

	private JTextPane mainTextPane;
	private JTextField typeTextWindow;

	private DefaultListModel<String> list;
	private JButton sendBtn;
	private JButton privateMessage;
	private JButton groupMessage;
	private String username;
	private String sendMessage;
	private JButton sendFileBtn;
	
	private ClientController clientController;

	/**
	 * Constructor for each client GUI
	 * @param username
	 * @param server
	 * @param port
	 */
	public ClientGUI(String username) {
		super(username);
		this.username = username;
		drawGUI();
		add();
	}
	
	public void setClientController(ClientController controller) {
		this.clientController = controller;
	}

	/**
	 * Method to draw the GUI
	 */
	public void drawGUI() {
		main = new JPanel(new BorderLayout(1, 1));
		east = new JPanel(new BorderLayout(1, 1));

		north = new JPanel(new BorderLayout(1, 1));
		center = new JPanel(new BorderLayout(1, 2));
		south = new JPanel(new BorderLayout(1, 2));
		southwestpnl = new JPanel(new BorderLayout(1, 1));
		southeastpnl = new JPanel(new BorderLayout(1, 1));
		namelbl = new JLabel(username);

		mainTextPane = new JTextPane();
		typeTextWindow = new JTextField(47);

		sendBtn = new JButton("Send");
		privateMessage = new JButton("PM");
		groupMessage = new JButton("GM");
		sendFileBtn = new JButton("File");

		list = new DefaultListModel<String>();
		onlineListWindow = new JList<String>(list);
		onlineListWindow.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		onlineListWindow.setLayoutOrientation(JList.VERTICAL_WRAP);
		onlineListWindow.setVisibleRowCount(-1);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(762, 400);
		setVisible(true);

		mainTextPane.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				mainTextPane.setEditable(true);
			}

			@Override
			public void focusGained(FocusEvent e) {
				mainTextPane.setEditable(false);
			}
		});

		addWindowListener(this);
	}

	/**
	 * Method to add all components
	 */
	public void add() {
		north.add(namelbl);

		center.add(mainTextPane);
		center.add(new JScrollPane(mainTextPane));
		east.add(onlineListWindow);
		southwestpnl.add(typeTextWindow, BorderLayout.WEST);
		southwestpnl.add(sendBtn, BorderLayout.EAST);
		southeastpnl.add(privateMessage, BorderLayout.EAST);
		southeastpnl.add(groupMessage);
		southeastpnl.add(sendFileBtn, BorderLayout.WEST);

		south.add(southwestpnl, BorderLayout.WEST);
		south.add(southeastpnl, BorderLayout.EAST);
		main.add(north, BorderLayout.NORTH);
		main.add(center, BorderLayout.CENTER);
		main.add(south, BorderLayout.SOUTH);
		main.add(east, BorderLayout.EAST);

		add(main, BorderLayout.CENTER);
		sendBtn.addActionListener(this);
		groupMessage.addActionListener(this);
		privateMessage.addActionListener(this);
		sendFileBtn.addActionListener(this);
	}

	/**
	 * Method that updates the color and text in the TextPane
	 * @param msg
	 * @param c
	 */
	public void appendChat(String msg, Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		int len = mainTextPane.getDocument().getLength();
		mainTextPane.setCaretPosition(len);
		mainTextPane.setCharacterAttributes(aset, false);
		mainTextPane.replaceSelection("\n" + msg);
	}

	/**
	 * Adds all online users to the onlineList
	 * @param onlineList
	 * @param onlineIds
	 */
	public void updateOnlineList(ArrayList<String> cList) {
		list.removeAllElements();
		for(String client : cList) {
			list.addElement(client);
		}
	}
	
	/**
	 * adds an image to the window
	 * @param image
	 */
	public void addImage(ImageIcon image){
		mainTextPane.insertIcon(image);
	}
	
	/**
	 * The actionlistener performed when buttons are pressed
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == sendBtn) {
				if (!typeTextWindow.getText().isEmpty()) {
					sendMessage = typeTextWindow.getText();
					Message message = new Message(sendMessage);
					
					if(file != null) {
						message.setImage(new ImageIcon(ImageIO.read(file)));
					}
					
					clientController.writeMessage(message);
					typeTextWindow.setText(null);
					file = null;
				}
			} else if (e.getSource() == groupMessage) {
				if (!typeTextWindow.getText().isEmpty()) {
					List<String> selections = onlineListWindow.getSelectedValuesList();
					sendMessage = typeTextWindow.getText();
					GroupMessage message = new GroupMessage(sendMessage);
					
					if(file != null) {
						message.setImage(new ImageIcon(ImageIO.read(file)));
					}
					
					boolean selfReceiver = false;
					for(String receiver : selections) {
						message.addReceiver(receiver);
						
						if(receiver.equals(username)) {
							selfReceiver = true;
						}
					}
					
					if(!selfReceiver) {
						message.addReceiver(username);
					}
					
					clientController.writeMessage(message);
					typeTextWindow.setText(null);
					file = null;
				}
			} else if (e.getSource() == privateMessage) {
				if (!typeTextWindow.getText().isEmpty()) {
					sendMessage = typeTextWindow.getText();
					PrivateMessage message = new PrivateMessage(sendMessage);
					
					if(file != null) {
						message.setImage(new ImageIcon(ImageIO.read(file)));
					}
					
					message.addReceiver(onlineListWindow.getSelectedValue());
					message.addReceiver(username);
					
					clientController.writeMessage(message);
					typeTextWindow.setText(null);
					file = null;
				}
			} else if (e.getSource() == sendFileBtn) {
				jfc.showOpenDialog(null);
				file = jfc.getSelectedFile();
			}
		} catch(IOException ex) {
			
		}
	}

	/**
	 * method that decides what to happen when the window closes
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		ServerMessage message = new ServerMessage(username + " disconnected");
		message.setDisconnectingUser(username);
		clientController.writeMessage(message);

		dispose();
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