package dtu.alto.exception;

/**
 * Created by s150924 on 4/23/17.
 */

public class InvalidTypedAddrException extends Exception {
    public InvalidTypedAddrException(String message) {
        super(message);
    }

    public InvalidTypedAddrException(Throwable cause) {
        super(cause);
    }

    public InvalidTypedAddrException(String message, Throwable cause) {
        super(message, cause);
    }
}
