package mx.com.dgom.util.io.net;

/**
 * Created by beto on 10/29/15.
 */
public class NoInternetException extends Exception {

    public NoInternetException(Throwable t) {
        super(t);
    }

    public NoInternetException(String msg, Throwable t) {
        super(msg, t);
    }

    public NoInternetException(String msg) {
        super(msg);
    }
}
