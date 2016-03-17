package p3;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



/**
 * (Log)
 * Simpel klass f�r att formatera logs till strings.
 * @author Björn
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

