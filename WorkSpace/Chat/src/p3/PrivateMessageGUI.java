package p3;

/**
 * (GUI)
 * Representerar ett f�nster med en gruppchatt med andra klienter skapade av servern.
 * 
 * @author Rasmus
 *
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PrivateMessageGUI extends JFrame implements ActionListener {

	private JPanel main;
	private JPanel north;
	private JPanel center;
	private JPanel south;
	private JPanel southeastpnl;
	private JPanel southwestpnl;
	private JLabel namelbl;
	private JTextArea mainReadTextWindow;
	private JTextField typeTextWindow;
	private JButton sendBtn;

	public PrivateMessageGUI() {
		drawGUI();
		add();
	}

	public void drawGUI() {
		main = new JPanel(new BorderLayout(1, 1));

		north = new JPanel(new BorderLayout(1, 1));
		center = new JPanel(new BorderLayout(1, 1));
		south = new JPanel(new BorderLayout(1, 2));
		southwestpnl = new JPanel(new BorderLayout(1, 2));
		southeastpnl = new JPanel(new BorderLayout(1, 2));
		namelbl = new JLabel("Name");

		mainReadTextWindow = new JTextArea("hej");
		typeTextWindow = new JTextField(47);

		sendBtn = new JButton("Send");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		mainReadTextWindow.setEditable(false);

	}

	public void add() {

		north.add(namelbl);

		center.add(mainReadTextWindow);

		southwestpnl.add(typeTextWindow);
		southeastpnl.add(sendBtn);

		south.add(southwestpnl, BorderLayout.WEST);
		south.add(southeastpnl, BorderLayout.EAST);
		main.add(north, BorderLayout.NORTH);
		main.add(center, BorderLayout.CENTER);
		main.add(south, BorderLayout.SOUTH);

		add(main, BorderLayout.CENTER);

	}

	public static void main(String[] args) {
		new PrivateMessageGUI();

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendBtn) {

		}
	}
}