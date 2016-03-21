package p3.server;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import p3.message.Message;

/**
 * (Log)
 * Simpel klass f�r att formatera logs till strings.
 * @author Alexander
 * @author Björn
 * @author David
 * @author Robert
 * @author Rasmus
 * @author Ludwig
 *
 */
public class LogFormatter {
	private Logger log;
	private FileHandler fh;

	/**
	 * Constructor for the LogFormatter
	 * @throws SecurityException
	 * @throws IOException
	 */
	public LogFormatter() throws SecurityException, IOException{
		log = Logger.getLogger("Chatlog");
		File dir = new File("Logfiles");
		if(!dir.exists()) {
			dir.mkdir();
		}
		
		fh = new FileHandler("Logfiles/"+log.getName()+" "+new SimpleDateFormat("M-d_HHmmss").format(Calendar.getInstance().getTime())+".txt",true);
		log.addHandler(fh);
		fh.setFormatter(new SimpleFormatter());
	}

	/**
	 * Method to log a standard chat message
	 * @param message the message class that was sent
	 */
	public void logMessage(Message message) {
		try {
			log.info(message.getMessage());
		} catch(Exception e) { 
			logError(e);
		}
	}
	
	/**
	 * Method to log specific server messages
	 * @param string the customized text to log
	 */
	public void logServerMessage(String string){
		try {
			log.warning(string);
		} catch(Exception e) {
			logError(e);
		}
	}
	
	/**
	 * Method to log any encountered errors
	 * @param e an exception
	 */
	public void logError(Exception e){
		log.severe(e.getMessage());
	}
}

