package mx.com.dgom.util.io.file;

public class FileResourcesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7457864878076561982L;

	public FileResourcesException(String m) {
		super(m);
	}

	public FileResourcesException(Throwable t) {
		super(t);
	}
	
	public FileResourcesException(String m, Throwable t) {
		super(m,t);
	}
}
