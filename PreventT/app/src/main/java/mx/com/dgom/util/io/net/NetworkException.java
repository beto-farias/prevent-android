package mx.com.dgom.util.io.net;

public class NetworkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2841336715881427093L;

	public NetworkException(Throwable t) {
		super(t);
	}

	public NetworkException(String msg, Throwable t) {
		super(msg, t);
	}

	public NetworkException(String msg) {
		super(msg);
	}
}
