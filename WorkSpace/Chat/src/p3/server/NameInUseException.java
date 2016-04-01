package p3.server;

public class NameInUseException extends Exception {
	private static final long serialVersionUID = 1729027235630360050L;
	
	public NameInUseException() {}
    
	public NameInUseException(String message) {
		super(message);
	}
}