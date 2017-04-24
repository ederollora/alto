package dtu.alto.exception;

/**
 * Created by s150924 on 4/23/17.
 */

public class InvalidTypedAddrFormatException extends Exception {
    public InvalidTypedAddrFormatException(String message) {
        super(message);
    }

    public InvalidTypedAddrFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidTypedAddrFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
