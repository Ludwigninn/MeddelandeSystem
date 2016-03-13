package p3;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * (GUI) Fönstret som syns innan man kopplat upp sig till servern där man får
 * skriva in information så som ip och port.
 * 
 * Denna klass behöves inte nödvändigtvis utan kan släss ihop med ett annat gui
 * men tills vidare har vi den.
 * 
 * @author Ludwig
 *
 */
public class StartGUI extends JFrame implements ActionListener {
	private JTextField TextFieldPort;
	private JTextField TextFieldServerAdress;
	private JPanel northPanel;
	private JPanel addressAndPort;
	private JButton login;
	private int port;
	private String adress;

	public StartGUI(String adress, int port) {
		this.port = port;
		this.adress = adress;
		drawGUI();
	}

	/**
	 * Ritar ut fönstret för att skriva in Adress och port nummer.
	 */
	public void drawGUI() {

		// Login panelen
		northPanel = new JPanel(new GridLayout(1, 1));
		addressAndPort = new JPanel(new GridLayout(3, 1));
		TextFieldServerAdress = new JTextField(adress);
		TextFieldPort = new JTextField(port);
		addressAndPort.add(new JLabel("Server Adress:  "));
		addressAndPort.add(TextFieldServerAdress);
		addressAndPort.add(new JLabel("Port Number:  "));
		addressAndPort.add(TextFieldPort);
		northPanel.add(addressAndPort);
		add(northPanel, BorderLayout.NORTH);

		// Knappar
		login = new JButton("Login");
		JPanel southPanel = new JPanel();
		southPanel.add(login);
		add(southPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 130);
		setVisible(true);

	}

	/**
	 * Ska skicka viader port och adress. Ska sedan starta upp CLientGUI som
	 * visar alla som är inne på servern och därifrån kan man välja PM och GM.
	 */
	public void actionPerformed(ActionEvent arg0) {

	}

	public static void main(String[] args) {
		new StartGUI("localhost", 1337);
	}
}
