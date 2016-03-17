package p3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.ws.LogicalMessage;

import p3.Message.MessageType;

/**
 * (Log)
 * Simpel klass f�r att formatera logs till strings.
 * @author Ludwig
 *
 */
public class LogFormatter {
	private Logger log;
	private FileHandler fh;

	public LogFormatter() throws SecurityException, IOException{
		log = Logger.getLogger("Chatlog");
		fh = new FileHandler("Logfiles/"+log.getName()+" "+new SimpleDateFormat("M-d_HHmmss").format(Calendar.getInstance().getTime())+".txt",true);
		log.addHandler(fh);
		fh.setFormatter(new SimpleFormatter());

	}

	public void logMessage(Message message) {
		try{
			log.info(""+ message.getType() + ": "+ message.getMessage());
		}catch(Exception e){ 
			logError(e);}
	}
	public void logError(Exception e){
		log.warning(""+e);
	}
}

