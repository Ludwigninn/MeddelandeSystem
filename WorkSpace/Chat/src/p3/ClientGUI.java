package p3;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import java.awt.*;

import java.awt.event.*;

import javax.swing.*;

import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import p3.Message.MessageType;

/**
 * ClientGUI vissar Klients fonster. Det gar ocksa att navigera vidare till GM
 * och PM. * @author Ludwig
 *
 */
public class ClientGUI extends JFrame implements ActionListener, WindowListener {
	private JPanel east;
	private JPanel main;
	private JPanel north;
	private JPanel center;
	private JPanel south;
	private JPanel southeastpnl;
	private JPanel southwestpnl;

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
	
	private int[] idList;

	private Client client;

	public ClientGUI(String username, String server, int port) {
		super(username);
		this.username = username;
		drawGUI();
		add();
		this.client = new Client(username, server, port, this);
	}

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

		list = new DefaultListModel<String>();
		onlineListWindow = new JList<String>(list);
		onlineListWindow.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		onlineListWindow.setLayoutOrientation(JList.VERTICAL_WRAP);
		onlineListWindow.setVisibleRowCount(-1);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(700, 700);
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

	public void add() {
		north.add(namelbl);

		center.add(mainTextPane);
		center.add(new JScrollPane(mainTextPane));
		east.add(onlineListWindow);
		southwestpnl.add(typeTextWindow);
		southeastpnl.add(sendBtn, BorderLayout.WEST);
		southeastpnl.add(privateMessage, BorderLayout.EAST);
		southeastpnl.add(groupMessage);

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
	}

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

	public void addToOnline(String[] onlineList, int[] onlineIds) {
		list.removeAllElements();
		idList = onlineIds;
		for(int i = 0; i < onlineList.length; i++) {
			list.addElement(onlineList[i]);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendBtn) {
			if (typeTextWindow.getText() != "") {
				sendMessage = typeTextWindow.getText();
				Message message = new Message(MessageType.Chat, sendMessage);
				client.writeMessage(message);
				typeTextWindow.setText(null);
			}
		} else if (e.getSource() == groupMessage) {
			int selections[] = onlineListWindow.getSelectedIndices();
	        Object selectedValues[] = onlineListWindow.getSelectedValues();
	        for (int i = 0, n = selections.length; i < n; i++) {
	        	if (i == 0) {
	            	appendChat("  Selections: ", Color.BLUE);
	            }
	        	
	        	appendChat(selections[i] + "/" + selectedValues[i] + " ", Color.BLUE);
	        }
			/*int[] list = onlineListWindow.getSelectedIndices();
			if(list.length > 0) {
	        	for(int value : list) {
	        		appendChat("" + value, Color.BLUE);
	        		appendChat("" + idList[value - 1], Color.BLUE);
	        	}
			}*/
		} else if (e.getSource() == privateMessage) {
        	appendChat("" + onlineListWindow.getSelectedIndex(), Color.BLUE);
        	appendChat("" + idList[onlineListWindow.getSelectedIndex() - 1], Color.BLUE);
		}
	}
	
	@Override
	public void windowClosing(WindowEvent arg0) {
		Message message = new Message(MessageType.Server, "");
		message.setSenderID(client.getId());
		client.writeMessage(message);
		
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