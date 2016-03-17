package p3;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import p3.Message.MessageType;

/**
 * (Log)
 * Simpel klass f�r att formatera logs till strings.
 * @author Ludwig
 *
 */
public class LogFormatter {
	private Logger log;

	public LogFormatter() throws SecurityException, IOException{
		log = Logger.getLogger("Chatlog");
		log.addHandler(new FileHandler("Logfiles/"+log.getName()+"%u.txt",true));
	}
	public void logMessage(Message message) {
		LogRecord logRecord = new LogRecord(Level.INFO,  "\n"+message.getMessage());
		log.info(logRecord.getMessage());
		
	}

	public static void main(String[] args) throws SecurityException, IOException {
		LogFormatter lf = new LogFormatter();
		Message newMessage = new Message(MessageType.Command, "Hallå!");
		
		lf.logMessage(newMessage);
		newMessage=new Message(MessageType.Group,"Tjena!");
		lf.logMessage(newMessage);
	}

}

