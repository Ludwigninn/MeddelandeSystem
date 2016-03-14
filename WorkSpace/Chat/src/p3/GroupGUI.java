package p3;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * GroupGUI visar ett chat fonster med medlemmar som man valt fr�n ClientGUI:et.
 * @author ludwi
 *
 */
public class GroupGUI extends JFrame implements ActionListener {
	private JPanel east;
	private JPanel main;
	private JPanel north;
	private JPanel center;
	private JPanel south;
	private JPanel southeastpnl;
	private JPanel southwestpnl;

	private JLabel namelbl;
	private JTextArea OnlineTextWindow;
	private JTextArea mainReadTextWindow;
	private JTextField typeTextWindow;

	private JButton sendBtn;


	public GroupGUI() {
		drawGUI();
		add();
	}

	public void drawGUI() {
		main = new JPanel(new BorderLayout(1, 1));
		east = new JPanel(new BorderLayout(1, 1));

		north = new JPanel(new BorderLayout(1, 1));
		center = new JPanel(new BorderLayout(1, 2));
		south = new JPanel(new BorderLayout(1, 2));
		southwestpnl = new JPanel(new BorderLayout(1, 1));
		southeastpnl = new JPanel(new BorderLayout(1, 1));
		namelbl = new JLabel("Name");

		mainReadTextWindow = new JTextArea("Text");
		typeTextWindow = new JTextField(47);
		OnlineTextWindow = new JTextArea("Online Lista   ");
		sendBtn = new JButton("Send");
	

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(700, 700);
		setVisible(true);
		mainReadTextWindow.setEditable(false);
		OnlineTextWindow.setEditable(false);
	}

	public void add() {
		north.add(namelbl);

		center.add(mainReadTextWindow);
		east.add(OnlineTextWindow);
		southwestpnl.add(typeTextWindow);
		southeastpnl.add(sendBtn, BorderLayout.WEST);
	

		south.add(southwestpnl, BorderLayout.WEST);
		south.add(southeastpnl, BorderLayout.EAST);
		main.add(north, BorderLayout.NORTH);
		main.add(center, BorderLayout.CENTER);
		main.add(south, BorderLayout.SOUTH);
		main.add(east, BorderLayout.EAST);

		add(main, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		new GroupGUI();

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendBtn) {

		}
	}
}