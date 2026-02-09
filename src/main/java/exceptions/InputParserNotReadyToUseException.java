package exceptions;

public class InputParserNotReadyToUseException extends RuntimeException {

    public InputParserNotReadyToUseException(String message) {
        super(message);
    }
}
