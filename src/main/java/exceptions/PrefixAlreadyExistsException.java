package exceptions;

public class PrefixAlreadyExistsException extends RuntimeException {
    public PrefixAlreadyExistsException(String message) {
        super(message);
    }
}
