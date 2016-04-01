package p3.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import p3.server.NameInUseException;

/**
 * (GUI) F�nstret som syns innan man kopplat upp sig till servern d�r man f�r
 * skriva in information s� som ip och port.
 * 
 * Denna klass beh�ves inte n�dv�ndigtvis utan kan sl�ss ihop med ett annat gui
 * men tills vidare har vi den.
 * 
 * @author Alexander
 * @author Björn
 * @author David
 * @author Robert
 * @author Rasmus
 * @author Ludwig
 *
 */
public class StartGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = -167677549754509368L;
	private JTextField tfName;
	private JTextField tfPort;
	private JTextField tfServerAdress;
	private JPanel northPanel;
	private JPanel addressAndPort;
	private JButton btnLogin;
	private int port;
	private String address;
	private String username;

	public StartGUI(String username,String address, int port) {
		super("Login");
		this.port = port;
		this.address = address;
		this.username = username;
		drawGUI();
	}

	/**
	 * Ritar ut f�nstret f�r att skriva in Adress och port nummer.
	 */
	public void drawGUI() {
		// Login panelen
		northPanel = new JPanel(new GridLayout(1, 1));
		addressAndPort = new JPanel(new GridLayout(3, 1));
		tfServerAdress = new JTextField(address);
		tfPort = new JTextField("" + port);
		tfName = new JTextField("Anonymous");
		addressAndPort.add(new JLabel("Server Adress:  "));
		addressAndPort.add(tfServerAdress);
		addressAndPort.add(new JLabel("Port Number:  "));
		addressAndPort.add(tfPort);
		addressAndPort.add(new JLabel("Client Name:  "));
		addressAndPort.add(tfName);
		northPanel.add(addressAndPort);
		add(northPanel, BorderLayout.NORTH);

		// Knappar
		btnLogin = new JButton("Login");
		JPanel southPanel = new JPanel();
		southPanel.add(btnLogin);
		add(southPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 130);
		setVisible(true);
		
		btnLogin.addActionListener(this);
	}

	/**
	 * Ska skicka viader port och adress. Ska sedan starta upp CLientGUI som
	 * visar alla som �r inne p� servern och d�rifr�n kan man v�lja PM och GM.
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnLogin){
			new ClientThread().start();
		}
	}

	private class ClientThread extends Thread {
		public void run() {
			username = tfName.getText();
			address = tfServerAdress.getText();
			port = Integer.parseInt(tfPort.getText());
			try {
				new Client(username, address, port);
			} catch(NameInUseException e) {
				JOptionPane.showMessageDialog(null, "Name is in use, please choose another one!");
			}
		}
	}
	
	public static void main(String[] args) {
		new StartGUI("Anonymous", "localhost", 1500);
	}
}