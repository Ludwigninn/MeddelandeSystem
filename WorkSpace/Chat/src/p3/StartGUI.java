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
 * (GUI) F�nstret som syns innan man kopplat upp sig till servern d�r man f�r
 * skriva in information s� som ip och port.
 * 
 * Denna klass beh�ves inte n�dv�ndigtvis utan kan sl�ss ihop med ett annat gui
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
	private String address;
	private ServerController serverController =null;

	public StartGUI(String address, int port) {
		this.port = port;
		this.address = address;
		drawGUI();

	}

	/**
	 * Ritar ut f�nstret f�r att skriva in Adress och port nummer.
	 */
	public void drawGUI() {

		// Login panelen
		northPanel = new JPanel(new GridLayout(1, 1));
		addressAndPort = new JPanel(new GridLayout(3, 1));
		TextFieldServerAdress = new JTextField(address);
		TextFieldPort = new JTextField(""+port);
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
		
		login.addActionListener(this);

	}

	/**
	 * Ska skicka viader port och adress. Ska sedan starta upp CLientGUI som
	 * visar alla som �r inne p� servern och d�rifr�n kan man v�lja PM och GM.
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==login){
			System.out.println("Startar Servern!");
			serverController=new ServerController(TextFieldServerAdress.getText(),Integer.parseInt(TextFieldPort.getText()));

		}
	}

	public static void main(String[] args) {
		new StartGUI("localhost", 1337);
	}
}