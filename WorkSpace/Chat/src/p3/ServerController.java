package p3;
/**
 * (Server)
 * Main klassen f�r programmet,hanterar hur det generella fl�det i programmet g�r.
 * @author Ludwig
 *
 */
public class ServerController {
	private Server server;
	
	public ServerController(String address, int port){
		this.server = new Server(this,address, port);
	}
	
	public static void main(String[] args){
		
	}
}