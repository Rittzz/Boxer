package wittz.boxer;

/**
 * Created on 6/29/15.
 */
public class BoxerException extends Exception {
    public BoxerException() {
    }

    public BoxerException(String message) {
        super(message);
    }

    public BoxerException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoxerException(Throwable cause) {
        super(cause);
    }

    public BoxerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
